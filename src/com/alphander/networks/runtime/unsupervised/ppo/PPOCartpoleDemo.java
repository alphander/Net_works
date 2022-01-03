package com.alphander.networks.runtime.unsupervised.ppo;

import java.awt.Color;

import com.alphander.networks.environment.environments.Cartpole;
import com.alphander.networks.network.activation.activators.LeakyReLU;
import com.alphander.networks.network.activation.activators.Linear;
import com.alphander.networks.network.activation.activators.ReLU;
import com.alphander.networks.network.activation.activators.Sigmoid;
import com.alphander.networks.network.activation.activators.SoftMax;
import com.alphander.networks.network.activation.activators.Swish;
import com.alphander.networks.network.activation.activators.Tanh;
import com.alphander.networks.network.deepnet.DeepNet;
import com.alphander.networks.network.loss.lossfunctions.NoLoss;
import com.alphander.networks.reinforce.pg.PGAgent;
import com.alphander.networks.reinforce.ppo.PPOAgent;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Display.NetworkGraph;

public class PPOCartpoleDemo 
{
	//This uses the policy gradient theorem.
	public static void main(String[] args) 
	{	
		//Environment setup
		Cartpole env = new Cartpole();

		//Actor setup

		DeepNet actor = new DeepNet(new int[] {4, 32, 16, 2});
		actor.name = "Actor";
		actor.weightDecay = 0.0f;
		actor.stepWeights = 0.00033f;
		actor.stepBiases = 0.00033f;
		actor.lossFunction = new NoLoss();

		actor.setActivator(0, new ReLU());
		actor.setActivator(1, new Sigmoid());
		actor.setActivator(2, new SoftMax());
		
		DeepNet critic = new DeepNet(new int[] {4, 32, 16, 1});
		actor.name = "Critic";
		actor.weightDecay = 0.0f;
		actor.stepWeights = 0.00033f;
		actor.stepBiases = 0.00033f;
		actor.lossFunction = new NoLoss();

		actor.setActivator(0, new ReLU());
		actor.setActivator(1, new ReLU());

		//Reinforcement learning agent setup
		PPOAgent agent = new PPOAgent(env, actor, critic, 5, 0.96f, 0.2f);

		NetworkGraph graph = new NetworkGraph("Reward", Color.RED, 0);

		env.render();

		int iter = 100;
		NetArray totalReward = new NetArray();
		float latestReward = 0f;
		for(int i = 0; i < iter && latestReward < 250f; i++)
		{
			for(int j = 0; j < 2; j++)
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

