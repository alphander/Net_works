package com.alphander.networks.reinforce.ppo;

import java.util.ArrayList;

import com.alphander.networks.environment.Environment;
import com.alphander.networks.network.deepnet.DeepNet;
import com.alphander.networks.utils.NetArray;
import com.alphander.networks.utils.Util;

public class PPOAgent
{
	//Policy Gradient Agent
	public DeepNet actor;
	public DeepNet critic;
	public Environment env;
	private int actionSpace;
	private int observationSpace;

	private float gamma;
	private float random;

	private int batchSize = 5;
	public float totalReward = 0;
	public float loss = 0;

	ArrayList<NetArray> states = new ArrayList<NetArray>();
	ArrayList<NetArray> probs = new ArrayList<NetArray>();
	ArrayList<Integer> actions = new ArrayList<Integer>();
	NetArray values = new NetArray();
	NetArray rewards = new NetArray();
	NetArray dones = new NetArray();
	int testCount = 0;

	public PPOAgent(Environment env, DeepNet actor, DeepNet critic, int batchSize, float gamma, float random)
	{
		this.env = env;
		this.actor = actor;
		this.critic = critic;
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
			float value = critic.run(state).item();
			int action = probability.add(new NetArray(actionSpace).noise(random).sumZero()).sampleDiscrete();

			env.setAction(new NetArray().append(action));

			float reward = env.getReward();
			float done = env.getDone();

			states.add(state);
			probs.add(probability);
			actions.add(action);
			rewards.append(reward);
			dones.append(done);
			values.append(value);
			totalReward += reward;
			testCount++;

			Util.delay(delay);
		}
		env.reset();
	}

	public void learn()
	{	
		NetArray advantage = rewards.computeAdvantage(dones, values, gamma, actionSpace);

		NetArray losses = new NetArray();

		for(NetArray batch : new NetArray(testCount).ascending().shuffle().split(batchSize))
		{
			actor.clearGradients();
			for(float i : batch)
			{
				int index = (int) i;
				NetArray state = new NetArray(observationSpace);
				state = state.add(this.states.get(index));
				
				NetArray ac = actor.run(state);
				float r = critic.run(state).item();
				Util.print("" + actions.get(index));
				Util.print("" + ac.get(index));
				Util.print("" + r);
	
				float A = advantage.get(index);
	
				NetArray probs = this.probs.get(index);
				int action = actions.get(index);
	
				float prob = probs.get(action);
				float logProbs = (float) Math.log(prob);
	
				float loss = -logProbs * A / testCount;
	
				losses.append(loss);
				float probGrad = -(A / prob) / testCount;
				NetArray gradient = new NetArray(actionSpace).set(action, probGrad);
				
				actor.backward(gradient);
				Util.delay(1000);
			}
			actor.step(batchSize);
		}
		
		loss = losses.sum();

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
