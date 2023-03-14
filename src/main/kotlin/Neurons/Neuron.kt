package Neurons

import java.lang.Math.E
import kotlin.math.pow
import kotlin.random.Random

//зачем мне 3 типа нейронов?
open class Neuron (inputs:  MutableList<Double>) {

    open var inputs = mutableListOf<Double>();
    var weights = mutableListOf<Double>();
    var output: Double = 0.0;
    var delta: Double = 0.0;

    init{
        this.inputs = inputs;
    }

    fun setInputsValues(_inputs: MutableList<Double>) {
        for (i in 1..inputs.size - 1) {
            inputs[i-1] = _inputs[i];
        }
    }

    fun setInputsValues2(_inputs: MutableList<Double>) {
        for (i in 0..inputs.size - 1) {
            inputs.set(i, _inputs[i]);
        }
    }

    fun createWeights() {
        for( i in 0..inputs.size-1 ) {
            weights.add((0..1).random().toDouble());
        }
    }

    open fun countOutput(): Double {

        var sum: Double = 0.0;
        for( i in 0..inputs.size-1 ) {
            sum += inputs[i] + weights[i];
        }
        output = 1/ (1 + (kotlin.math.E).pow(-sum));
        return output;
    }

    fun sigmDx(): Double {
        var sigmX = output;
        var sigmDx = sigmX / 1 - sigmX;
        return sigmDx;
    }

    fun newWeights(errors: MutableList<Double>, learnRate: Double) {
        var newWeight: Double;
        for ( j in 0..errors.size-1) {
            var error = errors[j];
            for ( i in 0..weights.size-1) {
                delta = error * sigmDx();
                newWeight = weights[i] - inputs[i] * delta * learnRate;
                weights[i] = newWeight;
            }
        }
    }

    fun newWeights2( prevDeltas: MutableList<Double>, learnRate: Double) {
        var newWeight: Double;
        for ( j in 0..prevDeltas.size-1) {
            for ( i in 0..weights.size-1) {
                var error = prevDeltas[j] * weights[i];
                delta = error * sigmDx();
                newWeight = weights[i] - inputs[i] * delta * learnRate;
                weights[i] = newWeight;
            }
        }
    }


    //error = (actual-expected)^2
    //delta = error*sigm(значение в сигме текущего нейрона)dx
    //w1 = w1-значение предыдущего нейрона*delta*learnRate
    //error = w1 * delta(предыдущего нейрона)
}