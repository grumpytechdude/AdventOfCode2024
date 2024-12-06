import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.math.exp

class Puzzle2KtTest {

    @org.junit.jupiter.api.Test
    fun `number one is greater than number two`() {
        val result = isGreaterThan2(2, 1)
        assertEquals(result, true to null)
    }

    @org.junit.jupiter.api.Test
    fun `number one is the same as than number two`() {
        val result = isGreaterThan2(2, 2)
        assertEquals(result, false to Direction.LEFT)
    }

    @org.junit.jupiter.api.Test
    fun `number one is less than number two`() {
        val result = isGreaterThan2(2, 3)
        assertEquals(result, false to Direction.RIGHT)
    }

    @org.junit.jupiter.api.Test
    fun isLessThan2() {
    }

    @org.junit.jupiter.api.Test
    fun `returns true`() {
        assertEquals(true to null, isWithinSafeDistance2(listOf(1, 3), 0))
    }

    @org.junit.jupiter.api.Test
    fun `returns right if removing right would be safe`() {
        assertEquals(false to Direction.RIGHT, isWithinSafeDistance2(listOf(1, 2, 6, 4), 1))
    }

    @org.junit.jupiter.api.Test
    fun `returns left if removing right would be safe`() {
        assertEquals(false to Direction.LEFT, isWithinSafeDistance2(listOf(1, 7, 3, 4), 1))
    }
}