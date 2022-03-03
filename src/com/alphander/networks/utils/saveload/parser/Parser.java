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
	private static final String nextLine = "";
	private static final String indent = "";
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
		if(part == null)
			throw new IllegalArgumentException();
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
		indents += 1;
		boolean first = true;
		for(Entry<String, Part> entry : dict)
		{
			if(first)
				first = false;
			else
				str += pause + nextLine;
			str += string + entry.getKey() + string;
			str += dictPause;
			part(entry.getValue());
		}
		indents -= 1;
		str += nextLine + dictExit;
	}
	
	private void list(List list)
	{
		str += listEnter + nextLine;
		boolean first = true;
		for(Part part : list)
		{
			if(first)
				first = false;
			else
				str += pause + nextLine;
			part(part);
		}
		str += nextLine + listExit;
	}
	
	private void leaf(Leaf leaf)
	{
		if(leaf.getType() == Type.String)
			str += leaf.getString();
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
			if(part == null) return null;
			list.add(part);
			char c = chars[index];
		
			if(c == pause)
				continue;
			if(c == listExit)
				break;
			return null;
		}
		index++;
		return list;
	}
	
	private Dict dict()
	{
		Dict dict = new Dict();
		while(true)
		{	
			search();
			String key = leaf().get();
			
			char c = chars[index];
			if(c != dictPause) return null;
			
			Part value = part();
			
			if(value == null || key == null) return null;
			dict.put(key, value);
			
			c = chars[index];
			if(c == pause)
				continue;
			if(c == dictExit)
				break;
			return null;
		}
		index++;
		return dict;
	}
	
	private Leaf leaf()
	{
		String str = "";
		while(true)
		{
			char c = chars[index];
			for(char s : stop)
				if(s == c)
					return Leaf.leaf(str);
			str += c;
			index++;
		}
	}
	
	private char search()
	{
		while(true)
		{
			char c = chars[++index];
			for(char i : ignore)
				if(c != i)
					return c;
		}
	}
	
	private String indents()
	{
		String str = "";
		for(int i = 0; i < indents; i++)
			str += indent;
		return str;
	}
}
