package com.alphander.networks.utils.saveload;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import com.alphander.networks.network.activation.Activator;
import com.alphander.networks.network.neatnet.NEATNet;
import com.alphander.networks.network.neatnet.mutation.Mutation;
import com.alphander.networks.network.neatnet.structure.Genome;
import com.alphander.networks.network.neatnet.structure.Link;
import com.alphander.networks.utils.saveload.parser.Parser;
import com.alphander.networks.utils.saveload.parser.structure.Dict;
import com.alphander.networks.utils.saveload.parser.structure.Leaf;
import com.alphander.networks.utils.saveload.parser.structure.List;
import com.alphander.networks.utils.saveload.parser.structure.Part;

public class LoadNEATNet
{
	public static NEATNet loadNetwork(String dir, String name)
	{
		try {
			
			Path path = Paths.get(dir, name + ".json");
			String data = Files.readString(path);
			
			Parser p = new Parser();
			Dict d = (Dict) p.load(data);
			
			return loadNEATNet(d);
			
		} catch (Exception e) {	return null;}
	}
	
	private static NEATNet loadNEATNet(Dict d) throws Exception
	{
		String name = ((Leaf) d.get("name")).getString();
		int inputDims = (int) ((Leaf) d.get("input dims")).getNumber();
		int outputDims = (int) ((Leaf) d.get("output dims")).getNumber();
		float deathRate = (float) ((Leaf) d.get("death rate")).getNumber();
		int population = (int) ((Leaf) d.get("population")).getNumber();
		float repopTypeThresh = (float) ((Leaf) d.get("repoptype thresh")).getNumber();
		float speciesThresh = (float) ((Leaf) d.get("species thresh")).getNumber();
		Activator activator = FileHelper.getActivation(((Leaf) d.get("activator")).getString());
		int index = (int) ((Leaf) d.get("index")).getNumber();
		
		List m = (List) d.get("mutations");
		Mutation[] mutations = new Mutation[m.size()];
		for(int i = 0; i < m.size(); i++)
			mutations[i] = loadMutation((Dict) m.get(i));
		
		List g = (List) d.get("genomes");
		ArrayList<Genome> genomes = new ArrayList<Genome>();
		for(Part genome : g)
			genomes.add(loadGenome(activator, mutations, inputDims, outputDims, (Dict) genome));
		
		NEATNet net = new NEATNet(name, inputDims, outputDims, deathRate, population, 
				repopTypeThresh, speciesThresh, activator, mutations, genomes, index);

		return net;
	}
	
	private static Genome loadGenome(Activator activator, Mutation[] mutations, int inputDims, int outputDims, Dict dict)
	{
		int numNodes = (int) ((Leaf)dict.get("nodes")).getNumber();
		float score = (float) ((Leaf)dict.get("score")).getNumber();
		HashMap<Integer, Link> links = loadLinks((List) dict.get("links"));
		
		return new Genome(activator, mutations, links, numNodes, score, inputDims, outputDims);
	}
	
	private static Mutation loadMutation(Dict dict) throws Exception
	{
		String mutation = ((Leaf)dict.get("mutation")).getString();
		float probability = (float) ((Leaf)dict.get("probability")).getNumber();
		
		return FileHelper.getMutation(mutation, probability);
	}

	private static HashMap<Integer, Link> loadLinks(List list)
	{
		HashMap<Integer, Link> map = new HashMap<Integer, Link>();
		for(Part p : list)
		{
			if(!(p instanceof Dict))
				return map;
			Dict d = (Dict) p;
			int hash = Integer.parseInt(((Leaf) d.get("hash")).getString());
			float weight = (float) ((Leaf) d.get("weight")).getNumber();
			boolean enabled = (boolean) ((Leaf) d.get("enabled")).getBoolean();
			Link link = new Link(hash, weight, enabled);

			map.put(hash, link);
		}
		return map;
	}
}
