import java.io.File
import kotlin.math.abs
import kotlin.reflect.KFunction2

class puzzle2


val sample = listOf(
    "7 6 4 2 1",
    "1 2 7 8 9",
    "9 7 6 2 1",
    "1 3 2 4 5",
    "8 6 4 4 1",
    "1 3 6 7 9",
)
val sampleResult = mapOf(
    "7 6 4 2 1" to true,
    "1 2 7 8 9" to false,
    "9 7 6 2 1" to false,
    "1 3 2 4 5" to false,
    "8 6 4 4 1" to false,
    "1 3 6 7 9" to true,
)

fun main() {
    val puzzleInput = File("/Users/alexsinclair/IdeaProjects/TCL/AdventOfCode2024/src/puzzle_2_input.txt").readLines()

    puzzleOneSolution(puzzleInput)
    puzzleTwoSolution(puzzleInput)

}

fun puzzleOneSolution(puzzleInput: List<String>) {
    val result = puzzleInput.map {
        val numbers = it.split(' ').map { value -> value.toInt() }
        val comparison = getComparatorFunction(numbers)
        numbers.foldIndexed(true) { index, result, number ->
            if (index == 0) return@foldIndexed true
            if (!result) return@foldIndexed false
            val previousNumber = numbers[index - 1]
            val matchesSequence = comparison(number, previousNumber)
            val safeDistance = isWithinSafeDistance(number, previousNumber)
            return@foldIndexed matchesSequence && safeDistance
        }
    }.count { it }
    println("Total safe: $result")
}

fun puzzleTwoSolution(puzzleInput: List<String>) {
    val inputs = puzzleInput.map { it.split(' ').map { value -> value.toInt() } }
    val safeCount = inputs.map { numbers -> }
    println("Total Safe: $safeCount")
}

private fun isSafe(
    inputs: List<List<Int>>,
): Any {
    return inputs.map { numbers ->
        val comparison = getComparatorFunction2(numbers)

        val retryInput: MutableList<List<Int>> = mutableListOf()

        val allSafe = numbers.mapIndexed { index, _ ->
            if (index == 0) return@mapIndexed true
            if (index == numbers.size - 1) return@mapIndexed true

            val (isSafe, indexToRemove) = isSafeNumber(comparison, numbers, index)
            if (isSafe) return@mapIndexed true
            val retryNumbers = numbers.toMutableList()

            if (indexToRemove != null) retryInput.add(retryNumbers)
            when (indexToRemove) {
                Direction.LEFT -> retryNumbers.removeAt(index)
                Direction.RIGHT -> retryNumbers.removeAt(index + 1)
                else -> {}
            }
            return@mapIndexed false
        }.all { it }

        if (allSafe) return true
        if (retryInput.size != 1) return false

        retryInput[0].mapIndexed { index, _ ->
            if (index == 0) return@mapIndexed true
            if (index == numbers.size - 1) return@mapIndexed true

            val (isSafe) = isSafeNumber(comparison, retryInput[0], index)
            isSafe
        }.all { it }
    }
}

private fun isSafeNumber(
    comparison: KFunction2<Int, Int, Pair<Boolean, Direction?>>,
    numbers: List<Int>,
    index: Int
): Pair<Boolean, Direction?> {
    val matchesSequence = comparison(numbers[index], numbers[index + 1])
    val safeDistance = isWithinSafeDistance2(numbers, index)
    return when {
        matchesSequence.first && safeDistance.first -> true to null
        matchesSequence.first && !safeDistance.first -> false to safeDistance.second
        !matchesSequence.first && safeDistance.first -> false to matchesSequence.second
        else -> false to null
    }
}

private fun getComparatorFunction(numbers: List<Int>) =
    if (isGreaterThan(numbers[0], numbers[1])) ::isLessThan else ::isGreaterThan

private fun getComparatorFunction2(numbers: List<Int>) =
    if (isGreaterThan(numbers[0], numbers[1])) ::isGreaterThan2 else ::isLessThan2

fun isGreaterThan(number1: Int, number2: Int): Boolean = number1 > number2
fun isLessThan(number1: Int, number2: Int): Boolean = !isGreaterThan(number1, number2)
fun isWithinSafeDistance(number1: Int, number2: Int) = abs(number1 - number2) in 1..3

fun isGreaterThan2(number1: Int, number2: Int): Pair<Boolean, Direction?> = when {
    number1 > number2 -> true to null
    number1 == number2 -> false to Direction.LEFT
    else -> false to Direction.RIGHT
}

fun isLessThan2(number1: Int, number2: Int): Pair<Boolean, Direction?> = when {
    number1 < number2 -> true to null
    number1 == number2 -> false to Direction.LEFT
    else -> false to Direction.RIGHT
}

fun isWithinSafeDistance2(numbers: List<Int>, currentIndex: Int): Pair<Boolean, Direction?> {
    return when {
        abs(numbers[currentIndex] - numbers[currentIndex + 1]) in 1..3 -> true to null
        abs(numbers[currentIndex - 1] - numbers[currentIndex + 1]) in 1..3 -> false to Direction.LEFT
        abs(numbers[currentIndex] - numbers[currentIndex + 2]) in 1..3 -> false to Direction.RIGHT
        else -> false to null
    }
}


enum class Direction {
    LEFT, RIGHT
}