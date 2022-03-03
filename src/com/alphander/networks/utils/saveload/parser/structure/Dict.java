package com.alphander.networks.utils.saveload.parser.structure;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class Dict extends Part implements Iterable<Entry<String, Part>>
{
	private LinkedHashMap<String, Part> map = new LinkedHashMap<String, Part>();
	
	public void put(String key, Part value)
	{
		map.put(key, value);
	}
	
	public void put(String key, String value)
	{
		Leaf leaf = new Leaf();
		leaf.setString(value);
		map.put(key, leaf);
	}
	
	public void put(String key, double value)
	{
		map.put(key, new Leaf(value));
	}
	
	public void put(String key, boolean value)
	{
		map.put(key, new Leaf(value));
	}
	
	public Part get(String key)
	{
		return map.get(key);
	}
	
	public int size()
	{
		return map.size();
	}

	@Override
	public Iterator<Entry<String, Part>> iterator()
	{
		return map.entrySet().iterator();
	}
}
