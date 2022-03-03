package com.alphander.networks.utils.display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.alphander.networks.network.deepnet.DeepNet;

public class NetworkGraph
{
	private int width = 1024;
	private int height = 512;
	
	private int length = 0;
	private Color lineColor;
	
	private ArrayList<Float> list = new ArrayList<Float>();
	private float min = 0.0f;
	private float max = 0.0f;
	private JFrame frame;
	
	public NetworkGraph(DeepNet net)
	{
		setup("Error", Color.BLUE, 0);
	}
	
	public NetworkGraph(String name, Color lineColor, int length)
	{
		setup(name, lineColor, length);
	}
	
	private void setup(String name, Color lineColor, int length)
	{
		this.length = length;
		this.lineColor = lineColor;
		
		frame = new JFrame();
		frame.setVisible(true);
		frame.setTitle(name);
		frame.setSize(width, height + 40);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		NetworkPanel panel = new NetworkPanel();
		
		frame.setContentPane(panel);
	}
	
	public void addData(float data)
	{
		list.add(data);
		if(list.size() > length && length != 0)
			list.remove(0);
		min = 0;
		max = 0;
		for(float i : list)
		{
			if(i < min)
				min = i;
			if(i > max)
				max = i;
		}
	}
	
	class NetworkPanel extends JPanel
	{
		private static final long serialVersionUID = -9170048570433628132L;
		
		public NetworkPanel()
		{
			this.setBackground(Color.LIGHT_GRAY);
		}
		
		@Override
		protected void paintComponent(Graphics inGraphics)
		{
			super.paintComponent(inGraphics);
			
			Graphics2D graphics = (Graphics2D) inGraphics;
			RenderingHints hint = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			this.removeAll();
			
			graphics.setRenderingHints(hint);
			graphics.setColor(lineColor);
			graphics.setStroke(new BasicStroke(2.8f));
			
			for(int i = 1; i < list.size() - 1; i++)
			{
				int prev = i - 1;
				float x = normalize(i, 0, list.size()) * width;
				float y = normalize(list.get(i), min, max) * height;
				float xp = normalize(prev, 0, list.size()) * width;
				float yp = normalize(list.get(prev), min, max) * height;
				
				graphics.draw(new Line2D.Float(xp, flip(yp, height), x, flip(y, height)));
			}
			repaint();
		}
		
		private float normalize(float val, float min, float max)
		{
			return (val - min) / (max - min);
		}
		
		private float flip(float val, float range)
		{
			return -val + range;
		}
	}
	
}
