package com.alphander.networks.runtime.supervised.DeepNet;

import java.awt.Color;
import com.alphander.networks.environment.environments.pendulum.Pendulum;
import com.alphander.networks.network.deepnet.DeepNet;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;
import com.alphander.networks.utils.display.NetworkGraph;

public class PredictionDemo
{
	public static void main(String[] args)
	{
		int iterations = 10_000;
		
		//Setting up network!
		DeepNet net = new DeepNet(new int[] {2, 32, 32, 16, 2});//Util.loadNetwork(net);//Will return new network if none is found with the same name.
		net.name = "PendulumTester";
		net.weightDecay = 0.00001f;
		net.stepWeights = 0.033f;
		net.stepBiases = 0.0033f;
		
		//Settings up environment!
		Pendulum env = new Pendulum("Pendulum", 90.0f, 0.0f);
		env.airResistance = 0.0f;
		env.dt = 0.1f;
		env.stepSize = 1;
		env.thetaScale = 90f;
		env.velScale = 4f;
		
		//Setting up graph
		NetworkGraph errorGraph = new NetworkGraph("Error", Color.GREEN, 10000);
		
		//Training the network.
		NetArray previous = env.getState();
		for(int i = 0; i < iterations; i++)
		{	
			env.setAction(null);
			NetArray observation = env.getState();
			
			net.run(previous);
			net.train(observation);
			
			previous = observation;
			
			errorGraph.addData(net.getError());
			
			Util.printError(net);
		}
		Util.print("Final Error: [" + net.getError() + "]");
		
		//Running and comparing prediction!
		NetArray prediction = new NetArray(1.0f, 0.0f);
		
		NetworkGraph pendulumGraph = new NetworkGraph("Observation", Color.RED, 1000);
		NetworkGraph pendulumGraphPre = new NetworkGraph("Prediction", Color.BLUE, 1000);
		
		while(true)
		{
			NetArray observation = env.getState();
			Util.print("Truth: " + observation.string());
			
			env.setAction(null);
			Util.print("Predict: " + prediction.string());
			prediction = net.run(prediction);
			
			pendulumGraph.addData(observation.get(0));
			pendulumGraphPre.addData(prediction.get(0));
			
			Util.delay(100);
		}
		
	}
}
