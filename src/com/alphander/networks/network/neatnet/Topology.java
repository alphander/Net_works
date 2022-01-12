package com.alphander.networks.network.neatnet;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import com.alphander.networks.network.activation.Activator;

public class Topology
{
	public Genome genome;
	public int[][] network;//Contains references to input nodes.
	public float[][] weights;//Contains references to input weights.
	public float[] values;//Contains values of each node.
	public int[] order;//Contains order that each node needs to run.
	public Activator activator;
	public int inputDims, outputDims;
	
	public float score = 0f;
	
	Topology(Genome genome, Activator activator)
	{	
		this.genome = genome;
		this.inputDims = genome.inputDims;
		this.outputDims = genome.outputDims;
		this.activator = activator;
		//build(genome);
	}
	
	public float[] forward(float[] in)
	{
		Arrays.fill(values, 0.0f);
		for(int node : order)
			for(int input : network[node])
			{
				float weight = weights[node][input];
				float value = values[input];
				values[node] += activator.activator(value * weight);
			}
		float[] output = new float[outputDims];
		for(int i = 0; i < outputDims; i++)
			output[i] = values[i + inputDims];
		
		return output;
	}
	
	@SuppressWarnings("unchecked")
	public void build(Genome genome)//My special algorithm for compiling a neural network from the genome instructions.
	{
		int numNodes = genome.numNodes; //how many nodes there are in the genome.
		HashMap<Integer, Link> links = genome.links; //reference to all links in genome.
		HashSet<Integer>[] adjacent = new HashSet[numNodes]; //Store all adjacent nodes for every node. Nodes are stored as integers.
        for (int i = 0; i < adjacent.length; i++) //Fill array.
        	adjacent[i] = new HashSet<Integer>();
        
        for (Link link : links.values()) //Calculate adjacent nodes.
        {
        	adjacent[link.a].add(link.b);
        	adjacent[link.b].add(link.a);
        }
        
        HashSet<Integer>[] inputs = new HashSet[numNodes];
        for (int i = 0; i < inputs.length; i++) //Fill array.
        	inputs[i] = new HashSet<Integer>();
        
        ArrayDeque<Integer> next = new ArrayDeque<Integer>(); //Smart way of determining who's next to be checked.
        int[] nodes = new int[numNodes];
        int[] order = new int[numNodes];
        int index = numNodes - 1;
        
        for(int i = inputDims; i < outputDims; i++) next.add(i);//Adding outputs first.
        
        while(!next.isEmpty())//Explore and evaluate distance.
		{
			int node = next.peekFirst();
			order[index] = node;
			
			int d = nodes[node] + 1;
			for(int n : adjacent[node])
				if(nodes[n] <= d && !inputs[n].contains(node))
				{
					nodes[n] = d;
					inputs[node].add(n);
					next.offerLast(n);
				}
			next.pollFirst();
		}
        
        int[][] network = new int[numNodes][];
        float[][] weights = new float[numNodes][];
        
        for(int i = 0; i < inputs.length; i++)//Build fast network
        {
        	int size = inputs[i].size();
        	network[i] = new int[size];
        	weights[i] = new float[size];
        	int j = 0;
        	for(int node : inputs[i])
	        {
	        	network[i][j] = node;
	        	weights[i][j] = links.get(Link.hash(i, j)).weight;
	        	j++;
	        }
        }
        
        this.network = network;
        this.weights = weights;
        this.values = new float[numNodes];
	}
}