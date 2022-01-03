package com.alphander.networks.network.loss.lossfunctions;

import com.alphander.networks.network.loss.LossFunction;

public class NoLoss extends LossFunction
{

	@Override
	public float loss(float[] output, float[] expected)
	{
		float loss = 0f;
		for(int i = 0; i < output.length; i++)
			loss += expected[i];
		return loss;
	}

	@Override
	public float[] lossDerivative(float[] output, float[] expected)
	{
		return expected;
	}
	
}
