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
    println(csvChecker.read(1));
   // println(csvChecker.read(2));
   // println(csvChecker.read(3));
   // println(csvChecker.read(8));

    var neuralNetwork = NeuralNetwork();
    var numbersOfHiddenNeurons: Array<Int> = arrayOf(12,11);

    //println(csvChecker.getNumbers());

    println("the first string for dataset - input data for create network");
    println(csvChecker.getNumbers()[0])
    println("result of creating network");
    println(neuralNetwork.create( 10, numbersOfHiddenNeurons, csvChecker.getNumbers(), 0.4));

    println(neuralNetwork.go(csvChecker.getNumbers()[112]));

    neuralNetwork.learn();




}