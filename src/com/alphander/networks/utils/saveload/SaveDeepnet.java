package com.alphander.networks.utils.saveload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.alphander.networks.network.deepnet.DeepNet;
import com.alphander.networks.network.deepnet.Layer;
import com.alphander.networks.utils.saveload.parser.Parser;
import com.alphander.networks.utils.saveload.parser.structure.Dict;
import com.alphander.networks.utils.saveload.parser.structure.List;

public class SaveDeepnet
{	
	public static void saveNetwork(DeepNet net, String dir)
	{	
		Dict dict = new Dict();
		dict.put("settings", saveDeepnet(net));
		dict.put("activators", activators(net));
		dict.put("weights", weights(net));
		dict.put("biases", biases(net));
		
		Parser p = new Parser();
		String data = p.save(dict);
		
		Path path = Paths.get(dir);
		Path file = Paths.get(dir, net.getName() + ".json");
		
		try { 
			Files.createDirectories(path); 
			Files.writeString(file, data);
		} catch(IOException e) {throw new IllegalArgumentException();}
	}
	
	private static Dict saveDeepnet(DeepNet net)
	{
		Dict d = new Dict();
		d.put("name", net.name);
		d.put("weightDecay", net.weightDecay);
		d.put("stepWeights", net.stepWeights);
		d.put("stepBiases", net.stepBiases);
		d.put("lossFunction", net.lossFunction.getName());
		
		List list = new List();
		for(int size : net.getSize())
			list.add(size);
		d.put("size", list);
		
		return d;
	}
	
	private static List activators(DeepNet net)
	{	
		List l = new List();
		for(Layer layer : net.getLayers())
			l.add(layer.activator.getName());
		return l;
	}
	
	private static List weights(DeepNet net)
	{	
		List l = new List();
		for(Layer layer : net.getLayers())
			for(int i = 0; i < layer.weights.length; i++)
				for(int j = 0; j < layer.weights[i].length; j++)
					l.add(layer.weights[i][j]);
		return l;
	}
	
	private static List biases(DeepNet net)
	{		
		List l = new List();
		for(Layer layer : net.getLayers())
			for(float biases : layer.biases)
				l.add(biases);
		return l;
	}
}
