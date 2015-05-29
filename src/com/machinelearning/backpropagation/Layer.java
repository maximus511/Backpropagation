package com.machinelearning.backpropagation;

/**
 * Class representing a layer in the network
 * @author Rahul
 *
 */
public class Layer {
	Neuron[] neurons;
	double[] layerInput;

	public Layer() {
	}

	public Neuron[] getNeurons() {
		return neurons;
	}

	public void setNeurons(Neuron[] neurons) {
		this.neurons = neurons;
	}

	public double[] getLayerInput() {
		return layerInput;
	}

	public void setLayerInput(double[] layerInput) {
		this.layerInput = layerInput;
	}
}
