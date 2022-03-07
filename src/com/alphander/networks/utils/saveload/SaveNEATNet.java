package com.alphander.networks.utils.saveload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.alphander.networks.network.neatnet.NEATNet;
import com.alphander.networks.network.neatnet.mutation.Mutation;
import com.alphander.networks.network.neatnet.structure.Genome;
import com.alphander.networks.network.neatnet.structure.Link;
import com.alphander.networks.utils.saveload.parser.Parser;
import com.alphander.networks.utils.saveload.parser.structure.Dict;
import com.alphander.networks.utils.saveload.parser.structure.List;

public class SaveNEATNet
{
	public static void saveNetwork(NEATNet net, String dir)
	{
		Dict d = saveNEATNet(net);
		Parser p = new Parser();
		String data = p.save(d);
		
		Path path = Paths.get(dir);
		Path file = Paths.get(dir, net.getName() + ".json");
		
		try { 
			Files.createDirectories(path);
			Files.writeString(file, data);
		} catch(IOException e) {throw new IllegalArgumentException();}
	}
	
	private static Dict saveNEATNet(NEATNet net)
	{
		Dict d = new Dict();
		
		d.put("name", net.name);
		d.put("input dims", net.inputDims);
		d.put("output dims", net.outputDims);
		d.put("death rate", net.deathRate);
		d.put("population", net.population);
		d.put("repoptype thresh", net.repopTypeThresh);
		d.put("species thresh", net.speciesThresh);
		d.put("activator", net.activator.getName());
		
		List mutations = new List();
		for(Mutation mutation : net.mutations)
			mutations.add(saveMutation(mutation));
		d.put("mutations", mutations);
		
		List genomes = new List();
		for(Genome genome : net.genomes)
			genomes.add(saveGenome(genome));
		d.put("genomes", genomes);
		
		return d;
	}
	
	private static Dict saveGenome(Genome genome)
	{
		Dict d = new Dict();
		d.put("id", "" + genome.hashCode());
		d.put("score", genome.score);
		d.put("nodes", genome.numNodes);
		
		List links = new List();
		for(Link link : genome.links.values())
			links.add(saveLink(link));
		
		d.put("links", links);
		return d;
	}
	
	private static Dict saveMutation(Mutation mutation)
	{
		Dict d = new Dict();
		d.put("mutation", mutation.getName());
		d.put("probability", mutation.probability);
		return d;
	}
	
	private static Dict saveLink(Link link)
	{
		Dict d = new Dict();
		d.put("hash", "" + link.hashCode());
		d.put("weight", link.weight);
		d.put("enabled", link.enabled);
		return d;
	}
}
