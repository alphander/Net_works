package com.alphander.networks.reinforce;

public abstract class Agent
{
	public float totalReward;
	
	public abstract void run(int delay);
	public abstract void learn();
	public abstract void clear();
	
	public void run()
	{
		this.run(0);
	}
}
