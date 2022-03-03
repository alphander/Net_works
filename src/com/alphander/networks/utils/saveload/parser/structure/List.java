package com.alphander.networks.utils.saveload.parser.structure;

import java.util.ArrayList;
import java.util.Iterator;

public class List extends Part implements Iterable<Part>
{
	private ArrayList<Part> list = new ArrayList<Part>();
	
	public void add(Part part)
	{
		list.add(part);
	}
	
	public void add(String value)
	{
		list.add(new Leaf(value));
	}
	
	public void add(double value)
	{
		list.add(new Leaf(value));
	}
	
	public void add(boolean value)
	{
		list.add(new Leaf(value));
	}
	
	public Part get(int index)
	{
		return list.get(index);
	}
	
	public int size()
	{
		return list.size();
	}

	@Override
	public Iterator<Part> iterator()
	{
		return list.iterator();
	}
}
