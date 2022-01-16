package com.alphander.networks.environment.environments.pendulum;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import com.alphander.networks.environment.play.KeyAction;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;

class PendulumVisualizer extends JPanel
{
	private static final long serialVersionUID = -1174915281353137161L;

	private KeyAction action;
	private Pendulum pendulum;
	private int screenX, screenY;

	public PendulumVisualizer(Pendulum pendulum, KeyAction action, int screenX, int screenY)
	{
		this.action = action;
		this.pendulum = pendulum;
		this.setBackground(Color.LIGHT_GRAY);
		this.screenX = screenX;
		this.screenY = screenY;
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
		float x2 = (float) Math.cos(pendulum.theta) * mag;
		float y2 = (float) Math.sin(pendulum.theta) * mag;

		graphics.setColor(Color.GRAY);
		graphics.setStroke(new BasicStroke(8f));
		graphics.draw(new Line2D.Float(xOffset, yOffset, xOffset + x2, yOffset + y2));
		
		float circleSize = 30f;
		
		graphics.setColor(Color.BLACK);
		graphics.fill(new Ellipse2D.Float((xOffset + x2) - circleSize / 2, (yOffset + y2) - circleSize / 2, circleSize, circleSize));
		repaint();

		if(action != null)
		{
			pendulum.setAction(new NetArray().append(action.getAction()));
			if(pendulum.getDone() == 0)
				pendulum.reset();
			Util.delay(20);
		}
	}
}
