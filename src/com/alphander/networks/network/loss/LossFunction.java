package com.alphander.networks.network.loss;

public abstract class LossFunction
{	
	public static final String lossfunctionFile = "com.alphander.networks.network.loss.lossfunctions";
	
	public abstract float loss(float[] output, float[] expected);
	public abstract float[] lossDerivative(float[] output, float[] expected);

	public String getName()
	{
		return this.getClass().getSimpleName();
	}
}
