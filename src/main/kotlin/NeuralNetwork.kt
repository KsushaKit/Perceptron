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

    //у входных нейронов нет весов

    //функция подсчета значений в нейронах на одном слое при отсутсвиии весов
    public fun countOutputsInNeurons1(layer: Layer<out Neuron>): MutableList<Double> {

        var neurons = layer.neurons ;
        var outputs = mutableListOf<Double>();

        for( i in 0..neurons.size-1 ) {
            neurons[i].createWeights();
            outputs.add(neurons[i].countOutput());
        }
        return outputs;
    }

    //функция подсчета значений в нейронах на одном слое при присутсвии весов
    public fun countOutputsInNeurons2(layer: Layer<out Neuron>): MutableList<Double> {

        var neurons = layer.neurons ;
        var outputs = mutableListOf<Double>();

        for( i in 0..neurons.size-1 ) {
            outputs.add(neurons[i].countOutput());
        }
        return outputs;
    }

    public fun createInputLayer() {
        //заполнение входного слоя
        var listInputNeurons = mutableListOf<InputNeuron>();
        var _inputs = mutableListOf<Double>();

        for( i in 0..numberOfInputNeurons-1 ) {
            _inputs.add(0,inputs[i]);
            var inputNeuron = InputNeuron(_inputs);
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

        for( i in 0..numbersOfHiddenNeurons.size-1 ) {
            prevLayer = layers.last();
            _inputs = countOutputsInNeurons1(prevLayer);

            for( j in 0..numbersOfHiddenNeurons[i]-1 ) {
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
        var _inputs = countOutputsInNeurons1( layers.last());
        var listOutputNeurons = mutableListOf<OutputNeuron>();

        for( i in 0..numberOfOutputNeurons-1 ) {
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

        var _inputs = dataset[0];
        for (i in 1.._inputs.size-1) {
            this.inputs.add(dataset[0][i])
        }
        this.learnRate = learnRate;
        this.numberOfInputNeurons = inputs.size;
        this.numberOfOutputNeurons = numberOfOutputNeurons;
        this.numbersOfHiddenNeurons = numbersOfHiddenNeurons;
        this.dataset = dataset;

        createInputLayer();
        createHiddenLayers();
        createOutputLayer();

        return countOutputsInNeurons1(layers.last());
    }

    fun changeInputsInNeurons(prevLayer: Layer<out Neuron>, currLayer: Layer<out Neuron>): Layer<out Neuron> {
        var inputs = mutableListOf<Double>();
        var layer: Layer<out Neuron>;
        inputs = countOutputsInNeurons2(prevLayer);

        for( j in 0..currLayer.neurons.size-1 ) {
           // println("countOutputsInNeurons2(prevLayer) ${countOutputsInNeurons2(prevLayer)}")
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
        for ( i in 0..layer.neurons.size-1) {
            layer.neurons[i].setInputsValues(newInputs);
        }

        //заполнение скрытых слоев
        for ( i in 1..layers.size-1) {
            //println(i)
            changeInputsInNeurons(layers[i-1], layers[i]);
        }

        println("result of going network")
        return countOutputsInNeurons2(layers.last());
    }

//-----------------------------------------------------------------------
    //функция замены весов во всех нейронах всех слоев
    public fun learn( data: MutableList<Double> = dataset[1], flag: Int = 1) {

        println("data $data")
        //так как на вход подается строка, в которой первое число это ожидаемый результат,
        //а мне нужен список, то я его создаю и на нужное место втавляю то самое число
        var expected = mutableListOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        expected[data[0].toInt()] = data[0];
        var actual = go(data);

        //предпоследний слой
        var errors = mutableListOf<Double>();
        for ( i in 0..layers.last().neurons.size-1 ) {
            var error = (actual[i] - expected[i]).pow(2);
            errors.add(error);
        }

        var layer = layers[layers.size-2];
        for ( i in 0..layer.neurons.size-1) {
            var currNeuron = layer.neurons[i];
            currNeuron.newWeights(errors, learnRate);
        }

        //остальные слои
        for ( i in layers.size-3 downTo 0 ) {
            var prevDeltas = mutableListOf<Double>();
            for ( k in 0..layers[i+1].neurons.size-1 ) {
                var prevDelta = layers[i+1].neurons[k].delta;
                prevDeltas.add(prevDelta);
            }
            for ( j in 0..layers[i].neurons.size-1 ) {
                var currNeuron = layers[i].neurons[j];
                currNeuron.newWeights2(prevDeltas, learnRate);
            }
        }
        //error = (actual-expected)^2
        //delta = error*sigm(значение в сигме текущего нейрона)dx
        //w1 = w1-значение предыдущего нейрона*delta*learnRate

        println("expected $expected");
        println("actual $actual");
        //doing рекурсию, когда строки в датасет закончатся, тогда функция и успокоится
        if ( flag<dataset.size-2 ) {
            learn(dataset[flag], flag+1);
        };

    }

}