package com.alphander.networks.environment.play;


import java.awt.event.KeyEvent;

import com.alphander.networks.environment.environments.Cartpole;

public class CartpolePlayer
{
	public static void main(String[] args)
	{
		Cartpole cartpole = new Cartpole();
		cartpole.angleLimit = 70;
		cartpole.timeLimit = -1;
		
		int[] keys = new int[] {KeyEvent.VK_SPACE};
		KeyAction action = new KeyAction(keys);
		
		cartpole.render(action);
	}
}
