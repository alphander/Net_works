package com.alphander.networks.utils.saveload;

import java.util.ArrayList;
import java.util.Formatter;

import com.alphander.networks.network.neatnet.NEATNet;
import com.alphander.networks.network.neatnet.structure.Genome;
import com.alphander.networks.network.neatnet.structure.Link;

public class SaveNEATNet
{
	public static void saveNetwork(NEATNet net, String dir)
	{
		saveNEATNet(net, dir + "\\");
		ArrayList<Genome> genomes = net.genomes;
		for(int i = 0; i < net.genomes.size(); i++)
		{
			Genome genome = genomes.get(i);
			saveGenome(genome, dir + "\\genomes\\genome" + i + ".txt");
		}
	}
	
	private static void saveNEATNet(NEATNet net, String dir)
	{
		Formatter f = FileHelper.getFormatter(dir);
		
		f.format("name : " + net.name);
		f.format("population : " + net.population);
		f.format("speciesThresh : " + net.speciesThresh);
		f.format("deathRate : " + net.deathRate);
		f.format("repopTypeThresh : " + net.repopTypeThresh);
		
		f.close();
	}
	
	private static void saveGenome(Genome genome, String dir)
	{
		Formatter f = FileHelper.getFormatter(dir);
		
		for(Link link : genome.links.values())
			f.format("" + link.hashCode());
		
		f.close();
	}
}
