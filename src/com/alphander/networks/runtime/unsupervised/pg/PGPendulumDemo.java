package com.alphander.networks.runtime.unsupervised.pg;

import java.awt.Color;

import com.alphander.networks.environment.environments.pendulum.Pendulum;
import com.alphander.networks.network.activation.activators.Linear;
import com.alphander.networks.network.activation.activators.SoftMax;
import com.alphander.networks.network.activation.activators.Swish;
import com.alphander.networks.network.activation.activators.Tanh;
import com.alphander.networks.network.deepnet.DeepNet;
import com.alphander.networks.network.loss.lossfunctions.NoLoss;
import com.alphander.networks.reinforce.agents.PGAgent;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;
import com.alphander.networks.utils.display.NetworkGraph;

public class PGPendulumDemo 
{	
	//This uses the policy gradient theorem.
	public static void main(String[] args) 
	{	
		float rewardThresh = 1000f;
		int iterations = 10000;
		
		Pendulum env = new Pendulum();

		DeepNet actor = new DeepNet(new int[] {env.observationSpace, 32, 64, 32, 16, 16, env.actionSpace});
		actor.name = "PGPendulum";
		actor.weightDecay = 0.000001f;
		actor.stepWeights = 0.00033f;
		actor.stepBiases = 0.00033f;
		actor.lossFunction = new NoLoss();

		actor.setActivator(0, new Linear());
		actor.setActivator(1, new Swish());
		actor.setActivator(2, new Tanh());
		actor.setActivator(3, new Swish());
		actor.setActivator(4, new Tanh());
		actor.setActivator(5, new SoftMax());

		PGAgent agent = new PGAgent(env, actor, 5, 0.92f, 0.3f);

		NetworkGraph graph = new NetworkGraph("Reward", Color.RED, 0);
		NetArray totalReward = new NetArray();
		float latestReward = 0f;

		env.render();

		for(int i = 0; i < iterations && latestReward < rewardThresh; i++)
		{
			agent.run();
			latestReward = agent.totalReward;
			totalReward.append(agent.totalReward);
			
			if(totalReward.length() > 10)
			{
				graph.addData(totalReward.mean());
				totalReward.remove(0);
			}
			
			if(i % 4 == 0) agent.learn();
			Util.print(i + " ---------------------------------------------------------- " + latestReward);
		}
		
		while(true)
		{
			agent.run(20);
			agent.clear();
		}
	}
}
