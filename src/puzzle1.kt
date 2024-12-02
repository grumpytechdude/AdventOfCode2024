import java.io.File
import kotlin.math.abs

data class ListHolder(val one: List<Int>, val two: List<Int>)

fun readFileLineByLineUsingForEachLine(fileName: String): ListHolder {
    val firstList = mutableListOf<Int>()
    val secondList = mutableListOf<Int>()
    File(fileName).forEachLine {
        val strings = it.split("   ")
        firstList.add(strings[0].trim().toInt())
        secondList.add(strings[1].trim().toInt())
    }
    firstList.sort()
    secondList.sort()
    return ListHolder(firstList, secondList)
}

fun getInputData(realData: Boolean): ListHolder {
    if (realData)
        return  readFileLineByLineUsingForEachLine("/Users/alexsinclair/IdeaProjects/TCL/AdventOfCode2024/src/puzzle_1_input.txt")
    return ListHolder(listOf(3, 4, 2, 1, 3, 3), listOf(4,3, 5, 3, 9, 3))
}

fun challengeOne() {
    val (firstList, secondList) = getInputData(true)
    val totalDistance = firstList.mapIndexed { index, firstValue -> abs(firstValue - secondList[index]) }.sum()
    println("Challenge one total distance: $totalDistance")
}

fun challengeTwo() {
    val (firstList, secondList) = getInputData(true)
    val secondCount = secondList.groupBy { it }.mapValues { it.value.size }

    val totalDistance = firstList.sumOf { it * (secondCount[it] ?: 0) }
    println("Challenge two total distance: $totalDistance")
}

fun main() {
    challengeOne()
    challengeTwo()
}