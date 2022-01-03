package com.alphander.networks.utils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import com.alphander.networks.network.deepnet.DeepNet;
import com.alphander.networks.network.deepnet.Layer;

class LoadNetwork
{
	public static DeepNet loadNetwork(String dir, String name)
	{
		dir = dir + "\\" + name;
		File file = new File(dir);
		if(!file.exists()) return null;
		
		DeepNet network = loadNetwork(dir);
		applyBiases(network, dir);
		applyWeights(network, dir);
		applyActivators(network, dir);
		
		return network;
	}
	
	public static DeepNet loadNetwork(String dir)
	{
		File file = new File(dir + "\\parameters.txt");
		if(!file.exists()) return null;
		
		Scanner s = FileHelper.getScanner(file);
		
		HashMap<String, String> map = new HashMap<String, String>();
		while(s.hasNextLine())
		{
			String[] line = s.nextLine().split(":");
			
			if(line.length == 2) map.put(line[0], line[1]);
		}	

		int[] size = Arrays.stream(map.get("size").split(",")).mapToInt(Integer::parseInt).toArray();
		DeepNet network = new DeepNet(size);
		
		network.stepWeights = Float.parseFloat(map.get("stepWeights"));
		network.stepBiases = Float.parseFloat(map.get("stepBiases"));
		network.weightDecay = Float.parseFloat(map.get("weightDecay"));
		network.lossFunction = FileHelper.getLossFunction(map.get("lossFunction"));
		
		s.close();
		return network;
	}
	
	public static void applyBiases(DeepNet network, String dir)
	{
		File file = new File(dir + "\\biases.txt");
		if(!file.exists()) return;
		
		Scanner s = FileHelper.getScanner(file);
		
		for(Layer layer : network.getLayers())
			if(s.hasNextLine())
				for(int i = 0; i < layer.output.length; i++)
					layer.biases[i] = Float.parseFloat(s.nextLine());
		
		s.close();	
		return;
	}
	
	public static void applyWeights(DeepNet network, String dir)
	{
		File file = new File(dir + "\\weights.txt");
		if(!file.exists()) return;
		
		Scanner s = FileHelper.getScanner(file);
		
		for(Layer layer : network.getLayers())
			if(s.hasNextLine())
				for(int i = 0; i < layer.weights.length; i++)
					for(int j = 0; j < layer.weights[i].length; j++)
						layer.weights[i][j] = Float.parseFloat(s.nextLine());
		
		s.close();
		return;
	}
	
	public static DeepNet applyActivators(DeepNet network, String dir)
	{
		File file = new File(dir + "\\activators.txt");
		if(!file.exists()) return network;
		
		Scanner s = FileHelper.getScanner(file);
		for(Layer layer : network.getLayers())
			if(s.hasNextLine())
				layer.activator = FileHelper.getActivation(s.nextLine());
		
		s.close();			
		return network;
	}
}
