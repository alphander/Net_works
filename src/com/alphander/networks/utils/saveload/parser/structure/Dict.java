package com.alphander.networks.utils.saveload.parser.structure;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class Dict extends Part implements Iterable<Entry<Leaf, Part>>
{
	private LinkedHashMap<Leaf, Part> map = new LinkedHashMap<Leaf, Part>();
	
	public void put(Leaf key, Part value)
	{
		map.put(key, value);
	}
	
	public void put(String key, Part value)
	{
		map.put(new Leaf(key), value);
	}
	
	public void put(String key, String value)
	{
		map.put(new Leaf(key), new Leaf(value));
	}
	
	public void put(String key, double value)
	{
		map.put(new Leaf(key), new Leaf(value));
	}
	
	public void put(String key, boolean value)
	{
		map.put(new Leaf(key), new Leaf(value));
	}
	
	public Part get(String key)
	{
		return map.get(new Leaf(key));
	}
	
	public int size()
	{
		return map.size();
	}

	@Override
	public Iterator<Entry<Leaf, Part>> iterator()
	{
		return map.entrySet().iterator();
	}
}
