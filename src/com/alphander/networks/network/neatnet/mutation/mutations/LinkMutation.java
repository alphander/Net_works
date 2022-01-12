package com.alphander.networks.network.neatnet.mutation.mutations;

import java.util.Random;

import com.alphander.networks.network.neatnet.Genome;
import com.alphander.networks.network.neatnet.Link;
import com.alphander.networks.network.neatnet.mutation.Mutation;

public class LinkMutation extends Mutation
{
	public LinkMutation(float probability)
	{
		super(probability);
	}

	@Override
	public void mutate(Genome genome)
	{
		if(Math.random() > probability) return;
		
		Random random = new Random();
		
		int numNodes = genome.numNodes;
		
		int in = random.nextInt(numNodes), out = random.nextInt(numNodes);
		
		while(!genome.links.containsKey(Link.hash(in, out)))
		{
			in = random.nextInt(numNodes);
			out = random.nextInt(numNodes);
		}
		
		Link link = new Link(in, out);
		
		genome.links.put(link.hashCode(), link);
	}
}
