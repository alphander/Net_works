package com.alphander.networks.runtime.supervised.DeepNet;

import java.awt.Color;
import java.util.Arrays;

import com.alphander.networks.environment.Environment;
import com.alphander.networks.environment.environments.pendulum.Pendulum;
import com.alphander.networks.network.deepnet.DeepNet;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;
import com.alphander.networks.utils.Display.NetworkGraph;

public class PredictionDemo
{
	public static void main(String[] args)
	{
		//Setting up network!
				
		DeepNet net = new DeepNet(new int[] {2, 32, 32, 16, 2});//Util.loadNetwork(set);//Will return new network if none is found with the same name.
		net.name = "PendulumTester";
		net.weightDecay = 0.0f;
		net.stepWeights = 0.033f;
		net.stepBiases = 0.0033f;
		
		Util.hookSave(net);//Saves network if program shuts down.
		
		//Settings up environment!
		Environment pendulum = new Pendulum();
		
		NetArray previous = pendulum.getState();
		
		//Setting up graph
		NetworkGraph errorGraph = new NetworkGraph("Error", Color.GREEN, 10000);
		
		//Training the network.
		int iter = 300_000;
		
		for(int i = 0; i < iter; i++)
		{	
			pendulum.setAction(null);
			NetArray observation = pendulum.getState();
			
			net.run(previous);
			net.train(observation);
			
			previous = observation;
			
			errorGraph.addData(net.getError());
			
			Util.printError(net);
			
		}
		System.out.println("Final Error: [" + net.getError() + "]");
		
		Util.save(net);//Saving network
		
		//Running and comparing prediction!
		compare(net);
		
	}
	
	private static void compare(DeepNet net)
	{
		Environment env = new Pendulum();
		
		NetArray prediction = new NetArray(1.0f, 0.0f);
		
		NetworkGraph pendulumGraphReal = new NetworkGraph("Observation", Color.RED, 1000);
		NetworkGraph pendulumGraphPre = new NetworkGraph("Prediction", Color.BLUE, 1000);
		
		while(true)
		{
			NetArray observation = env.getState();
			System.out.println("Truth: " + Arrays.toString(observation.array()));
			
			env.setAction(null);
			System.out.println("Predict: " + Arrays.toString(prediction.array()));
			prediction = net.run(prediction);
			
			pendulumGraphReal.addData(observation.get(0));
			pendulumGraphPre.addData(prediction.get(0));
			
			Util.delay(100);
		}
	}
}
