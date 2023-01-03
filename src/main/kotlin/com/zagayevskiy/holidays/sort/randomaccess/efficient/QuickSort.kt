package com.zagayevskiy.holidays.sort.randomaccess.efficient

import com.zagayevskiy.holidays.collection.swap
import com.zagayevskiy.holidays.sort.randomaccess.RandomAccessSort

class QuickSort : RandomAccessSort {
    override val name = "Qsort"

    override fun <T> sort(list: MutableList<T>, comparator: Comparator<T>) {
        qsort(
            list,
            comparator,
            startInclusive = 0,
            endExclusive = list.size,
        )
    }

    private fun <T> qsort(list: MutableList<T>, comparator: Comparator<T>, startInclusive: Int, endExclusive: Int) {
        if (startInclusive >= endExclusive - 1) return

        val pivot = list.partition(comparator, startInclusive, endExclusive)

        qsort(list, comparator, startInclusive = startInclusive, endExclusive = pivot)
        qsort(list, comparator, startInclusive = pivot + 1, endExclusive = endExclusive)
    }

    private fun <T> MutableList<T>.partition(comparator: Comparator<T>, startInclusive: Int, endExclusive: Int,): Int {
        val endInclusive = endExclusive - 1

        val pivot = get(endInclusive)

        var tempPivot = startInclusive

        for (i in startInclusive until endInclusive) {
            if (comparator.compare(get(i), pivot) < 0) {
                if (tempPivot != i) {
                    swap(tempPivot, i)
                }
                tempPivot++
            }
        }

        swap(endInclusive, tempPivot)

        return tempPivot
    }
}