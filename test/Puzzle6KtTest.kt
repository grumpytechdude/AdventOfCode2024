import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class Puzzle6KtTest {

    @Test
    fun forward_emptyInput() {
        // Arrange
        val inputArray: List<List<String>> = listOf()
        val cell = Cell(0, 0)

        // Act & Assert
        assertNull(cell.forward(inputArray, "#"))
    }

    @Test
    fun forward_singleCharacter() {
        // Arrange
        val inputArray: List<List<String>> = listOf(listOf("^"))
        val cell = Cell(0, 0)

        // Act
        val result = cell.forward(inputArray, "#")

        // Assert
        assertEquals(GuardDirection.RIGHT to Cell(0, 0), result)
    }

    @Test
    fun forward_encountersStopCharacter() {
        // Arrange
        val inputArray: List<List<String>> = listOf(listOf("^", "#"))
        val cell = Cell(0, 0)

        // Act
        val result = cell.forward(inputArray, "#")

        // Assert
        assertEquals(GuardDirection.OFF_THE_MAP to Cell(0, 1), result)
    }
}