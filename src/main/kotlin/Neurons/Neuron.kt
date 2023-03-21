package Neurons

import java.lang.Math.E
import kotlin.math.pow
import kotlin.random.Random

open class Neuron (inputs:  MutableList<Double>) {

    open var inputs = mutableListOf<Double>();
    var weights = mutableListOf<Double>();
    var output: Double = 0.0;
    var error: Double = 0.0;

    init{
        this.inputs = inputs;
    }

    fun createWeights() {
        for( i in 0..inputs.size-1 ) {
            weights.add((0..1).random().toDouble());
        }
    }

    open fun countOutput(): Double {
        var sum: Double = 0.0;
        for( i in 0..inputs.size-1 ) {
            sum += inputs[i] * weights[i];
        }
        output = 1 / (1 + (kotlin.math.E).pow(-sum));
        return output;
    }

    fun sigmDx(): Double {
        var sigmX = output;
        println("output")
        println(output)
        var sigmDx = sigmX / (1 - sigmX);
        return sigmDx;
    }

    //вычисление ошибки для выходных нейронов
    fun countError(  difference: Double ) {
        error = sigmDx() * difference;
    }

    //вычисление ошибки для остальных нейронов
    fun countError2( errors: MutableList<Double>, neurons: MutableList<out Neuron> ) {
        var errorSum = 0.0;
        for ( i in 0..neurons.size-1) {
            errorSum += errors[i] * neurons[i].weights[i];
        }
        error = errorSum * sigmDx();
    }

    //w1 = w1-значение предыдущего нейрона*delta*learnRate
    //погрешность веса = learnRate * ошибку * output текущего нейрона
    fun newWeights( learnRate: Double ) {
        for ( i in 0..weights.size-1) {
            var delta = error * output *  learnRate;
            var newWeight = weights[i] - delta;
            weights[i] = newWeight;
        }
    }

    //error = (actual-expected)^2
    //delta = error*sigm(значение в сигме текущего нейрона)dx
    //w1 = w1-значение предыдущего нейрона*delta*learnRate
    //error = w1 * delta(предыдущего нейрона)
}