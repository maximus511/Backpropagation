package com.machinelearning.backpropagation;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class implementing Forward and Backpropagation for Artificial Neural Network
 * 
 * @author Rahul
 *
 */
public class Backpropagation {

	public static double[][] inputs;
	public static double[] outputs;
	public static Layer[] layers = new Layer[3];
	public static double alpha=0.05d;
	public static ArrayList<Double> calculatedOutput;
	
	/**
	 * Read data set
	 * @param fileName
	 */
	public static void readData(String fileName) {
		File file = new File(fileName);
		try {
			List<String[]> lines = new ArrayList<>();
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line = "";
				br.readLine();
				while ((line = br.readLine()) != null && !line.equals("")) {
					String[] splits = line.trim().split("\\s+");
					lines.add(splits);
				}
			}
			inputs = new double[lines.size()][2];
			outputs = new double[lines.size()];
			for (int i = 0; i < inputs.length; i++) {
				for (int j = 0; j < 2; j++) {
					inputs[i][j] = Double.parseDouble(lines.get(i)[j]);
				}
				outputs[i] = Double.parseDouble(lines.get(i)[lines.get(i).length - 1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Execute for the given epoch value
	 * @param maxEpoch
	 */
	private static void execute(int maxEpoch) {

		int currentEpoch = 0;
		b1=0.3;
		 b2=-0.4;
		alpha = 0.05d;
		initializeLayers();
		do {

			for (int i = 0; i < inputs.length; i++) {
				int j=0;
				propagate(inputs[i][j], inputs[i][j+1], i);
			}
			currentEpoch++;
			alpha*=0.95;
		} while (maxEpoch > currentEpoch);
	}

	/**
	 * Propagate the data through the network
	 * @param x1
	 * @param x2
	 * @param recNumber
	 */
	public static void propagate(double x1, double x2, int recNumber)

	{
		double delta = 0d;
		double out = 0d;
		forwardPropagate(x1, x2);
		
		//BackPropagation
		out = layers[2].getNeurons()[0].getOutput();
		delta = (outputs[recNumber] - out) * out * (1 - out);
		double[] tempWeight = layers[2].getNeurons()[0].weights;
		layers[2].getNeurons()[0].weights[0] += alpha * delta * layers[1].getNeurons()[0].getOutput();
		layers[2].getNeurons()[0].weights[1] += alpha * delta * layers[1].getNeurons()[1].getOutput();
		b1+=alpha*delta;

		double[] deltaHidden = new double[2];
		for(int i=0; i<2;i++)
		{
			deltaHidden[i] = layers[1].getNeurons()[i].getOutput()*(1-layers[1].getNeurons()[i].getOutput())*(tempWeight[0]*delta+tempWeight[1]*delta);
			layers[1].getNeurons()[i].weights[0]=alpha*deltaHidden[i]*layers[0].getNeurons()[0].getOutput();
			layers[1].getNeurons()[i].weights[1]=alpha*deltaHidden[i]*layers[0].getNeurons()[1].getOutput();
		}
		b2+=alpha*deltaHidden[0];

	}

	/**
	 * Method implementing forward propagation
	 * @param x1
	 * @param x2
	 */
	static double b1=0.3;
	static double b2=-0.4;
	private static void forwardPropagate(double x1, double x2) {
		layers[0].getNeurons()[0].setOutput(x1);
		layers[0].getNeurons()[1].setOutput(x2);

		double lSum =b1;
		for(int i=0; i<2;i++)
		{
			for(int j=0;j<2;j++)
			{
				lSum += layers[0].getNeurons()[j].getOutput()*layers[1].getNeurons()[i].getWeights()[j];
			}
			double sigmoid = 1.0f / (1.0f + Math.exp(-1 * lSum));
			layers[1].getNeurons()[i].setOutput(sigmoid);
		}
		lSum=b2;
		for(int j=0;j<2;j++)
		{
			lSum += layers[1].getNeurons()[j].getOutput()*layers[2].getNeurons()[0].getWeights()[j];
		}
		double sigmoid = 1.0f / (1.0f + Math.exp(-1 * lSum));
		layers[2].getNeurons()[0].setOutput(sigmoid);
	}
	
	
	/**
	 * Method to calculate the accuracy of the network
	 * @return
	 */
	public static double calculateAccuracy()
	{
		int correct=0;
		calculatedOutput = new ArrayList<Double>();
		for (int i = 0; i < inputs.length; i++) {
			int j=0;
			forwardPropagate(inputs[i][j], inputs[i][j+1]);
			if(layers[2].getNeurons()[0].getOutput()>0.5)
			{
				calculatedOutput.add(1d);
			}
			else
			{
				calculatedOutput.add(0d);
			}
		}
		
		for(int k=0;k<outputs.length;k++)
		{
			if(calculatedOutput.get(k)==outputs[k])
			{
				correct++;
			}
		}
		double accuracy = (double)correct/(double)outputs.length;
		return accuracy;
	}
	
	/**
	 * Initialize the layers of the network
	 */
	private static void initializeLayers() {

		layers = new Layer[3];
		for(int i=0; i<2;i++)
		{
			Neuron[] neurons = new Neuron[2];
			for(int j=0; j<2;j++)
			{
				neurons[j] = new Neuron();
			}
			layers[i] = new Layer();
			neurons[0].setWeights(new double[]{0.3d,-0.5d});
			neurons[1].setWeights(new double[]{-0.7d,0.4d});
			layers[i].setNeurons(neurons);
		}
		Neuron[] outNeuron = new Neuron[1];
		outNeuron[0] = new Neuron();
		outNeuron[0].setWeights(new double[]{0.8d,0.6d});
		layers[2] = new Layer();
		layers[2].setNeurons(outNeuron);
		
		System.out.println("Layer-1");
		System.out.println(layers[1].getNeurons()[0].weights[0]+"   "+layers[1].getNeurons()[0].weights[1]);
		System.out.println(layers[1].getNeurons()[1].weights[0]+"   "+layers[1].getNeurons()[1].weights[1]);
		
		System.out.println("Layer-2");
		System.out.println(layers[2].getNeurons()[0].weights[0]+"   "+layers[2].getNeurons()[0].weights[1]);
	}

	/**
	 * Main function
	 * @param args
	 */
	public static void main(String[] args)
	{
		if(args.length!=1)
		{
			System.out.println("Invalid number of arguments!");
			return;
		}
		readData(args[0]);
		execute(1);
		System.out.println("Accuracy for epoch=1 is "+calculateAccuracy()*100);
		printWeights();
		execute(100);
		System.out.println("Accuracy for epoch=100 is "+calculateAccuracy()*100);
		printWeights();
	}

	private static void printWeights() {
		// TODO Auto-generated method stub
		System.out.println("Layer-1");
		System.out.println(layers[1].getNeurons()[0].weights[0]+"   "+layers[1].getNeurons()[0].weights[1]);
		System.out.println(layers[1].getNeurons()[1].weights[0]+"   "+layers[1].getNeurons()[1].weights[1]);
		
		System.out.println("Layer-2");
		System.out.println(layers[2].getNeurons()[0].weights[0]+"   "+layers[2].getNeurons()[0].weights[1]);
	}

}
