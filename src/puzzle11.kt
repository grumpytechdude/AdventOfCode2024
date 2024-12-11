import java.io.*
import java.math.BigInteger

val puzzle11Input: List<BigInteger> = listOf(
    475449.toBigInteger(),
    2599064.toBigInteger(),
    213.toBigInteger(),
    0.toBigInteger(),
    2.toBigInteger(),
    65.toBigInteger(),
    5755.toBigInteger(),
    51149.toBigInteger()
)

fun main() {
    val initialFile = File("tmp/input-1.bin")
    DataOutputStream(BufferedOutputStream(initialFile.outputStream())).use { output ->
        puzzle11Input.forEach { number -> output.writeUTF(number.toString()) }
    }

    val result = repeat( 75, ::process)

    println(result)
}

fun repeat(times: Int, blink: (BigInteger) -> List<BigInteger>): Int {
    for (i in 1..times) {
        val inputFile = File("tmp/input-$i.bin") // Binary input file
        val outputFile = File("tmp/input-${i + 1}.bin") // Binary output file

        DataInputStream(BufferedInputStream(inputFile.inputStream())).use { input ->
            DataOutputStream(BufferedOutputStream(outputFile.outputStream())).use { output ->
                // On last iteration, return the count without writing to the file
                if (i == times) {
                    var count = 0
                    while (true) {
                        try {
                            val number = input.readUTF().toBigInteger()
                            count += blink(number).size
                        } catch (e: Exception) {
                            // End of file
                            break
                        }
                    }
                    return count
                }

                // Read, process, and write to the next file
                while (true) {
                    try {
                        val number = input.readUTF().toBigInteger()
                        blink(number).forEach { result ->
                            output.writeUTF(result.toString())
                        }
                    } catch (e: Exception) {
                        // End of file
                        break
                    }
                }
            }
        }

        inputFile.delete() // Delete the processed input file
    }
    return -1
}


fun process(number: BigInteger): List<BigInteger> {
    return when {
        number.isZero() -> listOf(1.toBigInteger())
        number.hasEvenDigits() -> number.splitInTwo()
        else -> listOf(number.multiplyBy2024())
    }
}

fun BigInteger.isZero(): Boolean = this == 0.toBigInteger()
fun BigInteger.hasEvenDigits(): Boolean = this.toString().length % 2 == 0
fun BigInteger.splitInTwo(): List<BigInteger> =
    this.toString().chunked(this.toString().length / 2).map(String::toBigInteger)

fun BigInteger.multiplyBy2024(): BigInteger = this * 2024.toBigInteger()
fun BigInteger.toList(): List<BigInteger> = listOf(this)