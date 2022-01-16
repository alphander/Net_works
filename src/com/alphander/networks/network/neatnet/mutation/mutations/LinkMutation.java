package com.alphander.networks.network.neatnet.mutation.mutations;

import java.util.Random;

import com.alphander.networks.network.neatnet.mutation.Mutation;
import com.alphander.networks.network.neatnet.structure.Genome;
import com.alphander.networks.network.neatnet.structure.Link;

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
		
		int in = 0, out = 0;
		do
		{
			in = random.nextInt(numNodes);
			out = random.nextInt(numNodes);
		}
		while(!genome.canLink(in, out));
		
		Link link = new Link(in, out);
		
		genome.links.put(link.hashCode(), link);
	}
}
