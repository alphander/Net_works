package com.alphander.networks.network.neatnet.mutation.mutations;

import com.alphander.networks.network.neatnet.mutation.Mutation;
import com.alphander.networks.network.neatnet.structure.Genome;
import com.alphander.networks.network.neatnet.structure.Link;

public class NodeMutation extends Mutation
{	
	public NodeMutation(float probability)
	{
		super(probability);
	}

	@Override
	public void mutate(Genome genome)
	{	
		if(Math.random() > probability) return;
		
		Link link = genome.randomLink();
		if(link == null) return;
		
		int in = link.a, out = link.b;
		
		genome.links.remove(link.hashCode());
		
		int newNode = genome.numNodes;
		
		Link a = new Link(in, newNode), b = new Link(newNode, out);
		b.weight = link.weight;
		
		genome.links.put(a.hashCode(), a);
		genome.links.put(b.hashCode(), b);
		
		genome.numNodes++;
	}
}
