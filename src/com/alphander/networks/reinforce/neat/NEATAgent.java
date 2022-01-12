package com.alphander.networks.reinforce.neat;

import com.alphander.networks.environment.Environment;
import com.alphander.networks.network.neatnet.NEATNet;
import com.alphander.networks.reinforce.Agent;

public class NEATAgent extends Agent
{
	public float totalReward;
	Environment env;
	NEATNet net;
	
	public NEATAgent(Environment env, NEATNet net)
	{
		this.env = env;
		this.net = net;
	}
	
	@Override
	public void testEnvironment(int delay)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void learn()
	{
		// TODO Auto-generated method stub
		
	}
	
	public void clearAll()
	{
		
	}
}