package com.alphander.networks.network.activation.activators;

import com.alphander.networks.network.activation.Activator;

public class ELU extends Activator
{
	private float exp = 0.01f;
	public ELU() {}
	
	public ELU(float exp)
	{
		this.exp = exp;
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
		return (float) (in >= 0 ? in : exp * (Math.exp(in) - 1));
	}
	
	@Override
	public float derivative(float in)
	{
		return in >= 0 ? in : activator(in) + exp;
	}
}
