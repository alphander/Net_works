package com.alphander.networks.runtime.unsupervised.pg;

import java.awt.Color;

import com.alphander.networks.environment.environments.cartpole.Cartpole;
import com.alphander.networks.network.activation.activators.ReLU;
import com.alphander.networks.network.activation.activators.SoftMax;
import com.alphander.networks.network.activation.activators.Tanh;
import com.alphander.networks.network.deepnet.DeepNet;
import com.alphander.networks.network.loss.lossfunctions.NoLoss;
import com.alphander.networks.reinforce.PGAgent;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;
import com.alphander.networks.utils.Display.NetworkGraph;

public class PGCartpoleDemo 
{
	//This uses the policy gradient theorem.
	public static void main(String[] args) 
	{	
		float rewardThresh = 400f;
		int iterations = 10000;
		
		//Environment setup
		Cartpole env = new Cartpole();
		
		//Actor setup
		DeepNet actor = new DeepNet(new int[] {env.observationSpace, 64, 64, 16, env.actionSpace});
		actor.name = "Actor";
		actor.weightDecay = 0.00001f;
		actor.stepWeights = 0.0033f;
		actor.stepBiases = 0.0033f;
		actor.lossFunction = new NoLoss();
		
		actor.setActivator(0, new ReLU());
		actor.setActivator(1, new Tanh());
		actor.setActivator(2, new SoftMax());
		
		//Reinforcement learning agent setup
		PGAgent agent = new PGAgent(env, actor, 10, 0.94f, 0.1f);
		
		NetworkGraph graph = new NetworkGraph("Reward", Color.RED, 0);
		NetArray totalReward = new NetArray();
		float latestReward = 0f;
		
		env.render();
		
		for(int i = 0; i < iterations && latestReward < rewardThresh; i++)
		{
			agent.testEnvironment();
			latestReward = agent.totalReward;
			totalReward.append(agent.totalReward);
				
			if(totalReward.length() > 0)
			{
					graph.addData(totalReward.mean());
					totalReward.remove(0);
			}
			
			if(i % 10 == 0) agent.learn();
			Util.print(i + " ---------------------------------------------------------- " + latestReward);
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
