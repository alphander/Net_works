package com.alphander.networks.network;

import com.alphander.networks.utils.NetArray;

public interface Network
{
	public NetArray run(NetArray in);
	public void train(NetArray in);
	public NetArray getInput();
	public NetArray getOutput();
}
