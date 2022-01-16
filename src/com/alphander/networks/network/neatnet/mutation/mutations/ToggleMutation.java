package com.alphander.networks.network.neatnet.mutation.mutations;

import com.alphander.networks.network.neatnet.mutation.Mutation;
import com.alphander.networks.network.neatnet.structure.Genome;
import com.alphander.networks.network.neatnet.structure.Link;

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
