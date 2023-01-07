package com.zagayevskiy.holidays.sort.randomaccess.hybrid

import com.zagayevskiy.holidays.sort.randomaccess.RandomAccessSort
import com.zagayevskiy.holidays.sort.randomaccess.efficient.HeapSort
import com.zagayevskiy.holidays.sort.randomaccess.efficient.QuickSort
import com.zagayevskiy.holidays.sort.randomaccess.simple.InsertionSort
import com.zagayevskiy.holidays.sort.randomaccess.sortWith
import kotlin.math.floor
import kotlin.math.log2

class IntroSort(private val insertionSwitch: Int = 16) : RandomAccessSort {
    override val name = "Introsort"
    override val declaredStability = false

    private val insertionSort = InsertionSort()
    private val heapSort = HeapSort()

    override fun <T> sort(list: MutableList<T>, comparator: Comparator<T>, additionalMemoryConstructor: (List<T>) -> MutableList<T>) {
        introsort(
            list,
            comparator,
            startInclusive = 0,
            endExclusive = list.size,
            remainingDepth = floor(log2(list.size.toDouble())).toInt() * 2,
        )
    }

    private fun <T> introsort(list: MutableList<T>, comparator: Comparator<T>, startInclusive: Int, endExclusive: Int, remainingDepth: Int) {
        if (startInclusive >= endExclusive - 1) return

        if (endExclusive - startInclusive <= insertionSwitch) {
            list.subList(fromIndex = startInclusive, toIndex = endExclusive).sortWith(insertionSort, comparator)
            return
        }

        if (remainingDepth == 0) {
            list.subList(fromIndex = startInclusive, toIndex = endExclusive).sortWith(heapSort, comparator)
            return
        }

        val pivot = QuickSort.partition(list, comparator, startInclusive, endExclusive)

        introsort(list, comparator, startInclusive = startInclusive, endExclusive = pivot, remainingDepth = remainingDepth - 1)
        introsort(list, comparator, startInclusive = pivot + 1, endExclusive = endExclusive, remainingDepth = remainingDepth - 1)
    }
}