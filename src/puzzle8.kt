import java.io.File
import java.math.BigInteger
import kotlin.reflect.KFunction2

private fun readInput(sample: Boolean): List<List<String>> {
    val samplePath = "C:\\Users\\alexd\\IdeaProjects\\AdventOfCode2024\\src\\puzzle_8_sample_input.txt"
    val realPath = "C:\\Users\\alexd\\IdeaProjects\\AdventOfCode2024\\src\\puzzle_8_input.txt"
    val path = if (sample) samplePath else realPath
    return File(path).readLines().map { row ->
        row.split("").filter { it != ""}
    }
}

fun main() {
    val input = readInput(true)

    val letters = groupNodes(input)

    // get diff
    // do again to get antinodes
//    letters.map { (letter, points) -> points.}
    println(letters)
}

private fun groupNodes(input: List<List<String>>): MutableMap<String, MutableList<Cell>> {
    val letters = mutableMapOf<String, MutableList<Cell>>()
    input.forEachIndexed { rowIndex, row ->
        row.forEachIndexed row@{ columnIndex, letter ->
            if (letter == ".") return@row
            val points = letters.getOrPut(letter, { mutableListOf() })
            points.add(Cell(rowIndex, columnIndex))
        }
    }
    return letters
}
