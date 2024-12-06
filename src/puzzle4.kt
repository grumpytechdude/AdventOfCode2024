import java.io.File

fun buildInputArray(): List<List<String>> {
    val readLines = File("/Users/alexsinclair/IdeaProjects/TCL/AdventOfCode2024/src/puzzle_4_input.txt").readLines()
    val allCharacters: List<List<String>> = readLines.map { it.split("").filter { it !== "" } }
    return allCharacters
}

typealias Cell = Pair<Int, Int>
typealias Row = List<Cell>
typealias Grid = List<Row>

private const val SEARCH_TERM = "MAS"

private const val SAMPLE_INPUT = """
MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX
"""

fun main() {
    val inputArray: List<List<String>> = buildInputArray()

    val maxRows = inputArray.size
    val maxColumns = inputArray.maxOfOrNull { it.size } ?: 0

    val coordinates = mutableListOf<Cell>()
    for (rowIndex in 0 until maxRows) {
        for (columnIndex in 0 until maxColumns) {
            coordinates.add(Cell(rowIndex, columnIndex))
        }
    }

//    println(coordinates)

//    val result = coordinates.map { point ->
//        point to listOf(
//            point.forward(inputArray, SEARCH_TERM.length) to point.forward(inputArray, SEARCH_TERM.length).containsXmas(),
//            point.backwards(inputArray, SEARCH_TERM.length) to point.backwards(inputArray, SEARCH_TERM.length).containsXmas(),
//            point.down(inputArray, SEARCH_TERM.length) to point.down(inputArray, SEARCH_TERM.length).containsXmas(),
//            point.up(inputArray, SEARCH_TERM.length) to point.up(inputArray, SEARCH_TERM.length).containsXmas(),
//        )
//    }

//    puzzleOne(coordinates, inputArray)

    val result = coordinates.map { point ->
        listOf(
            point.slopeFromTopLeftToBottomRight(inputArray, SEARCH_TERM.length).containsXmas() == true ||
            point.slopeFromTopLeftToBottomRight(inputArray, SEARCH_TERM.length)?.reversed().containsXmas() == true,
            point.slopeFromBottomLeftToTopRight(inputArray, SEARCH_TERM.length).containsXmas()  == true ||
            point.slopeFromBottomLeftToTopRight(inputArray, SEARCH_TERM.length)?.reversed().containsXmas() == true,
        ).all { it }
    }.count {it}

    println(result)
}

private fun puzzleOne(
    coordinates: MutableList<Cell>,
    inputArray: List<List<String>>
) {
    val result = coordinates.map { point ->
        listOfNotNull(
            point.forward(inputArray, SEARCH_TERM.length).containsXmas(),
            point.backwards(inputArray, SEARCH_TERM.length).containsXmas(),
            point.down(inputArray, SEARCH_TERM.length).containsXmas(),
            point.up(inputArray, SEARCH_TERM.length).containsXmas(),
            point.forwardDown(inputArray, SEARCH_TERM.length).containsXmas(),
            point.backwardsDown(inputArray, SEARCH_TERM.length).containsXmas(),
            point.forwardsUp(inputArray, SEARCH_TERM.length).containsXmas(),
            point.backwardsUp(inputArray, SEARCH_TERM.length).containsXmas(),
        ).count { it }
    }.sum()

    println(result)
}

fun String?.containsXmas(): Boolean? {
    if (this == null) return null
    return contains(SEARCH_TERM)
}

fun Cell.toLetter(inputArray: List<List<String>>): String {
    return inputArray[this.first][this.second]
}

fun Cell.forward(inputArray: List<List<String>>, length: Int): String? {
    if (this.second + length - 1 > inputArray.lastIndex) return null
    return (0..<length).joinToString("") { i -> inputArray[this.first][this.second + i] }
}

fun Cell.backwards(inputArray: List<List<String>>, length: Int): String? {
    if (this.second - length + 1 < 0) return null
    return (0..<length).joinToString("") { i -> inputArray[this.first][this.second - i] }
}

fun Cell.down(inputArray: List<List<String>>, length: Int): String? {
    if (this.first + length - 1 > inputArray[this.second].lastIndex) return null
    return (0..<length).joinToString("") { i -> inputArray[this.first + i][this.second] }
}

fun Cell.up(inputArray: List<List<String>>, length: Int): String? {
    if (this.first - length + 1 < 0) return null
    return (0..<length).joinToString("") { i -> inputArray[this.first - i][this.second] }
}

