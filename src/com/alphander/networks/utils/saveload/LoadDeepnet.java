package com.alphander.networks.utils.saveload;

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
		try {
			
			Path path = Paths.get(dir, name + ".json");
			String data = Files.readString(path);
			
			Parser p = new Parser();
			
			Dict d = (Dict) p.load(data);
			
			DeepNet net = loadDeepnet(d);
			activators(net, d);
			weights(net, d);
			biases(net, d);
			return net;
		} catch (Exception e) {return null;}
	}
	
	public static DeepNet loadDeepnet(Dict in) throws Exception
	{
		Dict d = (Dict) in.get("settings");
		String name = ((Leaf) d.get("name")).getString();
		float stepWeights = (float) ((Leaf) d.get("stepWeights")).getNumber();
		float stepBiases = (float) ((Leaf) d.get("stepBiases")).getNumber();
		float weightDecay = (float) ((Leaf) d.get("weightDecay")).getNumber();
		LossFunction lossFunction = FileHelper.getLossFunction(((Leaf)d.get("lossFunction")).getString());
		
		List l = (List) d.get("size");
		int[] size = new int[l.size()];
		for(int i = 0; i < l.size(); i++)
			size[i] = (int) ((Leaf) l.get(i)).getNumber();
		
		DeepNet net = new DeepNet(size);
		net.name = name;
		net.stepWeights = stepWeights;
		net.stepBiases = stepBiases;
		net.weightDecay = weightDecay;
		net.lossFunction = lossFunction;
		
		return net;
	}
	
	public static void activators(DeepNet net, Dict in) throws Exception
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
