package com.alphander.networks.network.neatnet;

import java.util.HashMap;

public class Node
{
	HashMap<Node, Float> inputs = new HashMap<Node, Float>();
	Node next;
	float value = 0f;
	
	public void update()
	{
		value = 0f;
		for(Node node : inputs.keySet())
			value += node.value * inputs.get(node);
		next.update();
	}
}
