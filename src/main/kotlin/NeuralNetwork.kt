import Layers.HiddenLayer
import Layers.InputLayer
import Layers.Layer
import Layers.OutputLayer
import Neurons.HiddenNeuron
import Neurons.InputNeuron
import Neurons.Neuron
import kotlin.math.pow

import Neurons.OutputNeuron

class NeuralNetwork {
    var numberOfInputNeurons: Int = 0
    var numberOfOutputNeurons: Int = 0
    var numbersOfHiddenNeurons = emptyArray<Int>();
    var inputs = mutableListOf<Double>();
    var layers = mutableListOf<Layer<out Neuron>>();
    var dataset = mutableListOf<MutableList<Double>>();
    var learnRate: Double = 0.0;

    //входные нейроны не хранят веса

    //функция подсчета значений в нейронах на одном слое при отсутсвиии весов
    public fun countOutputsInNeurons1(layer: Layer<out Neuron>): MutableList<Double> {

        var neurons = layer.neurons;
        var outputs = mutableListOf<Double>();

        for (i in 0..neurons.size - 1) {
            neurons[i].createWeights();
            outputs.add(neurons[i].countOutput());
        }
        return outputs;
    }

    //функция подсчета значений в нейронах на одном слое при присутсвии весов
    public fun countOutputsInNeurons2(layer: Layer<out Neuron>): MutableList<Double> {

        var neurons = layer.neurons;
        var outputs = mutableListOf<Double>();

        for (i in 0..neurons.size - 1) {
            outputs.add(neurons[i].countOutput());
        }
        return outputs;
    }

    public fun createInputLayer() {
        //заполнение входного слоя
        var listInputNeurons = mutableListOf<InputNeuron>();

        for (i in 0..numberOfInputNeurons - 1) {
            var inputNeuron = InputNeuron(inputs);
            listInputNeurons.add(inputNeuron);
        }
        var inputLayer = InputLayer(listInputNeurons);
        layers.add(inputLayer);
    }

    public fun createHiddenLayers() {
        //заполнение скрытых слоев
        var prevLayer: Layer<out Neuron>;
        var listHiddenNeurons = mutableListOf<HiddenNeuron>();
        var _inputs = mutableListOf<Double>();

        for (i in 0..numbersOfHiddenNeurons.size - 1) {
            prevLayer = layers.last();
            _inputs = countOutputsInNeurons1(prevLayer);

            for (j in 0..numbersOfHiddenNeurons[i] - 1) {
                var hiddenNeuron = HiddenNeuron(_inputs);
                listHiddenNeurons.add(hiddenNeuron);
            }

            var hiddenLayer = HiddenLayer(listHiddenNeurons);
            layers.add(hiddenLayer);
            //тут должна происходить очистка листа
            //listHiddenNeurons.clear();
            //тут происходит странная очистка листа
            listHiddenNeurons = mutableListOf<HiddenNeuron>();
        }
    }

    public fun createOutputLayer() {
        //заполнение выходного слоя
        //количество входов в каждый нейрон
        var prevLayer: Layer<out Neuron>;
        var _inputs = countOutputsInNeurons1(layers.last());
        var listOutputNeurons = mutableListOf<OutputNeuron>();

        for (i in 0..numberOfOutputNeurons - 1) {
            var outputNeuron = OutputNeuron(_inputs);
            listOutputNeurons.add(outputNeuron);
        }
        var outputLayer = OutputLayer(listOutputNeurons);
        layers.add(outputLayer);
    }

    //создание нейросети
    public fun create(
        numberOfOutputNeurons: Int, numbersOfHiddenNeurons: Array<Int>,
        dataset: MutableList<MutableList<Double>>, learnRate: Double
    ): MutableList<Double> {

        this.dataset = dataset;
        var _inputs = dataset[0];
        //первое число не считается тк это результат
        for (i in 1.._inputs.size - 1) {
            this.inputs.add(_inputs[i])
        }
        this.learnRate = learnRate;
        this.numberOfInputNeurons = inputs.size;
        this.numberOfOutputNeurons = numberOfOutputNeurons;
        this.numbersOfHiddenNeurons = numbersOfHiddenNeurons;


        createInputLayer();
        createHiddenLayers();
        createOutputLayer();

        println("network create");
        return countOutputsInNeurons1(layers.last());
    }

