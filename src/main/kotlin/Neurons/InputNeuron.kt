package Neurons

class InputNeuron(inputs:  MutableList<Double>) : Neuron(inputs) {
    override fun countOutput(): Double {
        //input всего один в кажом входном нейроне
        return inputs.first();
    }
}