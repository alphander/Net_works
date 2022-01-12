package com.alphander.networks.environment.environments.cartpole;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import com.alphander.networks.environment.play.KeyAction;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;

class CartpoleVisualizer extends JPanel
{
	private static final long serialVersionUID = -7653112463947514894L;

	KeyAction action;
	Cartpole cartpole;
	
	public CartpoleVisualizer(Cartpole cartpole, KeyAction action)
	{
		this.action = action;
		this.cartpole = cartpole;
		this.setBackground(Color.LIGHT_GRAY);
	}

	@Override
	protected void paintComponent(Graphics inGraphics)
	{
		super.paintComponent(inGraphics);
		
		Graphics2D graphics = (Graphics2D) inGraphics;
		this.removeAll();
		
		float xOffset = cartpole.screenX / 2;
		float yOffset = cartpole.screenY / 2;
		
		float mag = 200f;
		float x1 = cartpole.pos;
		float y1 = 100f;
		float x2 = (float) Math.cos(cartpole.theta) * mag + x1;
		float y2 = (float) Math.sin(cartpole.theta) * mag + y1;
		
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
			cartpole.setAction(new NetArray().append(action.getAction()));
			if(cartpole.getDone() == 0)
				cartpole.reset();
			Util.delay(20);
		}
	}
}
