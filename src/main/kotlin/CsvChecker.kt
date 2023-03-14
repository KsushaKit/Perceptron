import java.io.File

class CsvChecker(fileName: String) {
    lateinit var fileName: String;
    lateinit var file: File;
    var lines = mutableListOf<MutableList<Double>>();

    init{
        this.fileName = fileName;
    }

    public fun read(era:Int): String {

        file = File(fileName);
        //the bufferReader takes the file and reads each line from the File section
        val bufferReader = file.bufferedReader();
        //this is continuation with bufferReader process and reading each line from the file as a String
        var text: List<String> = bufferReader.readLines();

        //зачем вот это все? потому что переполняется память в тупой джаве
        val start = when (era) {
            1 -> 5
            2 -> 10001
            3 -> 20001
            4 -> 30001
            5 -> 40001
            6 -> 50001
            else -> {
                return "check number of era";
            }
        }

        var end = when (era) {
            1 -> 10000
            2 -> 20000
            3 -> 30000
            4 -> 40000
            5 -> 50000
            6 -> 60000
            else -> {
                return "check number of era";
            }
        }

        for ( i in start..end) {
            var line = text[i].split(",");
            lines.add(line.map { it.toDouble() } as MutableList<Double>);
        }
        return "csv read";
    }

    public fun getNumbers(): MutableList<MutableList<Double>> {
        return lines;
    }
}