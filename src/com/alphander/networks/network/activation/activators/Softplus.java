package com.alphander.networks.network.activation.activators;

import com.alphander.networks.network.activation.Activator;

public class Softplus extends Activator
{
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
		return (float) Math.log(1 + Math.exp(in));
	}
	
	@Override
	public float derivative(float in)
	{
		float ex = (float) Math.exp(in);
		return ex / (ex + 1);
	}
}
