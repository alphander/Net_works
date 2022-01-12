package com.alphander.networks.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class NetArray implements Iterable<Float>, Cloneable
{
	private ArrayList<Float> array;
	
	public NetArray(ArrayList<Float> array)
	{
		this.array = array;
	}
	
	public NetArray(HashSet<Float> array)
	{
		this.array = new ArrayList<Float>(array);
	}
	
	public NetArray(float[] array)
	{
		this.array = new ArrayList<Float>();
		for(int i = 0; i < array.length; i++)
			this.array.add(array[i]);
	}
	
	public NetArray(int size)
	{
		this.array = new ArrayList<Float>();
		for(int i = 0; i < size; i++)
			this.array.add(0f);
	}
	
	public NetArray(Float...array)
	{
		this.array = new ArrayList<Float>();
		for(int i = 0; i < array.length; i++)
			this.array.add(array[i]);
	}
	
	public NetArray(Double...array)
	{
		this.array = new ArrayList<Float>();
		for(int i = 0; i < array.length; i++)
			this.array.add(array[i].floatValue());
	}
	
	public NetArray()
	{
		this.array = new ArrayList<Float>();
	}
	
	private void check(NetArray array)
	{
		if(length() != array.length())
			throw new IndexOutOfBoundsException();
	}
	
	public NetArray sub(float val)
	{
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
			out[i] = get(i) - val;
		
		return new NetArray(out);
	}
	
	public NetArray add(float val)
	{
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
			out[i] = get(i) + val;
		
		return new NetArray(out);
	}
	
	public NetArray mul(float val)
	{
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
			out[i] = get(i) * val;
		
		return new NetArray(out);
	}
	
	public NetArray div(float val)
	{
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
			out[i] = get(i) / val;
		
		return new NetArray(out);
	}
	
	public NetArray pow(float in)
	{
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
			out[i] = (float) Math.pow(get(i), in);
		
		return new NetArray(out);
	}
	
	public NetArray sub(NetArray array)
	{
		check(array);
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
			out[i] = get(i) - array.get(i);
		
		return new NetArray(out);
	}
	
	public NetArray add(NetArray array)
	{
		check(array);
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
			out[i] = get(i) + array.get(i);
		
		return new NetArray(out);
	}
	
	public NetArray mul(NetArray array)
	{
		check(array);
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
			out[i] = get(i) * array.get(i);
		
		return new NetArray(out);
	}
	
	public NetArray div(NetArray array)
	{
		check(array);
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
			out[i] = get(i) / array.get(i);
		
		return new NetArray(out);
	}
	
	public NetArray pow(NetArray array)
	{
		check(array);
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
			out[i] = (float) Math.pow(get(i), array.get(i));
		
		return new NetArray(out);
	}
	
	public NetArray log()
	{
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
			out[i] = (float) Math.log(get(i));
		
		return new NetArray(out);
	}
	
	public NetArray exp()
	{
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
			out[i] = (float) Math.exp(get(i));
		
		return new NetArray(out);
	}
	
	public NetArray[] split(int segment)
	{
		if(segment > length()) throw new ArrayIndexOutOfBoundsException();
		
		ArrayList<NetArray> list = new ArrayList<NetArray>();
		NetArray current = null;
		for(int i = 0; i < length(); i++)
		{
			if(i % segment == 0) 
			{
				current = new NetArray();
				list.add(current);
			}
			current.append(i);
		}
		NetArray[] out = new NetArray[list.size()]; 
		for(int i = 0; i < out.length; i++) out[i] = list.get(i);
		
		return out;
	}
	
	public float sum()
	{
		float sum = 0;
		for(int i = 0; i < length(); i++) sum += get(i);
		
		return sum;
	}
	
	public float mean()
	{	
		return sum() / length();
	}
	
	public float variance()
	{
		float sum = 0;
		float mean = mean();
		for(int i = 0; i < length(); i++)
		{
			float a = get(i) - mean;
			sum += a * a;
		}
		
		return sum / length();
	}
	
	public float standardDeviation()
	{
		return (float) Math.sqrt(variance());
	}
	
	public NetArray bernoulli(NetArray array)
	{
		check(array);
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
		{
			float p = get(i);
			if(array.get(i) > 0.5f)
				out[i] = p;
			else
				out[i] = p - 1;
		}
		return new NetArray(out);
	}
	
	public int sampleDiscrete()
	{
		float random = (float) Math.random();
		float sum = 0;
		int i;
		for(i = 0; sum < random && i < length(); i++)
			sum += get(i);
		return i - 1;
	}
	
	public NetArray sample()
	{
		float[] samples = new float[array.size()];
		for(int i = 0; i < length(); i++)
			samples[i] = get(i) < Math.random() ? 0f : 1f;
		
		return new NetArray(samples);
	}
	
	public NetArray clip(float min, float max)
	{
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
		{
			float val = get(i);
			if(val > max)
				out[i] = max;
			else if(val < min)
				out[i] = min;
			else
				out[i] = val;
		}
		return new NetArray(out);
	}
	
	public NetArray min(NetArray in)
	{
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
		{
			float val = get(i);
			float min = in.get(i);
			if(val > min)
				out[i] = min;
			else
				out[i] = val;
		}
		return new NetArray(out);
	}
	
	public NetArray max(NetArray in)
	{
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
		{
			float val = get(i);
			float max = in.get(i);
			if(val < max)
				out[i] = max;
			else
				out[i] = val;
		}
		return new NetArray(out);
	}
	
	public NetArray softMax()
	{
		float[] out = new float[length()];
		
		float sum = 0;
		for(int i = 0; i < length(); i++)
			sum += Math.exp(get(i));
		
		for(int i = 0; i < length(); i++)
			out[i] = (float) (Math.exp(get(i)) / sum);
		
		return new NetArray(out);
	}
	
	public NetArray noise(float strength)
	{
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
			out[i] = (float) (Math.random() * strength);
		return new NetArray(out);
	}
	
	public NetArray gaussianNoise(float strength)
	{
		Random rand = new Random();
		float[] out = new float[length()];
		for(int i = 0; i < length(); i++)
			out[i] = (float) (rand.nextGaussian() * strength);
		return new NetArray(out);
	}
	
	public NetArray sumZero()
	{
		return this.sub(this.mean());
	}
	
	public NetArray discounted(float gamma, NetArray dones)
	{	
		float[] discounted = new float[length()];
		
		for(int i = 0; i < length(); i++)
		{
			float Gt = 0;
			float y = 1;
			for(int j = i; j < length(); j++)
			{
				Gt += y * get(j) * dones.get(j);
				y *= gamma;
			}
			discounted[i] = Gt;
		}
		return new NetArray(discounted);
	}
	
	public NetArray computeAdvantage(NetArray dones, NetArray values, float gamma, float lambda)
	{
		float[] advantage = new float[length()];
		for(int t = 0; t < length() - 1; t++)
		{
			float discount = 1f;
			float At = 0f;
			for(int k = t; k < length() - 1; k++)
			{
				At += discount * (get(k) + gamma * values.get(k + 1) * (1 - dones.get(k)) - values.get(k));
				discount *= gamma * lambda;
			}
			advantage[t] = At;
		}
	    return new NetArray(advantage);
	}
	
	public NetArray fill(float value)
	{
		for(int i = 0; i < length(); i++)
			set(i, value);
		return this;
	}
	
	public NetArray ascending()
	{
		for(int i = 0; i < length(); i++)
			set(i, i);
		return this;
	}
	
	public NetArray descending()
	{
		for(int i = length() - 1; i >= 0; i--)
			set(i, i);
		return this;
	}
	
	public NetArray shuffle()
	{	
		@SuppressWarnings("unchecked")
		ArrayList<Float> cloned = (ArrayList<Float>) array.clone();
		
		Collections.shuffle(cloned);
		return new NetArray(cloned);
	}
	
	public float item()
	{
		if(length() != 1) throw new ArrayIndexOutOfBoundsException();
		
		return get(0);
	}
	
	public float get(int index)
	{
		if(index > array.size()) throw new ArrayIndexOutOfBoundsException();
		
		return array.get(index);
	}
	
	public NetArray get(NetArray indices)
	{
		float[] out = new float[indices.length()];
		for(float i : indices.array)
		{
			int index = (int) Math.floor(i);
			out[index] = get(index);
		}
		return new NetArray(out);
	}
	
	public NetArray set(int index, float value)
	{
		if(index > array.size()) throw new IndexOutOfBoundsException();
		
		array.set(index, value);
		return this;
	}
	
	public void remove(int index)
	{
		if(index > array.size()) throw new IndexOutOfBoundsException();
		
		array.remove(index);
	}
	
	public NetArray append(float value)
	{
		array.add(value);
		return this;
	}
	
	public int length()
	{
		return array.size();
	}
	
	public ArrayList<Float> list()
	{
		return array;
	}
	
	public float[] array()
	{
		float[] arr = new float[length()];
		for(int i = 0; i < length(); i++)
			arr[i] = get(i);
		return arr;
	}
	
	public String string()
	{
		return Arrays.toString(array());
	}
	
	public void clear()
	{
		array.clear();
	}
	
	@Override
	public NetArray clone()
	{
		return (NetArray) array.clone();
	}

	@Override
	public Iterator<Float> iterator()
	{
		return array.iterator();
	}
}
