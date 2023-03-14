package Layers

import Neurons.InputNeuron

open class Layer<T>(neurons: MutableList<T> ) {
    var neurons = mutableListOf<T>();

    init {
        this.neurons = neurons;
    }
}
