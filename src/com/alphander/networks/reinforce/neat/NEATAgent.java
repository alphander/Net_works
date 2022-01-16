package com.alphander.networks.reinforce.neat;

import com.alphander.networks.environment.Environment;
import com.alphander.networks.network.neatnet.NEATNet;
import com.alphander.networks.reinforce.Agent;
import com.alphander.networks.utils.NetArray;

public class NEATAgent extends Agent
{
	public float totalReward;
	Environment env;
	NEATNet actor;
	
	public NEATAgent(Environment env, NEATNet actor)
	{
		this.env = env;
		this.actor = actor;
	}
	
	@Override
	public void testEnvironment(int delay)
	{
		NetArray state = env.getState();
		totalReward += env.getReward();
		NetArray probability = actor.run(state).softMax();
		int action = (int) probability.add(new NetArray(env.actionSpace)).sampleDiscrete();
		env.setAction(new NetArray().append(action));
	}

	@Override
	public void learn()
	{
		actor.train(totalReward);
		totalReward = 0f;
	}
}