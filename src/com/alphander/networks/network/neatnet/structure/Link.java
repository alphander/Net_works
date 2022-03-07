package com.alphander.networks.network.neatnet.structure;

public final class Link
{
	public int a, b;
	public float weight;
	public boolean enabled = true;
	
	private int hash = 0;
	
	public Link(int a, int b)
	{
		this.a = a;
		this.b = b;
		this.weight = (float) (Math.random() * 2f - 1f);
		this.hash = getHash(a, b);
	}
	
	public Link(int hash, float weight, boolean enabled)
	{
		this.a = getA(hash);
		this.b = getB(hash);
		this.weight = weight;
		this.enabled = enabled;
		this.hash = hash;
	}
	
	public static int getA(int hash)
	{
		return hash & 0x0000ffff;
	}
	
	public static int getB(int hash)
	{
		return hash >> 16;
	}
	
	public static int getHash(int a, int b)
	{
		return (Math.min(a, b) << 16) + Math.max(a, b);
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
}
