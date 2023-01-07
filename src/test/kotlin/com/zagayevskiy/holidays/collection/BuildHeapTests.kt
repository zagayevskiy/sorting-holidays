package com.zagayevskiy.holidays.collection

import com.zagayevskiy.holidays.collection.trees.TreeTests
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BuildHeapTests {

    companion object {
        @JvmStatic
        fun data() = TreeTests.data()
    }

    @ParameterizedTest
    @MethodSource("data")
    fun fullHeapBuildCorrect(test: List<String>) {
        val heap = test.toMutableList().apply { buildMaxHeap(compareBy { it }) }

        heap.checkHeapInvariant(compareBy { it }, heap.size)
    }

    @ParameterizedTest
    @MethodSource("data")
    fun twoThirdsHeapBuildCorrect(test: List<String>) {
        val heapSize = test.size / 3 * 2
        val heap = test.toMutableList().apply { buildMaxHeap(compareBy { it }, heapSize) }

        heap.checkHeapInvariant(compareBy { it }, heapSize)

        assertEquals(test.subList(heapSize, test.size), heap.subList(heapSize, test.size), "rest of heapified list expected to be unchanged")
    }


    private fun <T> List<T>.checkHeapInvariant(comparator: Comparator<T>, heapSize: Int) {
        for (i in 0 until heapSize) {
            checkHeapInvariantForNode(i, comparator, heapSize)
        }
    }

    private fun <T> List<T>.checkHeapInvariantForNode(node: Int, comparator: Comparator<T>, heapSize: Int) {
        val left = (node * 2 + 1).takeIf { it < heapSize } ?: return
        assertTrue(comparator.compare(get(node), get(left)) >= 0, "node[$node]=${get(node)} must be >= left[$left]=${get(left)}")

        val right = (node * 2 + 2).takeIf { it < heapSize } ?: return
        assertTrue(comparator.compare(get(node), get(right)) >= 0, "node[$node]=${get(node)} must be >= right[$right]=${get(right)}")
    }
}