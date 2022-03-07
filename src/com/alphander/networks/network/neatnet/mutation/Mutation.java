package com.alphander.networks.network.neatnet.mutation;

import com.alphander.networks.network.neatnet.structure.Genome;

public abstract class Mutation
{
	public static final String mutationFile = "com.alphander.networks.network.neatnet.mutation.mutations";
	
	public float probability;
	
	public abstract void mutate(Genome genome);
	
	public Mutation(float probability)
	{
		this.probability = probability;
	}
	
	public String getName()
	{
		return this.getClass().getSimpleName();
	}
}
