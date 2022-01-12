package com.alphander.networks.runtime.unsupervised.neat;

import java.awt.Color;

import com.alphander.networks.environment.environments.cartpole.Cartpole;
import com.alphander.networks.network.activation.activators.ReLU;
import com.alphander.networks.network.activation.activators.Sigmoid;
import com.alphander.networks.network.activation.activators.SoftMax;
import com.alphander.networks.network.activation.activators.Tanh;
import com.alphander.networks.network.deepnet.DeepNet;
import com.alphander.networks.network.loss.lossfunctions.NoLoss;
import com.alphander.networks.network.neatnet.NEATNet;
import com.alphander.networks.reinforce.neat.NEATAgent;
import com.alphander.networks.reinforce.pg.PGAgent;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Display.NetworkGraph;

public class NEATCartpoleDemo
{
	public static void main(String[] args) 
	{	
		//Environment setup
		Cartpole env = new Cartpole();
		
		//Actor setup
		
		NEATNet actor = new NEATNet(env.observationSpace, env.actionSpace);
		actor.activator = new Tanh();
		
		//Reinforcement learning agent setup
		NEATAgent agent = new NEATAgent(env, actor);
		
		NetworkGraph graph = new NetworkGraph("Reward", Color.RED, 0);
		
		env.render();
		
		int iter = 100;
		NetArray totalReward = new NetArray();
		float latestReward = 0f;
		for(int i = 0; i < iter && latestReward < 200f; i++)
		{
			agent.learn();
			System.out.println(i + " ---------------------------------------------------------- " + latestReward);
		}
		
		NetworkGraph runGraph = new NetworkGraph("Running", Color.BLUE, 0);
		
		while(true)
		{
			agent.testEnvironment(20);
			latestReward = agent.totalReward;
			runGraph.addData(latestReward);
			agent.clearAll();
		}
	}
}
