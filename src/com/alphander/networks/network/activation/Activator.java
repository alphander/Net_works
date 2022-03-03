package com.alphander.networks.network.activation;

public abstract class Activator 
{	
	public abstract float[] activator(float[] in);
	public abstract float[] derivative(float[] in);
	public abstract float activator(float in);
	public abstract float derivative(float in);

	public String getName()
	{
		return this.getClass().getTypeName();
	}
	
	public String getPackage()
	{
		return this.getClass().getPackageName();
	}
}
