package com.alphander.networks.network.neatnet.mutation.mutations;

import com.alphander.networks.network.neatnet.Genome;
import com.alphander.networks.network.neatnet.Link;
import com.alphander.networks.network.neatnet.mutation.Mutation;

public class ToggleMutation extends Mutation
{
	public ToggleMutation(float probability)
	{
		super(probability);
	}

	@Override
	public void mutate(Genome genome)
	{	
		if(Math.random() > probability) return;
		
		Link link = genome.randomLink();
		if(link == null) return;
		
		link.enabled = !link.enabled;
	}
}
