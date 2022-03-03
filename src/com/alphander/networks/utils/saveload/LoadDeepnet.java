package com.alphander.networks.utils.saveload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.alphander.networks.network.deepnet.DeepNet;
import com.alphander.networks.network.deepnet.Layer;
import com.alphander.networks.network.loss.LossFunction;
import com.alphander.networks.utils.saveload.parser.Parser;
import com.alphander.networks.utils.saveload.parser.structure.Dict;
import com.alphander.networks.utils.saveload.parser.structure.Leaf;
import com.alphander.networks.utils.saveload.parser.structure.List;

public class LoadDeepnet
{
	public static DeepNet loadNetwork(String dir, String name)
	{
		Path path = Paths.get(dir, name + ".json");
		String data;
		try {
			data = Files.readString(path);
		} catch (IOException e) { return null;}
		Parser p = new Parser();
		Dict d = (Dict) p.load(data);
		
		DeepNet network = settings(d);
		activators(network, d);
		weights(network, d);
		biases(network, d);
		
		return network;
	}
	
	public static DeepNet settings(Dict in)
	{
		Dict d = (Dict) in.get("settings");
		float stepWeights = (float) ((Leaf)d.get("stepWeights")).getNumber();
		float stepBiases =  (float) ((Leaf)d.get("stepBiases")).getNumber();
		float weightDecay =  (float) ((Leaf)d.get("weightDecay")).getNumber();
		LossFunction lossFunction = FileHelper.getLossFunction(((Leaf)d.get("lossFunction")).get());
		
		List l = (List) d.get("size");
		int[] size = new int[l.size()];
		for(int i = 0; i < l.size(); i++)
			size[i] = (int) ((Leaf)l.get(i)).getNumber();
		
		DeepNet network = new DeepNet(size);
		network.stepWeights = stepWeights;
		network.stepBiases = stepBiases;
		network.weightDecay = weightDecay;
		network.lossFunction = lossFunction;
		
		return network;
	}
	
	public static void activators(DeepNet net, Dict in)
	{
		List l = (List) in.get("activators");
		Layer[] layers = net.getLayers();
		for(int i = 0; i < layers.length; i++)
			layers[i].activator = FileHelper.getActivation(((Leaf) l.get(i)).getString());		
	}
	
	public static void weights(DeepNet net, Dict in)
	{		
		List l = (List) in.get("weights");
		Layer[] layers = net.getLayers();
		for(Layer layer : layers)
			for(int i = 0; i < layer.weights.length; i++)
				for(int j = 0; j < layer.weights[i].length; j++)
					layer.weights[i][j] = (float)((Leaf) l.get(i)).getNumber();
	}
	
	public static void biases(DeepNet net, Dict in)
	{
		List l = (List) in.get("biases");
		Layer[] layers = net.getLayers();
		for(Layer layer : layers)
			for(int i = 0; i < layer.output.length; i++)
				layer.biases[i] = (float)((Leaf) l.get(i)).getNumber();
	}
}
