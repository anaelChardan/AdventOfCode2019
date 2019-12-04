class Day1(val lines: List<String>): Day<Int, Int> {
    override fun part1(): Int = lines.sumBy { fuelRequirement(it.toInt()) }
    override fun part2(): Int = lines.sumBy { fuelsRequirement(it.toInt()) }

    private fun fuelsRequirement(mass: Int): Int =
        generateSequence(fuelRequirement(mass), ::fuelRequirement)
            .takeWhile { it > 0 }
            .sum()

    private fun fuelRequirement(mass: Int) : Int = (mass / 3 - 2)
}