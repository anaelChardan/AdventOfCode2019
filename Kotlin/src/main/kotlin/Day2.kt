class Day2(val lines: List<String>): Day<Int, Int> {
    val linesInt = lines[0].split(",").map { it.toInt() }

    override fun part1(): Int
    {
        val res = runByReplacing(linesInt, 12, 2)

        return res[0]
    }

    override fun part2(): Int
    {
        val input = lines[0].split(",").map { it.toInt() }
        for(noun in 0 .. 99) {
            for(verb in 0..99) {
                val res = runByReplacing(input, noun, verb)
                if (res[0] == 19690720) {
                    return 100 * noun + verb
                }
            }
        }
        error("No matching solution")
    }

    private fun runByReplacing(lines: List<Int>, noun: Int, verb: Int): List<Int> {
        val tmp = lines.toMutableList()
        tmp[1] = noun;
        tmp[2] = verb;

        return IntCodeComputer(tmp).run(emptyList()).values
    }
}


