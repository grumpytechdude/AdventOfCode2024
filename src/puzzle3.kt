import java.io.File

private val PUZZLE_ONE_SAMPLE_INPUT = listOf("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")
private val PUZZLE_TWO_SAMPLE_INPUT = listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")


fun main() {
    val realInput = File("/Users/alexsinclair/IdeaProjects/TCL/AdventOfCode2024/src/puzzle_3_input.txt").readLines()
    val input = realInput
    puzzleOne(PUZZLE_ONE_SAMPLE_INPUT)

    val patternRegex = Regex("(mul\\(\\d?\\d?\\d?,\\d?\\d?\\d?\\)|do\\(\\)|don't\\(\\))")
    val numberRegex = Regex("(\\d+)")
    var isActive = true
    val results = input.map {
        patternRegex.findAll(it).map sum@{ match ->
            if (match.value == "do()") {
                isActive = true
                return@sum 0
            }
            if (match.value == "don't()") {
                isActive = false
                return@sum 0
            }

            if (!isActive) return@sum 0

            val numbers = numberRegex.findAll(match.value)
            numbers.first().value.toInt() * numbers.last().value.toInt()
        }
    }
    println(results.map { it.sum() }.sum())
}

private fun puzzleOne(input: List<String>) {
    val patternRegex = Regex("(mul\\(\\d?\\d?\\d?,\\d?\\d?\\d?\\))")
    val numberRegex = Regex("(\\d+)")
    val results = input.map {
        patternRegex.findAll(it).map { match ->
            val numbers = numberRegex.findAll(match.value)
            numbers.first().value.toInt() * numbers.last().value.toInt()
        }
    }
    println(results.map { it.sum() }.sum())
}