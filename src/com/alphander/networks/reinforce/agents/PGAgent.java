package com.alphander.networks.reinforce.agents;

import java.util.ArrayList;

import com.alphander.networks.environment.Environment;
import com.alphander.networks.network.deepnet.DeepNet;
import com.alphander.networks.reinforce.Agent;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;

public class PGAgent extends Agent
{
	//Policy Gradient Agent
	public Environment env;
	public DeepNet actor;
	private int actionSpace;
	private int observationSpace;

	private float gamma;
	private float random;

	private int batchSize;
	private int epochs = 1;
	public float totalReward = 0;
	public float loss = 0;

	ArrayList<NetArray> states = new ArrayList<NetArray>();
	ArrayList<NetArray> probs = new ArrayList<NetArray>();
	ArrayList<Integer> actions = new ArrayList<Integer>();
	NetArray rewards = new NetArray();
	NetArray dones = new NetArray();
	int testCount = 0;

	public PGAgent(Environment env, DeepNet actor, int batchSize, float gamma, float random)
	{
		this.env = env;
		this.actor = actor;
		this.gamma = gamma;
		this.batchSize = batchSize;
		actionSpace = actor.getOutput().length();
		observationSpace = actor.getInput().length();
	}
	
	@Override
	public void run(int delay)
	{
		while(env.getDone() != 0)
		{
			NetArray state = env.getState();
			NetArray probability = actor.run(state);
			int action = (int) probability.add(new NetArray(actionSpace).noise(random).sumZero()).sampleDiscrete();

			env.setAction(new NetArray().append(action));

			float reward = env.getReward();
			float done = env.getDone();

			states.add(state);
			probs.add(probability);
			actions.add(action);
			rewards.append(reward);
			dones.append(done);
			totalReward += reward;
			testCount++;

			Util.delay(delay);
		}
		env.reset();
	}
	
	@Override
	public void learn()
	{	
		NetArray advantage = rewards.discounted(gamma, dones);
		for(int e = 0; e < epochs; e++)
		{
			NetArray losses = new NetArray();

			for(NetArray batch : new NetArray(testCount).ascending().shuffle().split(batchSize))
			{
				actor.clearGradients();
				for(float i : batch)
				{
					int index = (int) i;
					NetArray state = new NetArray(observationSpace);
					state = state.add(this.states.get(index));
		
					float A = advantage.get(index);
		
					NetArray probs = this.probs.get(index);
					int action = actions.get(index);
		
					float prob = probs.get(action);
					float logProbs = (float) Math.log(prob);
		
					float loss = -logProbs * A / testCount;
		
					losses.append(loss);
					float probGrad = -(A / prob) / testCount;
					NetArray gradient = new NetArray(actionSpace).set(action, probGrad);
					actor.run(state);
					actor.backward(gradient);
				}
				actor.step(batchSize);
			}

		loss = losses.sum();
		}

		clear();
	}

	@Override
	public void clear()
	{
		states.clear();
		probs.clear();
		actions.clear();
		rewards.clear();
		dones.clear();
		totalReward = 0;
		testCount = 0;
	}
}
