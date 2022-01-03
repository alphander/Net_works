package com.alphander.networks.environment.play;

import java.awt.event.KeyEvent;

import com.alphander.networks.environment.environments.Pendulum.Pendulum;

public class PendulumPlayer
{
	public static void main(String[] args)
	{
		Pendulum pendulum = new Pendulum();
		
		int[] keys = new int[] {KeyEvent.VK_SPACE};
		KeyAction action = new KeyAction(keys);
		
		pendulum.render(action);
	}
}
