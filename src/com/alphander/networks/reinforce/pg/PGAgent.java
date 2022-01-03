package com.alphander.networks.reinforce.pg;

import java.util.ArrayList;

import com.alphander.networks.environment.Environment;
import com.alphander.networks.network.deepnet.DeepNet;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;

public class PGAgent
{
	//Policy Gradient Agent
	public DeepNet actor;
	public Environment env;
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

	public void testEnvironment(int delay)
	{
		while(env.getDone() != 0)
		{
			NetArray state = env.getState();
			NetArray probability = actor.run(state);
			int action = probability.add(new NetArray(actionSpace).gaussianNoise(random).sumZero()).sampleDiscrete();

			//int action = new NetArray(actionSpace).noise(random).clip(0f, 1f).softMax().sampleDiscrete();

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

		clearAll();
	}

	public void clearAll()
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