fun Cell.forwardDown(inputArray: List<List<String>>, length: Int): String? {
    if (this.second + length - 1 > inputArray.lastIndex) return null
    if (this.first + length - 1 > inputArray[this.second].lastIndex) return null
    return (0..<length).joinToString("") { i -> inputArray[this.first + i][this.second + i] }
}

fun Cell.backwardsDown(inputArray: List<List<String>>, length: Int): String? {
    if (this.second - length + 1 < 0) return null
    if (this.first + length - 1 > inputArray[this.second].lastIndex) return null
    return (0..<length).joinToString("") { i -> inputArray[this.first + i][this.second - i] }
}

fun Cell.forwardsUp(inputArray: List<List<String>>, length: Int): String? {
    if (this.second + length - 1 > inputArray.lastIndex) return null
    if (this.first - length + 1 < 0) return null
    return (0..<length).joinToString("") { i -> inputArray[this.first - i][this.second + i] }
}

fun Cell.backwardsUp(inputArray: List<List<String>>, length: Int): String? {
    if (this.first - length + 1 < 0) return null
    if (this.second - length + 1 < 0) return null
    return (0..<length).joinToString("") { i -> inputArray[this.first - i][this.second - i] }
}

fun Cell.slopeFromTopLeftToBottomRight(inputArray: List<List<String>>, length: Int): String? {
    val minimumIndex = length / -2
    val maximumIndex = length / 2
    if (this.second + maximumIndex > inputArray.lastIndex) return null
    if (this.first + maximumIndex > inputArray[this.second].lastIndex) return null
    if (this.first + minimumIndex < 0) return null
    if (this.second + minimumIndex < 0) return null
    return (minimumIndex..maximumIndex).joinToString("") { i -> inputArray[this.first + i][this.second + i] }
}

fun Cell.slopeFromBottomLeftToTopRight(inputArray: List<List<String>>, length: Int): String? {
    val minimumIndex = length / -2
    val maximumIndex = length / 2
    if (this.first + maximumIndex > inputArray.lastIndex) return null
    if (this.second + maximumIndex > inputArray[this.second].lastIndex) return null
    if (this.second + minimumIndex < 0) return null
    if (this.first + minimumIndex < 0) return null
    val list = mutableListOf<String>()
    for (i in minimumIndex..maximumIndex) {
        for (j in (minimumIndex..maximumIndex).reversed()) {
            if (i + j == 0)
                list.add(inputArray[this.first + j][this.second + i])
        }
    }
    return list.joinToString("")
}

//fun List<List<String>>.rowsContainXmas(): List<Boolean> {
//    return this.map { it -> it.joinToString("").containsXmas() }
//}
//
//fun List<List<String>>.rowsContainXmasReverse(): List<Boolean> {
//    return this.map { it -> it.reversed().joinToString("").containsXmas() }
//}
//
//fun List<List<String>>.columnsContainXmas(): List<Boolean> {
//    val columnStrings = getColumnStrings()
//    return columnStrings.map { it -> it.containsXmas() }
//}
//
//fun List<List<String>>.columnsContainXmasReverse(): List<Boolean> {
//    val columnStrings = getColumnStrings()
//    return columnStrings.map { it -> it.reversed().containsXmas() }
//}
//
//fun List<List<String>>.diagonalsContainXmas(): List<Boolean> {
//    val diagonalStrings = getDiagonalStrings()
//    return diagonalStrings.map { it -> it.containsXmas() }
//}
//
//fun List<List<String>>.diagonalsContainXmasReverse(): List<Boolean> {
//    val diagonalStrings = getDiagonalStrings()
//    return diagonalStrings.map { it -> it.reversed().containsXmas() }
//}

//fun List<List<String>>.getColumnStrings(): List<String> {
//    val columnStrings = MutableList(this.map { it -> it.size }.max()) { "" }
//    for (row in this) {
//        for ((index, column) in row.withIndex()) {
//            columnStrings[index] += column
//        }
//    }
//    return columnStrings
//}
//
//fun List<List<String>>.getDiagonalStrings(): List<String> {
//    val diagonalStrings = mutableListOf("", "")
//    for ((rowIndex, row) in this.withIndex()) {
//        for ((columnIndex, column) in row.withIndex()) {
//            if (rowIndex === columnIndex) {
//                diagonalStrings[0] += column
//            }
//            if (rowIndex + columnIndex === this.size - 1) {
//                diagonalStrings[1] += column
//            }
//        }
//    }
//    return diagonalStrings
//}
