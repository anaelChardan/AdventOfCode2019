import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val linesLoader = LinesLoader()
    listOf(
        Day1(linesLoader.getLines(1)),
        Day2(linesLoader.getLines(2)),
        Day3(linesLoader.getLines(3)),
        Day4(),
        Day5(linesLoader.getLines(5))
    ).forEachIndexed { index: Int, elem: Day<Int, Int> -> day(elem, index+1) }
}

fun day(day: Day<Int, Int>, dayNb: Int) {
    val part1Duration = measureTimeMillis { day.part1() }
    val part2Duration = measureTimeMillis { day.part2() }
    println("Day $dayNb part 1 : ${day.part1()}, duration $part1Duration ms")
    println("Day $dayNb part 2 : ${day.part2()}, duration $part2Duration ms")
}


