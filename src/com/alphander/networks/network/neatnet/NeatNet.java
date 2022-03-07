package com.alphander.networks.network.neatnet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.alphander.networks.network.Network;
import com.alphander.networks.network.activation.Activator;
import com.alphander.networks.network.activation.activators.Tanh;
import com.alphander.networks.network.neatnet.mutation.Mutation;
import com.alphander.networks.network.neatnet.mutation.mutations.WeightRandomMutation;
import com.alphander.networks.network.neatnet.mutation.mutations.WeightShiftMutation;
import com.alphander.networks.network.neatnet.structure.Genome;
import com.alphander.networks.network.neatnet.structure.Topology;
import com.alphander.networks.network.neatnet.mutation.mutations.ToggleMutation;
import com.alphander.networks.network.neatnet.mutation.mutations.NodeMutation;
import com.alphander.networks.network.neatnet.mutation.mutations.LinkMutation;
import com.alphander.networks.utils.NetArray;

public class NEATNet implements Network
{
	public String name = "NEATNet";
	public int population = 50;
	public float speciesThresh = 4f;
	public float deathRate = 0.3f;
	public float repopTypeThresh = 0.4f;;

	public int inputDims, outputDims;
	public Activator activator = new Tanh();
	public Mutation[] mutations = new Mutation[] 
	{
		new LinkMutation(0.01f),
		new NodeMutation(0.1f),
		new WeightShiftMutation(0.2f),
		new WeightRandomMutation(0.2f),
		new ToggleMutation(0.01f),
	};
	public ArrayList<Genome> genomes = new ArrayList<Genome>();

	private float[] inputs, outputs;
	private Random random = new Random();
	
	private Topology current;
	public int index = 0;
	int generations = 0;

	public NEATNet(int inputDims, int outputDims)
	{
		if(inputDims < 1 || outputDims < 1) throw new IllegalArgumentException();

		this.inputDims = inputDims;
		this.outputDims = outputDims;

		for(int i = 0; i < population; i++)
			genomes.add(new Genome(activator, mutations, inputDims, outputDims));
		this.current = genomes.get(0).topo;
	}
	
	public NEATNet(String name, int inputDims, int outputDims, float deathRate, int population, float repopTypeThresh, 
		float speciesThresh, Activator activator, Mutation[] mutations, ArrayList<Genome> genomes, int index)
	{
		this.name = name;
		this.inputDims = inputDims;
		this.outputDims = outputDims;
		this.deathRate = deathRate;
		this.population = population;
		this.repopTypeThresh = repopTypeThresh;
		this.speciesThresh = speciesThresh;
		this.activator = activator;
		this.mutations = mutations;
		this.genomes = genomes;
		this.current = genomes.get(index).topo;
		this.index = index;
	}

	@Override
	public NetArray run(NetArray in)
	{
		if(in.length() != inputDims) throw new ArrayIndexOutOfBoundsException();

		this.inputs = in.array();

		float[] output = current.forward(inputs);
		this.outputs = output;
		return new NetArray(output);
	}

	@Override
	public NetArray getInput()
	{
		return new NetArray(inputs);
	}

	@Override
	public NetArray getOutput()
	{
		return new NetArray(outputs);
	}
	
	@Override
	public String getName()
	{
		return name;
	}

	public void setMutations(Mutation[] mutations)
	{
		this.mutations = mutations;
	}

	@Override
	public void train(NetArray in)
	{
		int length = in.length();
		if(length != outputDims) return;

		float error = 0f;
		for(int i = 0; i < length; i++)
		{
			float d = in.get(i) * outputs[i];
			error += d * d;
		}

		train(-error);
	}

	public void train(float score)
	{
		index = (index + 1) % genomes.size();
		Genome owner = genomes.get(index);
		Topology instance = owner.topo;
		owner.score = score;
		current = instance;

		if(index == 0) speciate();
	}
	
	private void speciate()
	{
		ArrayList<ArrayList<Genome>> all = new ArrayList<ArrayList<Genome>>();//Species
		ArrayList<Genome> initSpecies = new ArrayList<Genome>();
		initSpecies.add(genomes.get(0));
		all.add(initSpecies);

		//Speciate genomes.
		for(Genome candidate : genomes)
		{
			float smallest = Float.MAX_VALUE;
			ArrayList<Genome> best = null;
			for(ArrayList<Genome> species : all)
				for(Genome genome : species)
				{
					float heuristic = candidate.heuristic(genome);
					if(smallest > heuristic)
					{
						smallest = heuristic;
						best = species;
					}
				}

			if(smallest > speciesThresh)
			{
				ArrayList<Genome> newArray = new ArrayList<Genome>();
				newArray.add(candidate);
				all.add(newArray);
			}
			else
				best.add(candidate);
		}

		//Sort by score.
		for(ArrayList<Genome> species : all)
			Collections.sort(species);

		//Cull genomes.
		int removed = 0;
		for(ArrayList<Genome> s : all)
		{
			float size = s.size() * deathRate;
			for(int i = 0; i < size; i++)
			{
				Genome remove = s.remove(i);
				genomes.remove(remove);
				removed++;
			}
		}
		//Repopulate genomes.
		for(int i = 0; i < removed; i++)
		{
			ArrayList<Genome> species = all.get(random.nextInt(all.size()));
			Genome genome = species.get(random.nextInt(species.size()));
			
			double type = Math.random();
			Genome newest = null;
			
			if(type > repopTypeThresh)
			{
				Genome other;
				do
				{
					other = species.get(random.nextInt(species.size()));
				}
				while(other == genome);
				newest = genome.crossover(other);
			}
			else
				newest = genome.mutate();

			genomes.add(newest);
		}
		generations++;
	}
}
