package com.zagayevskiy.holidays.sort

import com.zagayevskiy.holidays.collection.asMutableLinkedList
import com.zagayevskiy.holidays.sort.linkedlist.LinkedListSort
import com.zagayevskiy.holidays.sort.linkedlist.efficient.LinkedListMergeSort
import com.zagayevskiy.holidays.sort.linkedlist.efficient.LinkedListQuickSort
import com.zagayevskiy.holidays.sort.linkedlist.extension.countingSort
import com.zagayevskiy.holidays.sort.linkedlist.simple.LinkedListBubbleSort
import com.zagayevskiy.holidays.sort.linkedlist.simple.LinkedListSelectionSort
import com.zagayevskiy.holidays.sort.randomaccess.RandomAccessSort
import com.zagayevskiy.holidays.sort.randomaccess.efficient.MergeSort
import com.zagayevskiy.holidays.sort.randomaccess.efficient.QuickSort
import com.zagayevskiy.holidays.sort.randomaccess.extension.sortCounting
import com.zagayevskiy.holidays.sort.randomaccess.simple.BubbleSort
import com.zagayevskiy.holidays.sort.randomaccess.simple.SelectionSort
import com.zagayevskiy.holidays.sort.randomaccess.sortedWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
import java.util.stream.Stream
import java.util.stream.StreamSupport
import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun buildData(block: SortableDataBuilder.() -> Unit): List<Data<Any?>> {
    return SortableDataBuilder().apply(block).build()
}

class SortTests {

    companion object {

        @JvmStatic
        private fun linkedListTestCases() = data() * linkedListSorts()

        @JvmStatic
        private fun randomAccessTestCases() = data() * randomAccessSorts()

        private fun linkedListSorts(): List<LinkedListSort> = listOf(
            LinkedListBubbleSort(),
            LinkedListSelectionSort(),
            LinkedListQuickSort(),
            LinkedListMergeSort(),
        )


        private fun randomAccessSorts(): List<RandomAccessSort> = listOf(
            BubbleSort(),
            SelectionSort(),
            QuickSort(),
            MergeSort(MergeSort.Mode.BottomUp),
            MergeSort(MergeSort.Mode.TopDown)
        )

        private fun data() = buildData {
            data(emptyList<Int>())
            data(0)
            data("Hello")
            data(123.0, -3435.0)
            data(-3145.0, 1234.5)
            data(1, 2)
            data(3, 1, 2)
            data(3, 1, 4, 1)
            data(1, 2, 1, 2, 1, 2, 1, 2, 1, 2)
            data("1", "q", "a", "s", "d", "w", "qwerty", "123", "WASP", "uiop")
            data("1", "a", "q", "qwerty", "a", "s", "d", "w", "qwerty", "123", "qwerty", "WASP", "uiop", "a", "a")
            data("1", "q", "a", "s", "d", "w", "qwerty", "123", "WASP", "uiop", "123", comparator = compareByDescending { it })
            data((0..10).shuffled(Random(123456789L)))
            data((0..100).shuffled(Random(123456789L)), comparator = compareByDescending { it })
            data((0..1000).shuffled(Random(123456789L)))
        }
    }

    @ParameterizedTest()
    @MethodSource("linkedListTestCases")
    fun testLinkedListSort(case: TestCase<LinkedListSort, Any?>) {
        with(case) {
            val ll = items.asMutableLinkedList()

            sort.sort(ll, comparator)

            val actual = ll.toList()
            val expected = items.sortedWith(comparator)

            assertEquals(expected, actual)
        }

    }

    @ParameterizedTest()
    @MethodSource("linkedListTestCases")
    fun testLinkedListCountingSort(case: TestCase<LinkedListSort, Any?>) {
        with(case) {
            val ll = items.asMutableLinkedList()

            val result = sort.countingSort(ll, comparator)

            val actual = ll.toList()
            val expected = items.sortedWith(comparator)

            assertEquals(expected, actual)
            if (sort.declaredStability) {
                assertTrue(result.sortedStably, "${sort.name} declare stability but sorted not stably")
            }
        }
    }

    @ParameterizedTest()
    @MethodSource("randomAccessTestCases")
    fun testRandomAccessSort(case: TestCase<RandomAccessSort, Any?>) {
        with(case) {
            val actual = items.sortedWith(sort, comparator)
            val expected = items.sortedWith(comparator)

            assertEquals(expected, actual)
        }
    }

    @ParameterizedTest()
    @MethodSource("randomAccessTestCases")
    fun testRandomAccessCountingSort(case: TestCase<RandomAccessSort, Any?>) {
        with(case) {
            val mutableItems = items.toMutableList()
            val result = mutableItems.sortCounting(sort, comparator)
            val actual = mutableItems.toList()
            val expected = items.sortedWith(comparator)

            assertEquals(expected, actual)
            if (sort.declaredStability) {
                assertTrue(result.sortedStably, "${sort.name} declare stability but sorted not stably")
            }
        }
    }
}


data class TestCase<TSort : NamedSort, TItem>(val sort: TSort, val items: List<TItem>, val comparator: Comparator<TItem>) {
    override fun toString() = "${sort.name} $items"
}

data class Data<TItem>(val items: List<TItem>, val comparator: Comparator<TItem>)


operator fun <TSort : NamedSort> List<Data<Any?>>.times(sorts: List<TSort>): Stream<TestCase<TSort, Any?>> {
    val cases: List<TestCase<TSort, Any?>> = sorts.map { sort ->
        this.map { (items, comparator) ->
            TestCase(sort, items, comparator)
        }
    }.flatten()
    return StreamSupport.stream(cases.spliterator(), false)
}

class SortableDataBuilder {
    private val list = mutableListOf<Data<Any?>>()

    fun <TItem : Comparable<TItem>> data(vararg items: TItem, comparator: Comparator<TItem> = compareBy { it }) {
        data(items.asList(), comparator)
    }

    fun <TItem> data(items: List<TItem>, comparator: Comparator<TItem> = compareBy { it }) where TItem : Any, TItem : Comparable<TItem> {
        @Suppress("UNCHECKED_CAST")
        list.add(Data(items, comparator) as Data<Any?>)
    }

    fun build(): List<Data<Any?>> {
        return list
    }
}