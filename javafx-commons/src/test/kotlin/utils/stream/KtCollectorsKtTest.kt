package utils.stream

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.stream.Collectors

internal class KtCollectorsKtTest {

    @Test
    fun testGroupBy() {
        val list = listOf(1, 4, 5, 6, 3, 8, 6, 9, 34, 3, 4 ,3 ,2)

        val expected = list.stream()
            .collect(Collectors.groupingBy { it })

        val actual = list.stream()
            .groupBy { it }

        assertEquals(expected, actual)
    }
}
