package com.alphander.networks.network.activation.activators;

import com.alphander.networks.network.activation.Activator;

public class SoftMax extends Activator
{
	@Override
	public float[] activator(float[] in)
	{
		float[] out = new float[in.length];
		
		float sum = 0;
		for(int i = 0; i < in.length; i++)
			sum += Math.exp(in[i]);
		
		for(int i = 0; i < in.length; i++)
			out[i] = (float) (Math.exp(in[i]) / sum);
		
		return out;
	}
	@Override
	public float[] derivative(float[] in)
	{
		float[] out = new float[in.length];
		for(int i = 0; i < in.length; i++)
			for(int j = 0; j < out.length; j++)
				if(i == j)
					out[j] += in[i] * (1 - in[i]);
				else
					out[j] += -in[i] * in[j];
		return out;
	}
	
	@Override
	public float derivative(float in)
	{
		throw new UnsupportedOperationException();
	}
	@Override
	public float activator(float in)
	{
		throw new UnsupportedOperationException();
	}
}
