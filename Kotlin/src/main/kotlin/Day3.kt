import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

data class Position(val x: Int, val y: Int, val stepsTo: Int = 0)
data class Vector (val direction: Direction, val length: Int)

enum class Direction { L, D, R, U }

fun fromString(char : Char): Direction =
    when (char) {
        'D' -> Direction.D
        'L' -> Direction.L
        'R' -> Direction.R
        'U' -> Direction.U
        else -> error("UnknownDirection")
    }


class Day3(val lines: List<String>): Day<Int, Int> {
    val firstWireDirections = lines[0].split(",").map { Vector(fromString(it[0]), it.drop(1).toInt()) }
    val secondWireDirections = lines[1].split(",").map { Vector(fromString(it[0]), it.drop(1).toInt()) }

    val startingPosition = Position(1, 1)

    val firstWire = getWire(firstWireDirections, startingPosition)
    val secondWire = getWire(secondWireDirections, startingPosition)

    override fun part1(): Int {
        val closest = firstWire.map { Position(it.x, it.y) }.intersect(secondWire.map { Position(it.x, it.y) }).map { manhattanDistance(it, startingPosition) }.min()

        return closest!!
    }

    override fun part2(): Int {
        val intersections = firstWire.map { Position(it.x, it.y) }.intersect(secondWire.map { Position(it.x, it.y) })

        val closestInStep = intersections.map { i -> firstWire.find { it.x == i.x && it.y == i.y }!!.stepsTo + secondWire.find { it.x == i.x && it.y == i.y }!!.stepsTo }.min()

        return closestInStep!!
    }

    private fun manhattanDistance(position1: Position, position2: Position): Int = position1.x.absoluteDifference(position2.x) + position1.y.absoluteDifference(position2.y)

    private fun Int.absoluteDifference(other: Int) : Int = max(this.absoluteValue, other.absoluteValue).minus(min(this.absoluteValue, other.absoluteValue))

    private fun getWire(vectors: List<Vector>, startingPosition: Position): List<Position> = vectors.fold(
        Pair(startingPosition, emptyList()), { acc: Pair<Position, List<Position>>, current: Vector ->
            val directions = getAllPosition(acc.first, current)
            Pair(directions.first, acc.second.plus(directions.second))
        }).second

    private fun getAllPosition(startingPosition: Position, vector: Vector): Pair<Position, List<Position>> =
        when (vector.direction) {
            Direction.L ->
                Pair(
                    Position(startingPosition.x - vector.length, startingPosition.y, startingPosition.stepsTo + vector.length),
                    ((startingPosition.x - 1 downTo startingPosition.x - vector.length))
                        .map { Position(it, startingPosition.y, startingPosition.stepsTo + it.absoluteDifference(startingPosition.x)) }
                )
            Direction.R ->
                Pair(
                    Position(startingPosition.x + vector.length, startingPosition.y, startingPosition.stepsTo + vector.length),
                    ((startingPosition.x + 1) ..  (startingPosition.x + vector.length))
                        .map { Position(it, startingPosition.y, startingPosition.stepsTo + it.absoluteDifference(startingPosition.x)) }
                )
            Direction.U ->
                Pair(
                    Position(startingPosition.x, startingPosition.y + vector.length, startingPosition.stepsTo + vector.length),
                    ((startingPosition.y + 1) ..  (startingPosition.y + vector.length))
                        .map { Position(startingPosition.x, it, startingPosition.stepsTo + it.absoluteDifference(startingPosition.y)) }
                )
            Direction.D ->
                Pair(
                    Position(startingPosition.x, startingPosition.y - vector.length, startingPosition.stepsTo + vector.length),
                    ((startingPosition.y - 1) downTo startingPosition.y - vector.length)
                        .map { Position(startingPosition.x, it, startingPosition.stepsTo + it.absoluteDifference(startingPosition.y)) }
            )
        }
}