    fun changeInputsInNeurons(prevLayer: Layer<out Neuron>, currLayer: Layer<out Neuron>): Layer<out Neuron> {
        for (j in 0..currLayer.neurons.size - 1) {
            var outputs = countOutputsInNeurons2(prevLayer);
            currLayer.neurons[j].inputs.clear();
            currLayer.neurons[j].inputs.addAll(outputs);
        }
        return currLayer;
    }

    //the function to pass inputs through layers
    fun go(newInputs: MutableList<Double>): MutableList<Double> {

        //filling input layer
        var layer = layers[0];
        for (i in 0..layer.neurons.size - 1) {
            layer.neurons[i].inputs.clear();
            layer.neurons[i].inputs.addAll(newInputs);
        }

        //заполнение скрытых слоев
        for (i in 1..layers.size - 1) {
            //println(i)
            changeInputsInNeurons(layers[i - 1], layers[i]);
        }

        println("result of going network")
        return countOutputsInNeurons2(layers.last());
    }

    //-----------------------------------------------------------------------
    //функция обучения сети на датасете
    public fun learn(data: MutableList<Double>) {

        println("data $data")
        //так как на вход подается строка, в которой первое число это ожидаемый результат,
        //а мне нужен список, то я его создаю и на нужное место втавляю то самое число
        var expected = mutableListOf(data[0]);
        //expected[data[0].toInt()] = 1.0;
        var data2 = data.toMutableList();
        data2.removeAt(0);
        var actual = go(data2);


        //массив разности выходных и ожидаемых результатов
        var differences = mutableListOf<Double>();
        for (i in 0..expected.size - 1) {
            var difference = expected[i] - actual[i];
            differences.add(difference);
        }

        //функция получения errors for neurons in prevLayer
        fun getErrorsInNeurons(layer: Layer<out Neuron>): MutableList<Double> {
            var errors = mutableListOf<Double>();
            for (i in 0..layer.neurons.size - 1) {
                errors.add(layer.neurons[i].error);
            }
            return errors;
        }


        // 1-----------------------------------------------------
        //вычисление ошибки для каждого нейрона в выходном слое
        var neuronsArr = layers.last().neurons;
        for (i in 0..layers.last().neurons.size - 1) {
            neuronsArr[i].countError(differences[i]);
        }

        // 2-----------------------------------------------------
        //вычисление ошибки для каждого нейрона в остальных слоях
        for (i in layers.size - 2 downTo 1) {
            var currLayer = layers[i];
            var prevLayer = layers[i + 1];
            var errors = getErrorsInNeurons(prevLayer);
            var neurons = prevLayer.neurons;
            for (k in 0..currLayer.neurons.size - 1) {
                var currNeuron = currLayer.neurons[k];
                currNeuron.countError2(errors, neurons);
            }
        }

        // 3-----------------------------------------------------
        //вычисление новых весов и замена старых
        for (i in layers.size - 1 downTo 1) {
            var currLayer = layers[i];
            for (k in 0..currLayer.neurons.size - 1) {
                var currNeuron = currLayer.neurons[k];
                currNeuron.newWeights(learnRate);
                //println("weights")
                //println(currNeuron.weights.toString())
            }
        }

        var afterChange = go(data2);
        println("expected $expected");
        println("actual ${afterChange}");
        println();
        //error = (actual-expected)^2
        //delta = error*sigm(значение в сигме текущего нейрона)dx
        //w1 = w1-значение предыдущего нейрона*delta*learnRate


        //doing рекурсию, когда строки в датасет закончатся, тогда функция и успокоится
        /*
    if ( flag < dataset.size ) {
            learn(dataset[flag], flag+1);
        };
    */

    }

    fun feedforward(newInputs: MutableList<Double>) {
        var afterChange = go(newInputs);
        println(afterChange);
    }
}