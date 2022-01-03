package com.alphander.networks.environment.play;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyAction extends KeyAdapter
{
	int[] keys;
	int action;
	
	public KeyAction(int[] keys)
	{
		this.keys = keys;
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		keyChecker(e);
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		action = 0;
	}
	
	public int getAction()
	{
		return action;
	}
	
	private void keyChecker(KeyEvent e)
	{
		for(int i = 0; i < keys.length; i++)
			if(e.getKeyCode() == keys[i])
				action = i + 1;
	}
}
