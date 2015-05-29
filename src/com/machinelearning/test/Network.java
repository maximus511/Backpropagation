/**
 * 
 */
package com.machinelearning.test;

import java.io.Serializable;

/**
 * @author Rahul
 *
 */
public class Network implements Serializable {

    private Layer input;
    private Layer output;
    private Layer[] hiddenLayers;
    private int hiddenCount;

    public Network() {
    }

    public Network(Layer input, Layer output, Layer[] hiddenLayers, int hiddenCount) {
        this.input = input;
        this.output = output;
        this.hiddenLayers = hiddenLayers;
        this.hiddenCount = hiddenCount;
    }

    public void init(int inputcount, int inputneurons, int outputcount, int[] hiddenLayer, int hiddenlayercount) {
        input = new Layer(inputneurons, inputcount);
        if (hiddenLayer.length > 0 && hiddenlayercount > 0) {
            hiddenLayers = new Layer[hiddenlayercount];
            this.hiddenCount = hiddenlayercount;
            for (int i = 0; i < hiddenCount; i++) {
                if (i == 0) {
                    hiddenLayers[i] = new Layer(hiddenLayer[i], inputneurons);
                } else {
                    hiddenLayers[i] = new Layer(hiddenLayer[i], hiddenLayers[i - 1].getNeuronCount());
                }
            }
            output = new Layer(outputcount, hiddenLayers[hiddenlayercount - 1].getNeuronCount());
        } else {
            output = new Layer(outputcount, inputneurons);
        }

    }

    public void propagate(double[] in) {
        input.layerInput = in;
//        memcpy(m_inputlayer.layerinput,input,m_inputlayer.inputcount * sizeof(float));
        input.calculate();
        update(-1);
        if (hiddenLayers != null && hiddenLayers.length > 0) {
            for (int i = 0; i < hiddenCount; i++) {
                hiddenLayers[i].calculate();
                update(i);
            }
        }

        output.calculate();
    }

    public double train(double[] desiredOutput, double[] in, double alpha, double momentum) {
        double error = 0d;

        double sum = 0d;
        double tempSum = 0d;
        double delta = 0d;
        double updateDelta = 0d;
        double out = 0d;
        double localError = 0d;

        propagate(in);
        for (int i = 0; i < output.getNeuronCount(); i++) {
            out = output.neurons[i].output;
            localError = (desiredOutput[i] - out) * out * (1 - out);
            error += Math.pow((desiredOutput[i] - out), 2);

            for (int j = 0; j < output.getInputCount(); j++) {
                delta = output.neurons[i].deltas[j];
                updateDelta = alpha * localError * output.layerInput[j] + delta * momentum;
                output.neurons[i].weights[j] += updateDelta;
                output.neurons[i].deltas[j] = updateDelta;

                sum += output.neurons[i].weights[j] * localError;
            }

            output.neurons[i].weightGain = output.neurons[i].weightGain + alpha * localError * output.neurons[i].gain;
        }

        for (int i = (hiddenCount - 1); i >= 0; i--) {
            for (int j = 0; j < hiddenLayers[i].getNeuronCount(); j++) {
                out = hiddenLayers[i].neurons[j].output;
                localError = out * (1 - out) * sum;
                for (int k = 0; k < hiddenLayers[i].getInputCount(); k++) {
                    delta = hiddenLayers[i].neurons[j].deltas[k];
                    updateDelta = alpha * localError * hiddenLayers[i].layerInput[k] + delta * momentum;
                    hiddenLayers[i].neurons[j].weights[k] += updateDelta;
                    hiddenLayers[i].neurons[j].deltas[k] = updateDelta;
                    tempSum += hiddenLayers[i].neurons[j].weights[k] * localError;
                }

                hiddenLayers[i].neurons[j].weightGain = (hiddenLayers[i].neurons[j].weightGain + alpha * localError * hiddenLayers[i].neurons[j].gain);

            }
            sum = tempSum;
            tempSum = 0;
        }

        for (int i = 0; i < input.getNeuronCount(); i++) {
            out = input.neurons[i].output;
            localError = out * (1 - out) * sum;

            for (int j = 0; j < input.getInputCount(); j++) {
                delta = input.neurons[i].deltas[j];
                updateDelta = alpha * localError * input.layerInput[j] + delta * momentum;

                input.neurons[i].weights[j] += updateDelta;
                input.neurons[i].deltas[j] = updateDelta;
            }
            input.neurons[i].weightGain = (input.neurons[i].weightGain
                    + alpha * localError * input.neurons[i].gain);
        }

        return error / 2;
    }

    public void update(int layerIndex) {
        if (layerIndex == -1) {
            for (int i = 0; i < input.getNeuronCount(); i++) {
                if (hiddenLayers != null && hiddenLayers.length > 0) {
                    hiddenLayers[0].layerInput[i] = input.neurons[i].output;
                } else {
                    output.layerInput[i] = input.neurons[i].output;
                }
            }
        } else {
            for (int i = 0; i < hiddenLayers[layerIndex].getNeuronCount(); i++) {
                if (layerIndex < hiddenCount - 1) {
                    hiddenLayers[layerIndex + 1].layerInput[i] = hiddenLayers[layerIndex].neurons[i].output;
                } else {
                    output.layerInput[i] = hiddenLayers[layerIndex].neurons[i].output;
                }
            }
        }
    }

	public Layer getInput() {
		return input;
	}

	public void setInput(Layer input) {
		this.input = input;
	}

	public Layer getOutput() {
		return output;
	}

	public void setOutput(Layer output) {
		this.output = output;
	}

	public Layer[] getHiddenLayers() {
		return hiddenLayers;
	}

	public void setHiddenLayers(Layer[] hiddenLayers) {
		this.hiddenLayers = hiddenLayers;
	}

	public int getHiddenCount() {
		return hiddenCount;
	}

	public void setHiddenCount(int hiddenCount) {
		this.hiddenCount = hiddenCount;
	}

 
}
