package com.alphander.networks.runtime.supervised.NeatNet;

import com.alphander.networks.network.neatnet.NEATNet;

import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;

public class BasicDemo
{
	public static void main(String[] args)
	{
		NEATNet net = new NEATNet(2, 2);
		
		for(int i = 0; i < 20000; i++)
		{	
			NetArray array = net.run(new NetArray(1.0f, 1.0f));
			
			net.train(new NetArray(0.0f, 1.0f));
			
			Util.print(array.string());
		}
		
		Util.test(net);
	}
}
