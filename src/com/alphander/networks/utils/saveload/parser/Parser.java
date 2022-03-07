package com.alphander.networks.utils.saveload.parser;

import java.util.Map.Entry;

import com.alphander.networks.utils.saveload.parser.structure.Dict;
import com.alphander.networks.utils.saveload.parser.structure.Leaf;
import com.alphander.networks.utils.saveload.parser.structure.List;
import com.alphander.networks.utils.saveload.parser.structure.Part;
import com.alphander.networks.utils.saveload.parser.structure.Type;

public class Parser
{
	private String str;
	private int indents = 0;
	
	private int index;
	private char[] chars;
	
	private static final char dictEnter = '{';
	private static final char dictPause = ':';
	private static final char dictExit = '}';
	private static final char listEnter = '[';
	private static final char listExit = ']';
	private static final char pause = ',';
	private static final String nextLine = "\n";
	private static final String indent = "\t";
	private static final String space = " ";
	private static final char string = '"';
	private static final char[] ignore = {' ', '\n', '\t'};
	private static final char[] stop = {'{', ':', '}', '[', ']', ','};
	
	public String save(Part part)
	{
		str = "";
		indents = 0;
		part(part);
		return str;
	}
	
	public Part load(String str)
	{
		index = -1;
		chars = str.toCharArray();
		Part part = part();
		if(part == null) throw new IllegalArgumentException();
		return part;
	}
	
	private void part(Part part)
	{
		if(part instanceof Dict)
			dict((Dict) part);
		else if(part instanceof List)
			list((List) part);
		else if(part instanceof Leaf)
			leaf((Leaf) part);
			
	}
	
	private void dict(Dict dict)
	{
		str += dictEnter + nextLine;
		indents++;
		boolean first = true;
		for(Entry<String, Part> entry : dict)
		{
			if(first)
				first = false;
			else
				str += pause + nextLine;
			str += indents();
			str += string + entry.getKey() + string;
			str += dictPause + space;
			part(entry.getValue());
		}
		indents--;
		str += nextLine + indents() + dictExit;
	}
	
	private void list(List list)
	{
		str += listEnter + nextLine;
		indents++;
		boolean first = true;
		for(Part part : list)
		{
			if(first)
				first = false;
			else
				str += pause + nextLine;
			str += indents();
			part(part);
		}
		indents--;
		str += nextLine + indents() + listExit;
	}
	
	private void leaf(Leaf leaf)
	{
		if(leaf.getType() == Type.String)
			str += leaf.get();
		if(leaf.getType() == Type.Number)
			str += leaf.getNumber();
		if(leaf.getType() == Type.Boolean)
			str += leaf.getBoolean();
	}
	
	private Part part()
	{
		char c = search();
		if(c == listEnter)
			return list();
		if(c == dictEnter)
			return dict();
		return leaf();
	}
	
	private List list()
	{
		List list = new List();
		while(true)
		{	
			Part part = part();
			list.add(part);
			char c = chars[index];
		
			if(c == pause)
				continue;
			if(c == listExit)
				break;
			throw new IllegalArgumentException();
		}
		search();
		return list;
	}
	
	private Dict dict()
	{
		Dict dict = new Dict();
		while(true)
		{	
			search();
			String key = leaf().getString();
			
			char c = chars[index];
			if(c != dictPause) throw new IllegalArgumentException();
			
			Part value = part();
			
			dict.put(key, value);
			
			c = chars[index];
			if(c == pause)
				continue;
			if(c == dictExit)
				break;
			throw new IllegalArgumentException();
		}
		search();
		return dict;
	}
	
	private Leaf leaf()
	{
		String str = "";
		while(index < chars.length-1)
		{
			char c = chars[index];
			for(char s : stop)
				if(s == c)
					return Leaf.leaf(str);
			str += c;
			index++;
		}
		throw new ArrayIndexOutOfBoundsException();
	}
	
	private char search()
	{
		while(index < chars.length-1)
		{
			boolean b = false;
			char c = chars[++index];
			for(char i : ignore)
				b = c == i || b;
			
			if(!b) return c;
		}
		return 0;
	}
	
	private String indents()
	{
		String str = "";
		for(int i = 0; i < indents; i++)
			str += indent;
		return str;
	}
}
