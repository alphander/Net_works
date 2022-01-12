package com.alphander.networks.reinforce;

public abstract class Agent
{
	public abstract void testEnvironment(int delay);
	public abstract void learn();
	
	public void testEnvironment()
	{
		this.testEnvironment(0);
	}
}
