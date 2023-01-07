package com.zagayevskiy.holidays.sort.randomaccess.efficient

import com.zagayevskiy.holidays.collection.buildMaxHeap
import com.zagayevskiy.holidays.collection.heapifyMax
import com.zagayevskiy.holidays.collection.swap
import com.zagayevskiy.holidays.sort.randomaccess.RandomAccessSort

class HeapSort : RandomAccessSort {
    override val name = "HeapSort"
    override val declaredStability = false

    override fun <T> sort(list: MutableList<T>, comparator: Comparator<T>, additionalMemoryConstructor: (List<T>) -> MutableList<T>) {
        list.buildMaxHeap(comparator)
        for (newHeapSize in list.size - 1 downTo 1) {
            list.swap(0, newHeapSize)
            list.heapifyMax(0, comparator, newHeapSize)
        }
    }
}