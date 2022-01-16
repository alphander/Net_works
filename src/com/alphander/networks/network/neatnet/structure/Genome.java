package com.alphander.networks.network.neatnet.structure;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import com.alphander.networks.network.activation.Activator;
import com.alphander.networks.network.neatnet.mutation.Mutation;

public class Genome implements Comparable<Genome>
{
	public int numNodes = 0;
	public int inputDims, outputDims;
	public HashMap<Integer, Link> links;
	public Mutation[] mutations;
	public Activator activator;
	public Topology topo;
	private Random random = new Random();
	
	public float score = 0;
	
	public Genome(Activator activator, Mutation[] mutations, int inputDims, int outputDims)
	{
		this.links = new HashMap<Integer, Link>();
		this.mutations = mutations;
		this.inputDims = inputDims;
		this.outputDims = outputDims;
		this.numNodes = inputDims + outputDims;
		this.activator = activator;
		this.topo = new Topology(this, activator);
	}
	
	private Genome(Activator activator, Mutation[] mutations, HashMap<Integer, Link> links, int numNodes, int inputDims, int outputDims)
	{
		this.links = links;
		this.mutations = mutations;
		this.inputDims = inputDims;
		this.outputDims = outputDims;
		this.numNodes = numNodes;
		this.activator = activator;
		this.topo = new Topology(this, activator);
	}
	
	public Link randomLink()
	{
		Collection<Link> set = links.values();
		
		if(set.size() <= 0) return null;
		
		int random = this.random.nextInt(set.size());
		
		int index = 0;
		for(Link link : set)
		{
			if(index == random)
				return link;
			index++;
		}
		return null;
	}
	
	public float heuristic(Genome other)
	{	
		final float small = 0.00001f;
		int unionCount = 0;
		float weightDif = small;
		for(Link link : links.values())
		{
			if(!other.links.containsKey(link.hashCode())) continue;
			
			weightDif += Math.abs(link.weight - other.links.get(link.hashCode()).weight);
			
			unionCount++;
		}
		float difCount = (links.size() - unionCount) + (other.links.size() - unionCount) + small;
		
		float W = weightDif / (unionCount + small);
		float N = Math.max(links.size(), other.links.size()) + small;
		
		float h = difCount / N + W;
		return h;
	}
	
	@SuppressWarnings("unchecked")
	public Genome crossover(Genome parent)
	{
		HashMap<Integer, Link> links1 = (HashMap<Integer, Link>) this.links.clone();
		HashMap<Integer, Link> links2 = (HashMap<Integer, Link>) parent.links.clone();
		
		HashMap<Integer, Link> genome = new HashMap<Integer, Link>();
		
		for(Link link : links.values())
		{
			if(!links2.containsKey(link.hashCode())) continue;
			
			Link choice = random.nextInt(1) == 0 ? link : links2.get(link.hashCode());
				
			links1.remove(link.hashCode());
			links2.remove(link.hashCode());
			
			genome.put(choice.hashCode(), choice);
		}
		genome.putAll(links1);
		genome.putAll(links2);
		
		int numNodes = Math.max(this.numNodes, parent.numNodes);
		
		return new Genome(activator, mutations, genome, numNodes, inputDims, outputDims);
	}
	
	@SuppressWarnings("unchecked")
	public Genome mutate()
	{
		HashMap<Integer, Link> links = (HashMap<Integer, Link>) this.links.clone();
		Genome genome = new Genome(activator, mutations, links, numNodes, inputDims, outputDims);
		
		for(Mutation mutation : mutations)
			mutation.mutate(genome);
		
		return genome;
	}
	
	public boolean canLink(int a, int b)
	{
		boolean contains = links.containsKey(Link.hash(a, b));
		
		boolean same = a == b;
		
		boolean bothInInput = a < inputDims && b < inputDims;
		
		boolean bothInOutput = a > inputDims-1 && a < outputDims && b > inputDims-1 && b < outputDims;
		
		return !(contains || same || bothInInput || bothInOutput);
	}

	@Override
	public int compareTo(Genome genome)
	{
		if(this.score > genome.score)
			return 1;
		if(this.score < genome.score)
			return -1;
		return 0;
	}
}
