package com.alphander.networks.network.activation.activators;

import com.alphander.networks.network.activation.Activator;

public class ReLU extends Activator
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
		return in >= 0 ? in : 0;
	}
	
	@Override
	public float derivative(float in)
	{
		return in >= 0 ? 1 : 0;
	}
}
