package com.alphander.networks.network.neatnet.structure;

public class Link
{
	public int a, b = 0;
	public float weight = 0f;
	public boolean enabled = true;
	
	private int hash = 0;
	
	public Link(int a, int b)
	{
		this.a = a;
		this.b = b;
		weight = (float) (Math.random() * 2f - 1f);
		hash = hash(a, b);
	}
	
	@Override
	public int hashCode()
	{
		return hash;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Link))
			return false;
		if(o == this)
			return true;
		if(o.hashCode() == hashCode())
			return true;
		return false;	
	}
	
	public static int hash(int a, int b)
	{
		return (Math.min(a, b) << 16) + Math.max(a, b);
	}
}
