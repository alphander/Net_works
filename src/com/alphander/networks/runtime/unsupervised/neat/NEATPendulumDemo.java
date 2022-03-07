package com.alphander.networks.runtime.unsupervised.neat;

import java.awt.Color;

import com.alphander.networks.environment.environments.pendulum.Pendulum;
import com.alphander.networks.network.neatnet.NEATNet;
import com.alphander.networks.reinforce.agents.NEATAgent;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;
import com.alphander.networks.utils.display.NetworkGraph;

public class NEATPendulumDemo
{
	public static void main(String[] args) 
	{	
		float rewardThresh = 2000f;
		int iterations = 10000;
		
		//Actor/Environment setup
		Pendulum env = new Pendulum();
		env.thetaScale = 90f;
		env.velScale = 4f;
		NEATNet actor = new NEATNet(env.observationSpace, env.actionSpace);
		actor.name = "NEATPendulum";
		
		//Reinforcement learning agent setup
		NEATAgent agent = new NEATAgent(env, actor);
		
		NetworkGraph graph = new NetworkGraph("Reward", Color.RED, 0);
		NetArray totalReward = new NetArray();
		float latestReward = 0f;
		
		env.render();
		for(int i = 0; i < iterations && latestReward < rewardThresh; i++)
		{
			agent.run();
			latestReward = agent.totalReward;
			totalReward.append(agent.totalReward);
			
			if(totalReward.length() > 0)
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
			agent.run(20);
			latestReward = agent.totalReward;
			runGraph.addData(latestReward);
		}
	}
}
