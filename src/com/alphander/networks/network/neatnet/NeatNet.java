package com.alphander.networks.network.neatnet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.alphander.networks.network.Network;
import com.alphander.networks.network.activation.Activator;
import com.alphander.networks.network.activation.activators.Tanh;
import com.alphander.networks.network.neatnet.mutation.Mutation;
import com.alphander.networks.network.neatnet.mutation.mutations.WeightRandomMutation;
import com.alphander.networks.network.neatnet.mutation.mutations.WeightShiftMutation;
import com.alphander.networks.network.neatnet.mutation.mutations.ToggleMutation;
import com.alphander.networks.network.neatnet.mutation.mutations.NodeMutation;
import com.alphander.networks.network.neatnet.mutation.mutations.LinkMutation;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;

public class NEATNet implements Network
{
	public int populationSize = 50;
	int inputDims, outputDims;
	float[] inputs, outputs;
	public Activator activator = new Tanh();
	public Mutation[] mutations = new Mutation[] {
		new LinkMutation(1f),
		new NodeMutation(1f),
		new WeightRandomMutation(1f),
		new WeightShiftMutation(1f),
		new ToggleMutation(0f),
	};
	
	HashSet<Genome> genomes = new HashSet<Genome>();
	ArrayList<Topology> instances = new ArrayList<Topology>();
	
	int current = 0;
	
	public NEATNet(int inputDims, int outputDims)
	{
		if(inputDims < 1 || outputDims < 1) throw new IllegalArgumentException();
		
		this.inputDims = inputDims;
		this.outputDims = outputDims;
		
		Genome genome = new Genome(mutations, inputDims, outputDims);
		for(int i = 0; i < 2; i++)
			genome.mutate();
		Topology topo = new Topology(genome, activator);
		instances.add(topo);
	}
	
	@Override
	public NetArray run(NetArray in)
	{
		if(in.length() != inputDims) throw new ArrayIndexOutOfBoundsException();
		
		float[] output = instances.get(current).forward(in.array());
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
	
	public void train(float[] in)
	{
		if(in.length != outputDims) return;
		
		float error = 0f;
		for(int i = 0; i < in.length; i++)
		{
			float d = in[i] * outputs[i];
			error += d * d;
		}
		
		train(-error);
	}
	
	public void train(float score)
	{
		Topology instance = instances.get(current);
		instance.score = score;
		
		current = (current + 1) % instances.size();
		
		if(current != 0) return;
	}
}
