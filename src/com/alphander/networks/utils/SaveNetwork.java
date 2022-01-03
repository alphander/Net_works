package com.alphander.networks.utils;

import java.io.File;
import java.util.Formatter;

import com.alphander.networks.network.deepnet.DeepNet;
import com.alphander.networks.network.deepnet.Layer;

class SaveNetwork
{
	public static void saveNetwork(DeepNet network, String dir)
	{	
		dir = dir + "\\" + network.name;
		File file = new File(dir);
		file.mkdir();
		
		saveBiases(network, dir);
		saveWeights(network, dir);
		saveActivators(network, dir);
		saveSettings(network, dir);
	}
	
	private static void saveBiases(DeepNet network, String dir)
	{		
		Formatter f = FileHelper.getFormatter(dir + "\\biases.txt");
		
		for(Layer layer : network.getLayers())
			for(float biases : layer.biases)
				f.format(biases + "\n");
		
		f.close();
	}
	
	private static void saveWeights(DeepNet network, String dir)
	{	
		Formatter f = FileHelper.getFormatter(dir + "\\weights.txt");
	 
		for(Layer layer : network.getLayers())
			for(int i = 0; i < layer.weights.length; i++)
				for(int j = 0; j < layer.weights[i].length; j++)
					f.format(layer.weights[i][j] + "\n");
		
		f.close();
	}
	
	private static void saveActivators(DeepNet network, String dir)
	{	
		Formatter f = FileHelper.getFormatter(dir + "\\activators.txt");
		
		for(Layer layer : network.getLayers())
			f.format(layer.activator.getName() + "\n");
		
		f.close();
	}
	
	private static void saveSettings(DeepNet network, String dir)
	{
		Formatter f = FileHelper.getFormatter(dir + "\\settings.txt");
		
		f.format("weightDecay:" + network.weightDecay + "\n");
		f.format("stepWeights:" + network.stepWeights + "\n");
		f.format("stepBiases:" + network.stepBiases + "\n");
		f.format("lossFunction:" + network.lossFunction.getName() + "\n");
		f.format("size:" + FileHelper.arrayToString(network.getSize()));
		
		f.close();
	}
}
