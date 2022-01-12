package com.alphander.networks.network.neatnet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import com.alphander.networks.network.neatnet.mutation.Mutation;

public class Genome
{
	public int numNodes = 0;
	public int inputDims, outputDims;
	public HashMap<Integer, Link> links;
	public Mutation[] mutations;
	private Random random = new Random();
	
	Genome(Mutation[] mutations, int inputDims, int outputDims)
	{
		this.links = new HashMap<Integer, Link>();
		this.mutations = mutations;
		this.inputDims = inputDims;
		this.outputDims = outputDims;
		this.numNodes = inputDims + outputDims;
			
		mutate();
	}
	
	Genome(Mutation[] mutations, HashMap<Integer, Link> links)
	{
		this.links = links;
		this.mutations = mutations;
		mutate();
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
		int unionCount = 0;
		float weightDif = 0f;
		for(Link link : links.values())
		{
			if(!other.links.containsValue(link)) continue;
			
			weightDif += Math.abs(link.weight - other.links.get(link.hashCode()).weight);
			
			unionCount++;
		}
		int difCount = (links.size() - unionCount) + (other.links.size() - unionCount);
		
		float W = weightDif / unionCount;
		float N = Math.max(links.size(), other.links.size());
		
		return difCount / N + W;
	}
	
	@SuppressWarnings("unchecked")
	public Genome crossover(Genome parent)
	{
		HashMap<Integer, Link> links1 = (HashMap<Integer, Link>) links.clone();
		HashMap<Integer, Link> links2 = (HashMap<Integer, Link>) parent.links.clone();
		
		HashMap<Integer, Link> genome = new HashMap<Integer, Link>();
		
		for(Link link : links1.values())
		{
			if(!links2.containsValue(link)) continue;
			
			Link choice = random.nextInt(1) == 0 ? link : links2.get(link.hashCode());
				
			links1.remove(link.hashCode());
			links2.remove(link.hashCode());
			
			genome.put(choice.hashCode(), choice);
		}
		genome.putAll(links1);
		genome.putAll(links2);
		
		return new Genome(mutations, genome);
	}
	
	public void mutate()
	{
		for(Mutation mutation : mutations)
			mutation.mutate(this);
	}
}
