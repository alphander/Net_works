package com.alphander.networks.network.activation.activators;

import com.alphander.networks.network.activation.Activator;

public class Swish extends Activator
{	
	private float swish = 0.1f;
	public Swish() {}
	
	public Swish(float swish) 
	{
		this.swish = swish;
	}
	
	@Override
	public float[] activator(float[] in)
	{
		float[] out = new float[in.length];
		for(int i = 0; i < in.length; i++)
			out[i] = activator(in[i]);
		return out;
	}
	@Override
	public float[] derivative(float[] in)
	{
		float[] out = new float[in.length];
		for(int i = 0; i < in.length; i++)
			out[i] = derivative(in[i]);
		return out;
	}
	
	@Override
	public float activator(float in)
	{
		return (float) (in / (1 + Math.exp(-swish * in)));
	}
	
	@Override
	public float derivative(float in)
	{
		return (float) (Math.exp(swish) + 1);
	}
}
