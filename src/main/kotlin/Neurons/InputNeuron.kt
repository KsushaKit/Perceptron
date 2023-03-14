package Neurons

import kotlin.math.pow

class InputNeuron(inputs:  MutableList<Double>) : Neuron(inputs) {
    override fun countOutput(): Double {
        return inputs.first();
    }
}