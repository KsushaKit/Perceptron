fun main(args: Array<String>) {

//----------------------------------------------------------------------
    //тут я пробую запускать нейросеть без обучения

    //var inputs = mutableListOf<Double>(1.1,1.2,1.3);
    //var numbersOfHiddenNeurons: Array<Int> = arrayOf(3,4);
    //var neuralNetwork = NeuralNetwork();

   // neuralNetwork.create(inputs,3,numbersOfHiddenNeurons);
   // println(neuralNetwork.go());
    //println("hi")

//-----------------------------------------------------------------------
    //тут я пробую запускать нейросеть с обучением

    var fileName = "src/dataset/mnist_train.csv";
    var csvChecker = CsvChecker(fileName);
    //println(csvChecker.read(1));



    //println(csvChecker.getNumbers());

    //println("the first string for dataset - input data for create network");
    //println(csvChecker.getNumbers()[0])
    //println("result of creating network");


    //println(neuralNetwork.go(csvChecker.getNumbers()[112]));

    //neuralNetwork.learn();


    var inputNumbers = mutableListOf(
        mutableListOf(
            0.0,
            1.0, 1.0, 1.0,
            1.0, 0.0, 1.0,
            1.0, 0.0, 1.0,
            1.0, 0.0, 1.0,
            1.0, 1.0, 1.0
        ), //0
        mutableListOf(
            1.0,
            0.0, 1.0, 0.0,
            0.0, 1.0, 0.0,
            0.0, 1.0, 0.0,
            0.0, 1.0, 0.0,
            0.0, 1.0, 0.0
        ), //1
        mutableListOf(
            2.0,
            1.0, 1.0, 1.0,
            0.0, 0.0, 1.0,
            1.0, 1.0, 1.0,
            1.0, 0.0, 0.0,
            1.0, 1.0, 1.0
        ), //2
        mutableListOf(
            3.0,
            1.0, 1.0, 1.0,
            0.0, 0.0, 1.0,
            1.0, 1.0, 1.0,
            0.0, 0.0, 1.0,
            1.0, 1.0, 1.0
        ), //3
        mutableListOf(
            4.0,
            1.0, 0.0, 1.0,
            1.0, 0.0, 1.0,
            1.0, 1.0, 1.0,
            0.0, 0.0, 1.0,
            0.0, 0.0, 1.0
        ), //4
        mutableListOf(
            5.0,
            1.0, 1.0, 1.0,
            1.0, 0.0, 0.0,
            1.0, 1.0, 1.0,
            0.0, 0.0, 1.0,
            1.0, 1.0, 1.0
        ), //5
        mutableListOf(
            6.0,
            1.0, 1.0, 1.0,
            1.0, 0.0, 0.0,
            1.0, 1.0, 1.0,
            1.0, 0.0, 1.0,
            1.0, 1.0, 1.0
        ), //6
        mutableListOf(
            7.0,
            1.0, 1.0, 1.0,
            0.0, 0.0, 1.0,
            0.0, 0.0, 1.0,
            0.0, 0.0, 1.0,
            0.0, 0.0, 1.0
        ), //7
        mutableListOf(
            8.0,
            1.0, 1.0, 1.0,
            1.0, 0.0, 1.0,
            1.0, 1.0, 1.0,
            1.0, 0.0, 1.0,
            1.0, 1.0, 1.0
        ), //8
        mutableListOf(
            9.0,
            1.0, 1.0, 1.0,
            1.0, 0.0, 1.0,
            1.0, 1.0, 1.0,
            0.0, 0.0, 1.0,
            1.0, 1.0, 1.0
        ), //9
    )

    var inputNumbers2 = mutableListOf(
        mutableListOf(
            0.0,
            0.0, 0.0
        ), //0
        mutableListOf(
            1.0,
            0.0, 1.0
        ), //1
        mutableListOf(
            1.0,
            1.0, 0.0
        ), //2
        mutableListOf(
            0.0,
            1.0, 1.0
        ) //3
    )

    /*
    var neuralNetwork = NeuralNetwork();
    var numbersOfHiddenNeurons: Array<Int> = arrayOf(3,2);
    println(neuralNetwork.create( 1, numbersOfHiddenNeurons, inputNumbers2, 0.7));
    println();

    for ( k in 0..10000) {
        for ( i in 0..inputNumbers2.size-1) {
            neuralNetwork.learn(inputNumbers2[i]);
        }
    }


    println("TEST 1, 0.0");
    println(neuralNetwork.go(mutableListOf(1.0, 1.0)));
    println("TEST 2, 0.0");
    println(neuralNetwork.go(mutableListOf(0.0, 0.0)));
    println("TEST 3, 1.0");
    println(neuralNetwork.go(mutableListOf(1.0, 0.0)));

    println("TEST 1, 0.0");
    neuralNetwork2.learn(inputNumbers2[0]);
    println("TEST 2, 1.0");
    neuralNetwork2.learn(inputNumbers2[1]);
    println("TEST 3, 1.0");
    neuralNetwork2.learn(inputNumbers2[2]);
    println("TEST 4, 0.0");
    neuralNetwork2.learn(inputNumbers2[3]);
    */

    var neuralNetwork2 = NeuralNetwork();
    var numbersOfHiddenNeurons2: Array<Int> = arrayOf(3,2);
    println(neuralNetwork2.create( 1, numbersOfHiddenNeurons2, inputNumbers2, 0.89));
    println();

    for ( k in 0..100000) {
        for ( i in 0..inputNumbers2.size-1) {
            neuralNetwork2.learn(inputNumbers2[i]);
        }
    }


    println("TEST 1, 0.0");
    println(neuralNetwork2.learn(mutableListOf(1.0, 1.0)));
    println("TEST 2, 0.0");
    println(neuralNetwork2.learn(mutableListOf(0.0, 0.0)));
    println("TEST 3, 1.0");
    println(neuralNetwork2.learn(mutableListOf(1.0, 0.0)));
}