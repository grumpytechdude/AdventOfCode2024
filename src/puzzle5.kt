import java.io.File


data class Input(val rules: Map<Int, List<Int>>, val inputs: List<List<Int>>)

fun buildInput(sample: Boolean = true): Input {
    val lines =
        when (sample) {
            true -> File("/Users/alexsinclair/IdeaProjects/TCL/AdventOfCode2024/src/puzzle_5_sample_input.txt").readLines()
            false -> File("/Users/alexsinclair/IdeaProjects/TCL/AdventOfCode2024/src/puzzle_5_input.txt").readLines()
        }

    val rules = mutableMapOf<Int, MutableList<Int>>().withDefault { mutableListOf() }
    val inputs = mutableListOf<List<Int>>()

    lines.forEach { line ->
        if (line.contains('|')) {
            val split = line.split("|")
            val pageBefore = split[0].toInt()
            val ruleList = rules.getOrPut(pageBefore) { mutableListOf() }
            ruleList.add(split[1].toInt())
        }
        if (line.contains(',')) {
            inputs.add(line.split(",").map { it.toInt() })
        }
    }

    return Input(rules, inputs)
}


fun main() {
    val puzzleInput = buildInput(false)
    val rules = puzzleInput.rules
    val inputs: List<List<Int>> = puzzleInput.inputs

    println(puzzleOne(inputs, rules))

    val failing = inputs.filter {
        followsTheRules(it, rules).any { result -> !result }
    }

    var currentList = failing
    while (currentList.any { followsTheRules(it, rules).any { result -> !result } }) {
        currentList = currentList.map { applyTheRulesFix(it, rules) }
    }

    println(currentList.map { it[it.size / 2] }.sum())
}

private fun applyTheRulesFix(
    it: List<Int>,
    rules: Map<Int, List<Int>>
): MutableList<Int> {
    val modifiedLine = it.toMutableList()
    it.mapIndexed { index, page ->
        val applicableRules = rules[page] ?: listOf()
        val previousPagesInLine = it.slice(0..index)
        val failingRules = applicableRules.filter { rule ->
            previousPagesInLine.contains(rule)
        }
        failingRules.forEach { rule ->
            modifiedLine.remove(rule)
            val indexOf = modifiedLine.indexOf(page)
            modifiedLine.add(indexOf + 1, rule)
        }
    }
    return modifiedLine
}

private fun followsTheRules(
    it: List<Int>,
    rules: Map<Int, List<Int>>
) = it.mapIndexed { index, page ->
    val applicableRules = rules[page] ?: listOf()
    val previousPagesInLine = it.slice(0..index)
    val failingRules = applicableRules.filter { rule ->
        previousPagesInLine.contains(rule)
    }
    failingRules.isEmpty()
}

private fun puzzleOne(
    inputs: List<List<Int>>,
    rules: Map<Int, List<Int>>
): Int {
    val result = inputs.filter {
        it.mapIndexed { index, page ->
            val applicableRules = rules[page] ?: listOf()
            val previousPagesInLine = it.slice(0..index)
            val failingRules = applicableRules.filter { rule ->
                previousPagesInLine.contains(rule)
            }
            failingRules.isEmpty()
        }.all { result -> result }
    }.map { it[it.size / 2] }.sum()
    return result
}