package com.zagayevskiy.holidays.sort.randomaccess.simple

import com.zagayevskiy.holidays.collection.swap
import com.zagayevskiy.holidays.sort.randomaccess.RandomAccessSort

class SelectionSort : RandomAccessSort {
    override val name = "Selection sort"
    override val declaredStability = false

    override fun <T> sort(list: MutableList<T>, comparator: Comparator<T>, additionalMemoryConstructor: (List<T>) -> MutableList<T>) {
        if (list.size <= 1) return

        for (i in 0 until list.size) {
            val currentMinIndex = list.indexOfMinIn(fromInclusive = i, toExclusive = list.size, comparator)
            if (i != currentMinIndex) {
                list.swap(i, currentMinIndex)
            }
        }
    }

    private fun <T> List<T>.indexOfMinIn(fromInclusive: Int, toExclusive: Int, comparator: Comparator<T>): Int {
        var minIndex = fromInclusive
        var min = get(minIndex)
        for (i in fromInclusive + 1 until toExclusive) {
            val current = get(i)
            if (comparator.compare(current, min) < 0) {
                minIndex = i
                min = current
            }
        }

        return minIndex
    }
}