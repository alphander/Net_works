 package com.alphander.networks.environment.environments;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.alphander.networks.environment.Environment;
import com.alphander.networks.environment.play.KeyAction;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;

public class Cartpole extends Environment
{	
	//Render parameters
	int screenX = 960;
	int screenY = 540;
	String name = "Cartpole";
	
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
    private float theta;
    private float thetaVelocity;
    private float pos;
    private float velocity;
    public float t;
    
    //Response
    public Cartpole()
    {
    	reset();
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
		
		CartpoleVisualizer visual = new CartpoleVisualizer(action);
		frame.setContentPane(visual);
	}
	
	public void render()
	{
		render(null);
	}
	
	class CartpoleVisualizer extends JPanel
	{
		private static final long serialVersionUID = -7653112463947514894L;

		KeyAction action;
		
		public CartpoleVisualizer(KeyAction action)
		{
			this.action = action;
			this.setBackground(Color.LIGHT_GRAY);
		}

		@Override
		protected void paintComponent(Graphics inGraphics)
		{
			super.paintComponent(inGraphics);
			
			Graphics2D graphics = (Graphics2D) inGraphics;
			this.removeAll();
			
			float xOffset = screenX / 2;
			float yOffset = screenY / 2;
			
			float mag = 200f;
			float x1 = pos;
			float y1 = 100f;
			float x2 = (float) Math.cos(theta) * mag + x1;
			float y2 = (float) Math.sin(theta) * mag + y1;
			
			float rectX = 150f;
			float rectY = 50f;
			
			graphics.setColor(Color.BLACK);
			graphics.fill(new Rectangle2D.Float((xOffset + x1) - rectX / 2, (yOffset + y1) - rectY / 2, rectX, rectY));
			
			graphics.setColor(Color.ORANGE);
			graphics.setStroke(new BasicStroke(10f));
			graphics.draw(new Line2D.Float(xOffset + x1, yOffset + y1, xOffset + x2, yOffset + y2));
			repaint();
			
			if(action != null)
			{
				setAction(new NetArray().append(action.getAction()));
				if(getDone() == 0)
					reset();
				Util.delay(20);
			}
		}
	}
}
