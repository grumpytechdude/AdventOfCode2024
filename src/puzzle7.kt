import java.io.File
import java.math.BigInteger
import kotlin.reflect.KFunction2

private fun readInput(sample: Boolean): Map<BigInteger, List<BigInteger>> {
    val samplePath = "C:\\Users\\alexd\\IdeaProjects\\AdventOfCode2024\\src\\puzzle_7_sample_input.txt"
    val realPath = "C:\\Users\\alexd\\IdeaProjects\\AdventOfCode2024\\src\\puzzle_7_input.txt"
    val path = if (sample) samplePath else realPath
    return File(path).readLines().map {
        val split = it.split(":")
        val key = split[0].toBigInteger()
        val value = split[1].split(" ").filter(String::isNotEmpty).map(String::toBigInteger)
        key to value
    }.toMap()
}

fun add(a: BigInteger, b: BigInteger): BigInteger = a + b

fun times(a: BigInteger, b: BigInteger): BigInteger = a * b
fun concat(a: BigInteger, b: BigInteger): BigInteger = "$a$b".toBigInteger()
typealias Operator = KFunction2<BigInteger, BigInteger, BigInteger>

val operators: List<Operator> = listOf(::add, ::times, ::concat)

fun List<Operator>.permutations(length: Int): List<List<Operator>> {
    // Base case: If length is 0, return a list with an empty list
    if (length == 0) return listOf(emptyList())

    val result = mutableListOf<List<Operator>>()

    // For each element in the list, create combinations
    forEach { element ->
        // Recursively find all combinations with the remaining length
        this.permutations(length - 1).mapTo(result) { listOf(element) + it }
    }

    return result
}

fun main() {
    val input: Map<BigInteger, List<BigInteger>> = readInput(false)
    val result: List<BigInteger> = input.map { (result, numbers) ->
        val ops = operators.permutations(numbers.size - 1)
        ops.forEach { op ->
            val total = numbers.foldIndexed(numbers[0]) { index, result, i ->
                val next = numbers.getOrNull(index + 1) ?: return@foldIndexed result
                val fn = op.getOrNull(index)
                val fnResult = fn?.invoke(result, next) ?: 0
                fnResult as BigInteger

            }
            if (total == result) return@map result
        }
        return@map BigInteger.ZERO
    }
    println(result)
    val total = result.fold(BigInteger.ZERO) { acc, bigInteger -> acc + bigInteger }
    println(total)
}
