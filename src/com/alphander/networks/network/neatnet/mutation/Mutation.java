package com.alphander.networks.network.neatnet.mutation;

import com.alphander.networks.network.neatnet.Genome;

public abstract class Mutation
{
	protected float probability;
	
	public abstract void mutate(Genome genome);
	
	public Mutation(float probability)
	{
		this.probability = probability;
	}
}
