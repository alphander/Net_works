package com.alphander.networks.network.activation.activators;

import com.alphander.networks.network.activation.Activator;

public class LeakyReLU extends Activator
{
	private float leak = 0.01f;
	public LeakyReLU() {}
	
	public LeakyReLU(float leak)
	{
		this.leak = leak;
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
		return (float) Math.max(leak * in, in);
	}
	
	@Override
	public float derivative(float in)
	{
		return in >= 0 ? in : leak;
	}
}
