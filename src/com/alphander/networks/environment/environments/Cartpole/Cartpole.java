 package com.alphander.networks.environment.environments.cartpole;

import javax.swing.JFrame;
import com.alphander.networks.environment.Environment;
import com.alphander.networks.environment.play.KeyAction;
import com.alphander.networks.utils.NetArray;

public class Cartpole extends Environment
{	
	//Environment parameters
	public float dt = 0.01f;
    public float airResistance = 0.05f;
    public float cartAcceleration = 400f;
    public float poleCartAuthority = 10f;//In place of masses
    public float cartPoleAuthority = 0.1f;//In place of masses
    public float gravity = -9.80665f;
    public float radius = 20.0f;
    public int stepSize = 4;
    public float randomTheta = 5f;
    
    //Limits
    public float angleLimit = 30f;
    public float posLimit = 400f;
    public float timeLimit = 200;
	
    //State
    float theta;
    float thetaVelocity;
    float pos;
    float velocity;
    public float t;
    
    public Cartpole(String name, float theta, float thetaVelocity)
    {
    	reset();
    	observationSpace = 4;
    	actionSpace = 2;
    	screenX = 960;
    	screenY = 540;
    	this.name = name;
    	this.theta = (float) Math.toRadians(theta);
    	this.thetaVelocity = thetaVelocity;
    }
    public Cartpole()
    {
    	reset();
    	observationSpace = 4;
    	actionSpace = 2;
    	screenX = 960;
    	screenY = 540;
    	name = "Cartpole";
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
    	return new NetArray(theta, thetaVelocity, pos, velocity);
	}
	@Override
	public void reset()
	{
		theta = (float) Math.toRadians(((Math.random() -0.5f) * 2f * randomTheta) - 90f);
		thetaVelocity = 0.0f;
		pos = 0.0f;
		velocity = 0.0f;
		terminal = 1;
		reward = 0;
		t = 0;
	}
	
	private void step(NetArray action)
	{
		float a = (action.get(0) - 0.5f) * 2f * cartAcceleration;
		
        velocity += a * dt;
        velocity -= velocity * airResistance * dt;

        thetaVelocity += cartPoleAuthority * Math.sin(theta) * (a / radius) * dt;
        thetaVelocity += Math.cos(theta) * (-gravity / radius) * dt;
        thetaVelocity -= thetaVelocity * airResistance * dt;

        
        pos += velocity * dt;
        pos += poleCartAuthority * thetaVelocity * Math.sin(theta) * dt;
        
        theta += thetaVelocity * dt;
	}
	
	private void rules()
	{	
		float theta = (float) Math.toDegrees(this.theta);
		reward = 1f;
		
		//If all the checks are true, the sim can continue.
		boolean check = true;
		check = (pos < posLimit && pos > -posLimit) && check;
		
		check = theta < angleLimit - 90f && theta > -angleLimit - 90f && check;
		
		if(!check) reward = -5;
		
		check = (t < timeLimit || timeLimit < 0) && check;
		
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
		
		CartpoleVisualizer visual = new CartpoleVisualizer(this, action, screenX, screenY);
		frame.setContentPane(visual);
	}
	
	public void render()
	{
		render(null);
	}
}
