package com.alphander.networks.runtime.supervised.NeatNet;

import com.alphander.networks.network.neatnet.NEATNet;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;

public class BasicDemo
{
	public static void main(String[] args)
	{
		NEATNet neat = new NEATNet(2, 1);
		
		for(int i = 0; i < 10_000; i++)
		{	
			//NetArray array = neat.run(new NetArray(1f, 0f));
			
			//Util.print(array.string());
		}
	}
}
