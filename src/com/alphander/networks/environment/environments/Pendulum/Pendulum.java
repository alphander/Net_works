package com.alphander.networks.environment.environments.pendulum;

import javax.swing.JFrame;
import com.alphander.networks.environment.Environment;
import com.alphander.networks.environment.play.KeyAction;
import com.alphander.networks.utils.NetArray;

public class Pendulum extends Environment
{
	//Render parameters
	int screenX = 540;
	int screenY = 540;
	String name = "Pendulum";

	public float acceleration = 10;

	public float dt = 0.01f;
	public int stepSize = 4;
	public float radius = 2.0f;
	public float gravity = -9.80665f;
	public float airResistance = 0.05f;

	public float timeLimit = 600f;
	public float rewardTrigger= 0.7f;

	float theta;
	float vel;
	float t = 0f;

	public Pendulum()
	{
		reset();
		observationSpace = 2;
		actionSpace = 2;
	}
	@Override
	public void setAction(NetArray action)
	{
		for(int i = 0; i < stepSize; i++) step(action);
		rules();
		t++;
	}
	@Override
	public NetArray getState()
	{
		return new NetArray((float) Math.toDegrees(theta), (float) vel);
	}
	@Override
	public float getReward()
	{
		return reward;
	}

	@Override
	public int getDone()
	{
		return terminal;
	}

	@Override
	public void reset()
	{
		theta = (float) Math.toRadians(90);
		vel = 0f;
		terminal = 1;
		reward = 0f;
		t = 0f;
	}

	private void step(NetArray action)
	{
		float a = (action.get(0) - 0.5f) * 2f * acceleration;

		vel += Math.sin(theta) * (a / radius) * dt;
		vel += Math.cos(theta) * (-gravity / radius) * dt;
		vel -= vel * airResistance * dt;
		theta += vel * dt;
	}

	private void rules()
	{	
		float sin = (float) Math.sin(theta);
		reward = (-sin + 1) * Math.abs(vel);

		//If all the checks are true, the sim can continue.
		boolean check = (t < timeLimit || timeLimit < 0);
		if(!check) terminal = 0;
	}

	public void render(KeyAction action)
	{
		JFrame frame = new JFrame();
		frame.setTitle(name);
		frame.setSize(screenX, screenY);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		if(action != null)
			frame.addKeyListener(action);

		PendulumVisualizer visual = new PendulumVisualizer(this, action);
		frame.setContentPane(visual);
	}

	public void render()
	{
		render(null);
	}
}
