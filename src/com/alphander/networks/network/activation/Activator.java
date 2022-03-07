package com.alphander.networks.network.activation;

public abstract class Activator 
{	
	public static final String activatorFile = "com.alphander.networks.network.activation.activators";
	
	public abstract float[] activator(float[] in);
	public abstract float[] derivative(float[] in);
	public abstract float activator(float in);
	public abstract float derivative(float in);

	public String getName()
	{
		return this.getClass().getSimpleName();
	}
}
