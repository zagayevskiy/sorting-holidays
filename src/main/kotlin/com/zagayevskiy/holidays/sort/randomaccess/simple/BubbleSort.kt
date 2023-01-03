package com.zagayevskiy.holidays.sort.randomaccess.simple

import com.zagayevskiy.holidays.collection.swap
import com.zagayevskiy.holidays.sort.randomaccess.RandomAccessSort

class BubbleSort : RandomAccessSort {
    override val name = "Bubble sort"
    override fun <T> sort(list: MutableList<T>, comparator: Comparator<T>) {
        bubbleSort(list, comparator)
    }

    private fun <T> bubbleSort(list: MutableList<T>, comparator: Comparator<T>) {
        var endIndexExclusive = list.size
        do {
            val prevEndIndexExclusive = endIndexExclusive
            endIndexExclusive = 0
            for (currentIndex in 0 until prevEndIndexExclusive - 1) {
                val nextIndex = currentIndex + 1
                if (comparator.compare(list[currentIndex], list[nextIndex]) > 0) {
                    list.swap(currentIndex, nextIndex)
                    endIndexExclusive = nextIndex
                }
            }
        } while (endIndexExclusive != 0)
    }
}