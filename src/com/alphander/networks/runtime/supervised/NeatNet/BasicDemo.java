package com.alphander.networks.runtime.supervised.NeatNet;

import com.alphander.networks.network.neatnet.NEATNet;

import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;

public class BasicDemo
{
	public static void main(String[] args)
	{
		NEATNet neat = new NEATNet(2, 2);
		
		for(int i = 0; i < 1; i++)
		{	
			NetArray array = neat.run(new NetArray(Math.random(), Math.random()));
			
			Util.print(array.string());
		}
	}
}
