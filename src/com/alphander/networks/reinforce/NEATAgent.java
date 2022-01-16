package com.alphander.networks.reinforce;

import com.alphander.networks.environment.Environment;
import com.alphander.networks.network.neatnet.NEATNet;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;

public class NEATAgent extends Agent
{
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
		while(env.getDone() != 0)
		{
			NetArray state = env.getState();
			totalReward += env.getReward();
			NetArray probability = actor.run(state).softMax();
			int action = (int) probability.add(new NetArray(env.actionSpace)).sampleDiscrete();
			env.setAction(new NetArray().append(action));
			
			Util.delay(delay);
		}
		env.reset();
	}

	@Override
	public void learn()
	{
		actor.train(totalReward);
		totalReward = 0f;
	}
}