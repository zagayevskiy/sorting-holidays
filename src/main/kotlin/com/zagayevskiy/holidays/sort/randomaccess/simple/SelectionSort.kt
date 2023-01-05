package com.zagayevskiy.holidays.sort.randomaccess.simple

import com.zagayevskiy.holidays.collection.swap
import com.zagayevskiy.holidays.sort.randomaccess.RandomAccessSort

class SelectionSort private constructor(impl: AbsSelectionSort) : RandomAccessSort by impl {
    enum class Mode {
        Stable,
        Unstable,
    }

    constructor(mode: Mode) : this(mode.toImplementation())
}

private fun SelectionSort.Mode.toImplementation() = when (this) {
    SelectionSort.Mode.Stable -> StableSelectionSort()
    SelectionSort.Mode.Unstable -> UnstableSelectionSort()
}

private class StableSelectionSort : AbsSelectionSort("Selection sort stable", true) {
    override fun <T> MutableList<T>.insert(minIndex: Int, currentUnsortedIndex: Int) {
        val minValue = get(minIndex)
        for (i in minIndex downTo currentUnsortedIndex + 1) {
            set(i, get(i - 1))
        }
        set(currentUnsortedIndex, minValue)
    }
}

private class UnstableSelectionSort : AbsSelectionSort("Selection sort unstable", false) {
    override fun <T> MutableList<T>.insert(minIndex: Int, currentUnsortedIndex: Int) {
        swap(minIndex, currentUnsortedIndex)
    }
}

private abstract class AbsSelectionSort(override val name: String, override val declaredStability: Boolean) : RandomAccessSort {
    override fun <T> sort(list: MutableList<T>, comparator: Comparator<T>, additionalMemoryConstructor: (List<T>) -> MutableList<T>) {
        if (list.size <= 1) return

        for (i in 0 until list.size) {
            val currentMinIndex = list.indexOfMinIn(fromInclusive = i, toExclusive = list.size, comparator)
            if (i != currentMinIndex) {
                list.insert(minIndex = currentMinIndex, currentUnsortedIndex = i)
            }
        }
    }

    protected abstract fun <T> MutableList<T>.insert(minIndex: Int, currentUnsortedIndex: Int)

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