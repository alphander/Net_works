package com.alphander.networks.runtime.unsupervised.pg;

import java.awt.Color;

import com.alphander.networks.environment.environments.Pendulum.Pendulum;
import com.alphander.networks.network.activation.activators.Linear;
import com.alphander.networks.network.activation.activators.ReLU;
import com.alphander.networks.network.activation.activators.SoftMax;
import com.alphander.networks.network.activation.activators.Swish;
import com.alphander.networks.network.activation.activators.Tanh;
import com.alphander.networks.network.deepnet.DeepNet;
import com.alphander.networks.network.loss.lossfunctions.NoLoss;
import com.alphander.networks.reinforce.pg.PGAgent;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Display.NetworkGraph;

public class PGPendulumDemo 
{
	//This uses the policy gradient theorem.
	public static void main(String[] args) 
	{	
		Pendulum env = new Pendulum();

		DeepNet actor = new DeepNet(new int[] {2, 32, 64, 32, 16, 16, 2});
		actor.stepWeights = 0.0033f;
		actor.stepBiases = 0.00033f;
		actor.lossFunction = new NoLoss();

		actor.setActivator(0, new Linear());
		actor.setActivator(1, new Swish());
		actor.setActivator(2, new Tanh());
		actor.setActivator(3, new Swish());
		actor.setActivator(4, new Tanh());
		actor.setActivator(5, new SoftMax());

		PGAgent agent = new PGAgent(env, actor, 5, 0.92f, 0.1f);

		NetworkGraph graph = new NetworkGraph("Reward", Color.RED, 0);

		env.render();

		int iter = 100;
		NetArray totalReward = new NetArray();
		float latestReward = 0f;
		for(int i = 0; i < iter; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				agent.testEnvironment(0);
				latestReward = agent.totalReward;
				totalReward.append(agent.totalReward);
				
				if(totalReward.length() > 10)
				{
					graph.addData(totalReward.mean());
					totalReward.remove(0);
				}
			}
			
			agent.learn();
			System.out.println(i + " ---------------------------------------------------------- " + latestReward);
		}
		
		while(true)
		{
			agent.testEnvironment(20);
			agent.clearAll();
		}
	}
}
