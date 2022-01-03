package com.alphander.networks.runtime.supervised;

import com.alphander.networks.network.deepnet.DeepNet;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;

public class BasicDemo
{
	public static void main(String[] args)
	{
		DeepNet net = new DeepNet(new int[] {2, 16, 16, 2});//Easy network setup. Set size with int array.
		
		for(int i = 0; i < 10_000; i++)//Number of training iterations
		{	
			//Random rules that I made of for no specific reason.
			
			net.run(new NetArray(1f, 0f));//Case 1
			net.train(new NetArray(1f, 1f));//Expected
			
			net.run(new NetArray(1f, 1f));//Case 2
			net.train(new NetArray(0.1f, 1f));//Expected
			
			Util.printError(net);
		}
		
		System.out.println(net.run(new NetArray(1f, 0f)).string());//Output of our expected case 1
		System.out.println(net.run(new NetArray(1f, 1f)).string());//Output of our expected case 2
		
		Util.test(net);
	}
}
