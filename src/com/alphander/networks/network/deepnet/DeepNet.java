package com.alphander.networks.network.deepnet;

import java.util.Arrays;
import com.alphander.networks.network.Network;
import com.alphander.networks.network.activation.Activator;
import com.alphander.networks.network.activation.activators.Tanh;
import com.alphander.networks.network.loss.LossFunction;
import com.alphander.networks.network.loss.lossfunctions.MSE;
import com.alphander.networks.utils.NetArray;

public class DeepNet implements Network, Cloneable
{
	//Parameters and settings
	public String name = "Network";
	public float stepWeights = 0.033f;
	public float stepBiases = 0.0033f;
	public float weightDecay = 0.000001f;
	public LossFunction lossFunction;
	
	//Network structure
	private int[] size;
	private Layer[] layers;
	
	//Variables
	private float error;
	private float[] errors;
	private float[] output;
	private float[] input;
	
	//Setup and constructors
	public DeepNet(int[] size)//Non specific
	{	
		setup(size, new Tanh(), new MSE());
	}
	
	public DeepNet(int[] size, Activator defaultActivator, LossFunction lossFunction)//Specific
	{	
		setup(size, defaultActivator, lossFunction);
	}
	
	private void setup(int[] size, Activator defaultActivator, LossFunction lossFunction)
	{
		this.size = size.clone();
		this.lossFunction = lossFunction;
		
		if(size.length < 2) throw new IndexOutOfBoundsException();
		
		errors = new float[size[size.length - 1]];
		input = new float[size[0]];
		output = new float[size[size.length - 1]];
		
		layers = new Layer[size.length - 1];
		for(int i = 0; i < size.length - 1; i++)
			layers[i] = new Layer(size[i], size[i + 1], this, defaultActivator);
	}
	@Override
	public NetArray getOutput()
	{
		return new NetArray(output.clone());
	}
	
	@Override
	public NetArray getInput()
	{
		return new NetArray(input.clone());
	}
	
	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public NetArray run(NetArray input)
	{
		if(input.length() != this.input.length) throw new IndexOutOfBoundsException();
		
		Arrays.fill(this.output, 0f);
		this.input = input.array();
		
		forward(this.input);
		
		return getOutput();
	}
	
	public void forward(float[] input)
	{
		layers[0].forward(this.input);
		for(int i = 1; i < layers.length; i++)
			layers[i].forward(layers[i - 1].output);
		
		output = layers[layers.length - 1].output;
	}
	
	@Override
	public void train(NetArray expected)
	{
		clearGradients();
		backward(expected);
		step(1);
	}
	
	public void backward(NetArray expect)
	{
		float[] expected = expect.array();
		
		if(expected.length != this.output.length) throw new IndexOutOfBoundsException();
		
		this.error = lossFunction.loss(output, expected);
		float[] error = lossFunction.lossDerivative(output, expected);
		for(int i = 0; i < errors.length; i++)
			errors[i] = error[i];
		
		layers[layers.length - 1].backward(error);
		for(int i = layers.length - 2; i >= 0; i--)
			layers[i].backward(layers[i + 1].error);
	}
	
	public void step(int batchSize)
	{
		for(int i = 0; i < layers.length; i++)
			layers[i].step(batchSize);
	}
	
	public void clearGradients()
	{
		for(int i = 0; i < layers.length; i++)
			layers[i].clearGradients();
	}
	
	//Getters and Setters
	public void setActivator(int index, Activator activator)
	{
		if(index < 0 || index > getLayers().length) throw new IndexOutOfBoundsException();
		getLayers()[index].activator = activator;
	}
	
	public Activator getActivator(int index)
	{
		if(index < 0 || index > getLayers().length) throw new IndexOutOfBoundsException();
		return getLayers()[index].activator;
	}
	
	public int[] getSize()
	{
		return size.clone();
	}
	
	public Layer[] getLayers()
	{
		return layers;
	}
	
	public float getError()
	{
		return error;
	}
	
	@Override
	public DeepNet clone()
	{
		DeepNet network = new DeepNet(getSize());
		network.name = name;
		
		network.stepWeights = stepWeights;
		network.stepBiases = stepBiases;
		network.weightDecay = weightDecay;
		network.lossFunction = lossFunction;
		
		//Network structure
		Layer[] layers = new Layer[this.layers.length]; 
		for(int i = 0; i < this.layers.length; i++)
			layers[i] = this.layers[i].clone();
		
		network.layers = layers;
		
		//Variables
		network.error = error;
		network.output = output.clone();
		network.input = input.clone();
		
		return this;
	}
}