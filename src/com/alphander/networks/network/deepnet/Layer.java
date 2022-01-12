package com.alphander.networks.network.deepnet;

import java.util.Arrays;

import com.alphander.networks.network.activation.Activator;

public class Layer
{
	public DeepNet network;
	public Activator activator;
	
	public int inSize;
	public int outSize;
	public float[] input;
	public float[] output;
	
	public float[][] weights;
	public float[][] weightsGrad;
	public float[] biases;
	public float[] biasesGrad;
	
	public float[] error;
	
	Layer(int inSize, int outSize, DeepNet network, Activator activator)
	{
		this.network = network;
		this.activator = activator;
		
		this.inSize = inSize;
		this.outSize = outSize;
		input = new float[inSize];
		output = new float[outSize];
		
		weights = new float[inSize][outSize];
		weightsGrad = new float[inSize][outSize];
		biases = new float[outSize];
		biasesGrad = new float[outSize];
		
		error =  new float[inSize];
		
		for(int i = 0; i < inSize; i++)
			for(int j = 0; j < outSize; j++)
				weights[i][j] = (float) (Math.random() - 0.5);
	}
	
	float[] forward(float[] input)
	{
		this.input = input;
		for (int i = 0; i < outSize; i++)
		{
			output[i] = 0;
			for (int j = 0; j < inSize; j++)
				output[i] += input[j] * weights[j][i] + biases[i];
		}
		
		output = activator.activator(output);

		return output;
	}
	
	void clear()
	{
		Arrays.fill(input, 0f);
		Arrays.fill(output, 0f);
	}
	
	void backward(float[] frontError)
	{
		Arrays.fill(error, 0f);
		
		float[] derived = activator.derivative(output);
		for(int i = 0; i < output.length; i++)
			frontError[i] *= derived[i];
		
		for(int i = 0; i < outSize; i++)
		{
			biasesGrad[i] += frontError[i];
            for (int j = 0; j < inSize; j++)
            {
            	error[j] += weights[j][i] * frontError[i];
            	weightsGrad[j][i] += input[j] * frontError[i];
            }
		}
	}
	
	void step(int batchSize)
	{
		for(int i = 0; i < outSize; i++)
		{
			biases[i] -= biasesGrad[i] * (network.stepBiases / batchSize);
            for (int j = 0; j < inSize; j++)
            {
            	weights[j][i] -= weightsGrad[j][i] * (network.stepWeights / batchSize);
            	weights[j][i] *= 1 - network.weightDecay;
            }
		}
	}
	
	void clearGradients()
	{
		Arrays.fill(biasesGrad, 0f);
		for(int j = 0; j < inSize; j++)
            Arrays.fill(weightsGrad[j], 0f);
	}

	@Override
	protected Layer clone()
	{
		Layer layer = new Layer(inSize, outSize, network, activator);
		layer.input = input.clone();
		layer.output = output.clone();
		
		layer.weights = weights.clone();
		layer.weightsGrad = weightsGrad.clone();
		layer.biases = biases.clone();
		layer.biasesGrad = biasesGrad.clone();
		
		layer.error = error.clone();
		
		return layer;
	}
}
