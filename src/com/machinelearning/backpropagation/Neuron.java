package com.machinelearning.backpropagation;

import java.util.Random;

/**
 * Class representing a Neuron in the network
 * @author Rahul
 *
 */
public class Neuron {
	double[] weights;
    double[] deltas;
    double output;

    public Neuron() {
    	Random r = new Random();
        this.weights = new double[2];
        for (int i = 1; i <= 2; i++) {
            this.weights[i-1] = i%2==0?-r.nextDouble():r.nextDouble();
        }
    }
    
    public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	public double[] getDeltas() {
		return deltas;
	}

	public void setDeltas(double[] deltas) {
		this.deltas = deltas;
	}

	public double getOutput() {
		return output;
	}

	public void setOutput(double output) {
		this.output = output;
	}

}
