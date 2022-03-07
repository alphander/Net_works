package com.alphander.networks.utils.saveload;

import com.alphander.networks.network.activation.Activator;
import com.alphander.networks.network.loss.LossFunction;
import com.alphander.networks.network.neatnet.mutation.Mutation;

class FileHelper
{		
	public static Mutation getMutation(String string, float prob) throws Exception
	{
		return (Mutation) Class.forName(Mutation.mutationFile + "." + string).getConstructor(float.class).newInstance(prob);
	}
	
	public static Activator getActivation(String string) throws Exception
	{
		return (Activator) Class.forName(Activator.activatorFile + "." + string).getConstructor().newInstance();
	}
	
	public static LossFunction getLossFunction(String string) throws Exception
	{
		return (LossFunction) Class.forName(LossFunction.lossfunctionFile + "." + string).getConstructor().newInstance();
	}
}
