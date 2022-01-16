package com.alphander.networks.runtime.unsupervised.neat;

import java.awt.Color;

import com.alphander.networks.environment.environments.cartpole.Cartpole;
import com.alphander.networks.network.neatnet.NEATNet;
import com.alphander.networks.reinforce.NEATAgent;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;
import com.alphander.networks.utils.Display.NetworkGraph;

public class NEATCartpoleDemo
{
	public static void main(String[] args) 
	{	
		float rewardThresh = 300f;
		int iterations = 10000;
		
		//Actor/Environment setup
		Cartpole env = new Cartpole();
		NEATNet actor = new NEATNet(env.observationSpace, env.actionSpace);
		
		//Reinforcement learning agent setup
		NEATAgent agent = new NEATAgent(env, actor);
		
		NetworkGraph graph = new NetworkGraph("Reward", Color.RED, 0);
		NetArray totalReward = new NetArray();
		float latestReward = 0f;
		
		env.render();
		for(int i = 0; i < iterations && latestReward < rewardThresh; i++)
		{
			agent.testEnvironment();
			latestReward = agent.totalReward;
			totalReward.append(agent.totalReward);
			
			if(totalReward.length() > 100)
			{
				graph.addData(totalReward.mean());
				totalReward.remove(0);
			}
			
			agent.learn();
			
			Util.print(i + " ---------------------------------------------------------- " + latestReward);
		}
		
		NetworkGraph runGraph = new NetworkGraph("Running", Color.BLUE, 0);
		
		while(true)
		{
			agent.testEnvironment(20);
			latestReward = agent.totalReward;
			runGraph.addData(latestReward);
		}
	}
}
