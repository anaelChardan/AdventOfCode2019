import java.io.File

class LinesLoader {
    public fun getLines(day: Int) : List<String> {
        val file = File(LinesLoader()::class.java.getClassLoader().getResource("day${day}-input.txt").getFile())

        return file.bufferedReader().readLines()
    }
}