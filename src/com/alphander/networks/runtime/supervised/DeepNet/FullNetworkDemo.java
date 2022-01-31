package com.alphander.networks.runtime.supervised.DeepNet;

import java.awt.Color;

import com.alphander.networks.network.activation.activators.ReLU;
import com.alphander.networks.network.activation.activators.Tanh;
import com.alphander.networks.network.deepnet.DeepNet;
import com.alphander.networks.network.loss.lossfunctions.MSE;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;
import com.alphander.networks.utils.Display.NetworkGraph;

public class FullNetworkDemo
{	
	public static void main(String[] args)
	{
		int iterations = 10_000;
		
		//Network Setup. These are values I found that work well.
		DeepNet net = new DeepNet(new int[] {2, 16, 16, 2});
		net.name = "FullNetwork";
		net.weightDecay = 0.00001f;
		net.stepWeights = 0.033f;
		net.stepBiases = 0.0033f;
		net.lossFunction = new MSE();
			
		net.setActivator(0, new Tanh());
		net.setActivator(1, new ReLU());
		net.setActivator(2, new Tanh());
		
		
		net = Util.load(net, "FullNetwork");//Will return new network. If none, will return null.
		
		Util.hookSave(net);//Saves network if program shuts down
		
		NetworkGraph graph = new NetworkGraph("Error", Color.BLUE, 0);//Setting up graph
		
		//Training iterations
		for(int i = 0; i < iterations; i++)
		{	
			net.run(new NetArray(0f, 0f));
			net.train(new NetArray(0f, 0f));
			
			net.run(new NetArray(0f, 1f));
			net.train(new NetArray(0f, 1f));
			
			net.run(new NetArray(1f, 0f));
			net.train(new NetArray(0f, 1f));
			
			net.run(new NetArray(1f, 1f));
			net.train(new NetArray(1f, 0f));
			
			Util.printError(net);
			graph.addData(net.getError());
		}
		Util.print("Final Error: [" + net.getError() + "]");
	
		Util.save(net);//Saving network!
		
		//Testing the network
		Util.test(net);
	}
}