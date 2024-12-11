import GuardDirection.*
import java.io.File
import kotlin.math.abs

class InfiniteLoopException : Exception()

fun buildInputArray(sample: Boolean): List<List<String>> {
    val filePath = when (sample) {
        true -> "/Users/alexsinclair/IdeaProjects/TCL/AdventOfCode2024/src/puzzle_6_sample_input.txt"
        false -> "/Users/alexsinclair/IdeaProjects/TCL/AdventOfCode2024/src/puzzle_6_input.txt"
    }
    val readLines = File(filePath).readLines()
    val allCharacters: List<List<String>> = readLines.map { it.split("").filter { it !== "" } }
    return allCharacters
}

val guard = Regex("([\\^><v])")

enum class GuardDirection(val character: String) {
    UP("^"), DOWN("V"), LEFT("<"), RIGHT(">"), OFF_THE_MAP("?");

    companion object {
        fun fromCharacter(character: String): GuardDirection? {
            return entries.find { it.character == character }
        }
    }

    fun turn90Degrees() = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
        OFF_THE_MAP -> OFF_THE_MAP
    }
}

fun main() {
    val input = buildInputArray(false)

    val allCells = mutableSetOf<Cell>()
    val possibleObstructionLocations = input.mapIndexed { rowIndex, row -> row.mapIndexed { columnIndex, _ -> Cell(rowIndex, columnIndex)}}.flatten()

    val startingPosition = input.find(guard) ?: throw IllegalArgumentException("Can't find starting position")
    input.printPosition(startingPosition, possibleObstructionLocations)

    val stoppingPositions = walkPath(startingPosition, input, allCells, mutableListOf())

    println("Possible obstruction count: ${possibleObstructionLocations.toSet().size}")

    val viableObstructionLocations = possibleObstructionLocations.toSet().filter {
        println()
        println("Possible Obstruction: $it")
        val newInput = input.map {row -> row.toMutableList()}
        newInput[it.first][it.second] = "#"
        val result = walkPath(startingPosition, newInput).isEmpty()
        println("Possible Obstruction: $it. Result $result")
        result
    }

//    println(stoppingPositions)

//    println(allCells.size)
    println(viableObstructionLocations.toSet().size)

}

private fun walkPath(
    startingPosition: Position,
    input: List<List<String>>,
    allCells: MutableSet<Cell> = mutableSetOf(),
    possibleObstructionLocations: MutableList<Cell> = mutableListOf()
): List<Position> {
    var lastPosition = startingPosition
    val stoppingPositions = mutableListOf(startingPosition)

    do {
        val onwardPath = input.onwardPath(lastPosition, allCells, possibleObstructionLocations)
        input.printPosition(onwardPath, possibleObstructionLocations)
        if (stoppingPositions.contains(onwardPath)) return listOf()
        stoppingPositions.add(onwardPath)
        lastPosition = onwardPath
    } while (onwardPath.first != OFF_THE_MAP)
    return stoppingPositions
}

typealias Position = Pair<GuardDirection?, Cell>

fun List<List<String>>.find(input: Regex): Position? {
    for (rowIndex in indices) {
        for (columnIndex in this[rowIndex].indices) {
            if (input.matches(this[rowIndex][columnIndex])) {
                val direction = input.find(this[rowIndex][columnIndex])?.value?.let { GuardDirection.fromCharacter(it) }
                return when (direction) {
                    null -> null
                    else -> direction to Cell(rowIndex, columnIndex)
                }
            }
        }
    }
    return null
}

fun List<List<String>>.onwardPath(position: Position, allCells: MutableSet<Cell>, possibleObstructionLocations: MutableList<Cell>): Position {
    return when (position.first) {
        UP -> position.second.up(this, "#", allCells, possibleObstructionLocations)
        DOWN -> position.second.down(this, "#", allCells, possibleObstructionLocations)
        LEFT -> position.second.backwards(this, "#", allCells, possibleObstructionLocations)
        RIGHT -> position.second.forward(this, "#", allCells, possibleObstructionLocations)
        OFF_THE_MAP -> TODO()
        null -> TODO()
    }
}

