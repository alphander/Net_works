package com.alphander.networks.network.loss;

public abstract class LossFunction
{	
	public abstract float loss(float[] output, float[] expected);
	public abstract float[] lossDerivative(float[] output, float[] expected);

	public String getName()
	{
		return this.getClass().getName();
	}
}
