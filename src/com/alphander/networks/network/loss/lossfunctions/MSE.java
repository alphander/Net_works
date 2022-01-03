package com.alphander.networks.network.loss.lossfunctions;

import com.alphander.networks.network.loss.LossFunction;

public class MSE extends LossFunction
{
	@Override
	public float loss(float[] output, float[] expected)
	{
		float loss = 0f;
		for(int i = 0; i < output.length; i++)
		{
			float dif = (output[i] - expected[i]);
			loss += (dif * dif) / expected.length;
		}
		return loss;
	}
	
	@Override
	public float[] lossDerivative(float[] output, float[] expected)
	{
		float[] out = new float[output.length];
		for(int i = 0; i < output.length; i++)
			out[i] = (2 * (output[i] - expected[i])) / expected.length;
		return out;
	}
}