fun Cell.forward(inputArray: List<List<String>>, stopCharacter: String, allCells: MutableSet<Cell>, possibleObstructionLocations: MutableList<Cell>): Position {
    var index = 0

    do {
        val character = inputArray[this.first].getOrNull(this.second + index)
        when (character) {
            stopCharacter -> return RIGHT.turn90Degrees() to Cell(this.first, this.second + index - 1)
            null -> return OFF_THE_MAP to Cell(this.first, this.second + index)
            else -> {
                val position = Cell(this.first, this.second + index)
                if (index != 0 && allCells.contains(position))
                    possibleObstructionLocations.add(Cell(this.first, this.second + index + 1))
                allCells.add(position)
                index++
            }
        }

    } while (true)
}

fun Cell.backwards(inputArray: List<List<String>>, stopCharacter: String, allCells: MutableSet<Cell>, possibleObstructionLocations: MutableList<Cell>): Position {
    var index = 0

    do {
        val character = inputArray[this.first].getOrNull(this.second - index)
        when (character) {
            stopCharacter -> return LEFT.turn90Degrees() to Cell(this.first, this.second - index + 1)
            null -> return OFF_THE_MAP to Cell(this.first, this.second - index)
            else -> {
                val position = Cell(this.first, this.second - index)
                if (index != 0 && allCells.contains(position))
                    possibleObstructionLocations.add(Cell(this.first, this.second - index - 1))
                allCells.add(position)
                index++
            }
        }

    } while (true)
}

fun Cell.down(inputArray: List<List<String>>, stopCharacter: String, allCells: MutableSet<Cell>, possibleObstructionLocations: MutableList<Cell>): Position {
    var index = 0

    do {
        val character = inputArray.getOrNull(this.first + index)?.getOrNull(this.second)
        when (character) {
            stopCharacter -> return DOWN.turn90Degrees() to Cell(this.first + index - 1, this.second)
            null -> return OFF_THE_MAP to Cell(this.first + index, this.second)
            else -> {
                val position = Cell(this.first + index, this.second)
                if (index != 0 && allCells.contains(position)) possibleObstructionLocations.add(Cell(this.first + index + 1, this.second))
                allCells.add(position)
                index++
            }
        }

    } while (true)
}

fun Cell.up(inputArray: List<List<String>>, stopCharacter: String, allCells: MutableSet<Cell>, possibleObstructionLocations: MutableList<Cell>): Position {
    var index = 0

    do {
        val character = inputArray.getOrNull(this.first - index)?.getOrNull(this.second)
        when (character) {
            stopCharacter -> return UP.turn90Degrees() to Cell(this.first - index + 1, this.second)
            null -> return OFF_THE_MAP to Cell(this.first - index, this.second)
            else -> {
                val position = Cell(this.first - index, this.second)
                if (index != 0 && allCells.contains(position)) possibleObstructionLocations.add(Cell(this.first - index - 1, this.second))
                allCells.add(position)
                index++
            }
        }

    } while (true)
}

fun List<Position>.countSteps(): Int {
    return this.mapIndexed { index, (_, cell) ->
        if (index == 0) return@mapIndexed 0
        val previousCell = this[index - 1].second
        val result = abs(previousCell.first - cell.first) + abs(previousCell.second - cell.second)
        println("$previousCell to $cell: $result")
        return@mapIndexed result
    }.sum()
}

fun List<List<String>>.printPosition(position: Position, possibleObstructionLocations: List<Cell>) {
    return
    println("---")
    for (rowIndex in this.indices) {
        for (columnIndex in this[rowIndex].indices) {
            val (row, column) = position.second

            if (rowIndex == row && columnIndex == column) {
                print("*")
            } else if (possibleObstructionLocations.contains(Cell(rowIndex, columnIndex))) {
                print("O")
            } else {
                print(this[rowIndex][columnIndex])
            }
        }
        println()
    }
    println("---")
}