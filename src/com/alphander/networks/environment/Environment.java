package com.alphander.networks.environment;

import com.alphander.networks.utils.NetArray;

public abstract class Environment 
{	
	protected int terminal = 1;
    protected float reward = 0.0f;
	
	public abstract void setAction(NetArray action);
	public abstract NetArray getState();
	public abstract void reset();
	
	public int observationSpace = 0;
	public int actionSpace = 0;
	
	public float getReward()
	{
		return reward;
	}
	
	public int getDone()
	{
		return terminal;
	}
}
