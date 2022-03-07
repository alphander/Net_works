package com.alphander.networks.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

import com.alphander.networks.network.Network;
import com.alphander.networks.network.deepnet.DeepNet;
import com.alphander.networks.network.neatnet.NEATNet;
import com.alphander.networks.utils.saveload.LoadDeepnet;
import com.alphander.networks.utils.saveload.SaveDeepnet;
import com.alphander.networks.utils.saveload.SaveNEATNet;
import com.alphander.networks.utils.saveload.LoadNEATNet;

public class Util
{
	public static final String defaultSave = "Saves";
	public static final String defaultNeat = "NEAT Networks";
	public static final String defaultDeep = "Deep Networks";

	public static void save(Network net)
	{
		if(net instanceof DeepNet)
			save(net, defaultSave + File.separator + defaultDeep);
		if(net instanceof NEATNet)
			save(net, defaultSave + File.separator + defaultNeat);
	}
	
	public static void save(Network net, String dir)
	{
		Util.print("...Saving network...");
		if(net instanceof DeepNet)
			SaveDeepnet.saveNetwork((DeepNet) net, dir);
		if(net instanceof NEATNet)
			SaveNEATNet.saveNetwork((NEATNet) net, dir);
	}
	
	public static DeepNet load(DeepNet backup)
	{
		return load(backup, defaultSave + File.separator + defaultDeep);
	}
	
	public static DeepNet load(DeepNet backup, String dir)
	{
		Util.print("...Loading network...");
		DeepNet net = LoadDeepnet.loadNetwork(dir, backup.getName());
		
		if(net == null) 
		{
			Util.print("Failed loading network!");
			Util.print("...Using backup...");
			return backup;
		}
		return net;
	}
	
	public static NEATNet load(NEATNet backup)
	{
		return load(backup, defaultSave + File.separator + defaultNeat);
	}
	
	public static NEATNet load(NEATNet backup, String dir)
	{
		Util.print("...Loading network...");
		NEATNet net = LoadNEATNet.loadNetwork(dir, backup.getName());
		
		if(net == null) 
		{
			Util.print("Failed loading network!");
			Util.print("...Using backup...");
			return backup;
		}
		return net;
	}
	
	@SuppressWarnings("resource")
	public static void test(Network net)
	{
		Scanner s = new Scanner(System.in);
		
		String[] str = s.next().split(",");
		
		if(str.length == net.getInput().length())
		{
			float[] in = new float[str.length];
			for(int i = 0; i < in.length; i++)
				in[i] = Float.parseFloat(str[i]);
			Util.print("" + Arrays.toString(net.run(new NetArray(in)).array()));
		}
		else
			Util.print("Wrong size! Input size of " + net.getInput().length() + " only!");
		
		test(net);
	}
	
	public static void hookSave(DeepNet net)
	{
		Runtime.getRuntime().addShutdownHook(new Thread(() -> Util.save(net)));
	}
	public static void hookSave(DeepNet net, String dir)
	{
		Runtime.getRuntime().addShutdownHook(new Thread(() -> Util.save(net, dir)));
	}
	
	public static void delay(int delay)
	{
		if(delay == 0) return;
		
		try
		{
			Thread.sleep(delay);
		} 
		catch (InterruptedException e)
		{
			System.out.print("Can't sleep");
		}
	}
	
	public static void printError(DeepNet net)
	{
		String error = "Error: " + net.getError();
		System.out.println(error);
	}
	
	public static void print(String...in)
	{
		String out = "";
		for(int i = 0; i< in.length; i++) out = out + in[i] + " ";
		System.out.println(out);
	}
}
