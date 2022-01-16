package com.alphander.networks.environment;

import com.alphander.networks.utils.NetArray;

public abstract class Environment 
{	
	//Render parameters
	protected int screenX, screenY;
	protected String name;
	
	protected int terminal;
    protected float reward;
	
	public abstract void setAction(NetArray action);
	public abstract NetArray getState();
	public abstract void reset();
	
	public int observationSpace, actionSpace;
	
	public float getReward()
	{
		return reward;
	}
	
	public int getDone()
	{
		return terminal;
	}
}
