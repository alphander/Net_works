package com.alphander.networks.utils;

import java.util.Arrays;
import java.util.Scanner;

import com.alphander.networks.network.Network;
import com.alphander.networks.network.deepnet.DeepNet;

public class Util
{
	public static void save(DeepNet net)
	{
		System.out.println("...Saving network...");
		SaveDeepnet.saveNetwork(net, "SavedNetworks");
	}
	public static void save(DeepNet net, String dir)
	{
		System.out.println("...Saving network...");
		SaveDeepnet.saveNetwork(net, dir);
	}
	
	public static DeepNet load(DeepNet backup)
	{
		System.out.println("...Loading network...");
		DeepNet net =  LoadDeepnet.loadNetwork("SavedNetworks", backup.name);
		
		if(net == null) return backup;
		
		return net;
	}
	public static DeepNet load(DeepNet backup, String dir)
	{
		System.out.println("...Loading network...");
		DeepNet net =  LoadDeepnet.loadNetwork(dir, backup.name);
		
		if(net == null) return backup;
		
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
			System.out.println("" + Arrays.toString(net.run(new NetArray(in)).array()));
		}
		else
			System.out.println("Wrong size! Input size of " + net.getInput().length() + " only!");
		
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
