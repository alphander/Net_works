package com.alphander.networks.utils;

public class MathHelper
{
	public static double lerp(double a, double b, double t)
	{
		return t * (b - a) + a;
	}
	
	public static double[] lerp(double[] a, double[] b, double t)
	{
		int length = sameLength(a, b);
		
		double[] out = new double[length];
		for(int i = 0; i < length; i++)
			out[i] = lerp(a[i], b[i], t);
		
		return out;
	}
	
	public static int sameLength(double[] a, double[] b)
	{
		if(a.length != b.length) throw new IndexOutOfBoundsException();
		
		return a.length;
	}
}
