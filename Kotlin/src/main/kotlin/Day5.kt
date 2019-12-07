class Day5(val lines: List<String>): Day<Int, Int> {
    val linesInt = lines[0].split(",").map { it.toInt() }

    override fun part1(): Int
    {
        return IntCodeComputer(linesInt).run(listOf(1)).output.last()
    }

    override fun part2(): Int
    {
        return IntCodeComputer(linesInt).run(listOf(5)).output.last()
    }
}


