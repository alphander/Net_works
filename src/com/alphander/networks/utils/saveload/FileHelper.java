package com.alphander.networks.utils.saveload;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.Scanner;

import com.alphander.networks.network.activation.Activator;
import com.alphander.networks.network.loss.LossFunction;
import com.alphander.networks.network.neatnet.mutation.Mutation;

class FileHelper
{
	public static Scanner getScanner(File file)
	{
		Scanner s = null;
		try 
		{ 
			s = new Scanner(file); 
		} 
		catch (FileNotFoundException e) 
		{}
		return s;
	}
	
	public static Formatter getFormatter(String dir)
	{
		Formatter f = null;
		try 
		{ 
			f = new Formatter(dir); 
		} 
		catch (FileNotFoundException e) 
		{}
		return f;
	}
	
	public static File getFile(String dir)
	{
		File file = new File(dir);
		file.mkdir();
		return file;
	}
	
	@SuppressWarnings("deprecation")
	public static Mutation getMutation(String string)
	{
		try
		{
			return (Mutation) Class.forName(string).newInstance();
		} 
		catch (Exception e)
		{
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static Activator getActivation(String string)
	{
		Activator act = null;
		try
		{
			act = (Activator) Class.forName(string).newInstance();
		} 
		catch (Exception e)
		{}
		return act;
	}
	
	@SuppressWarnings("deprecation")
	public static LossFunction getLossFunction(String string)
	{
		LossFunction act = null;
		try
		{
			act = (LossFunction) Class.forName(string).newInstance();
		} 
		catch (Exception e)
		{}
		return act;
	}
	
	public static String arrayToString(int[] array)
	{
		String str = "";
		if(array.length == 0)
			return str;
		str += array[0];
		for(int i = 1; i < array.length; i++)
			str += "," + array[i];
		return str;
	}
}
