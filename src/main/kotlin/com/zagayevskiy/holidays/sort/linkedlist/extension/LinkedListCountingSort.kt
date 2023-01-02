package com.zagayevskiy.holidays.sort.linkedlist.extension

import com.zagayevskiy.holidays.collection.CountingMutableLinkedList
import com.zagayevskiy.holidays.collection.MutableLinkedList
import com.zagayevskiy.holidays.collection.asMutableLinkedList
import com.zagayevskiy.holidays.sort.linkedlist.LinkedListSort
import com.zagayevskiy.holidays.sort.linkedlist.TemporaryNode
import kotlin.system.measureTimeMillis


fun <T> LinkedListSort.countingSort(items: MutableLinkedList<T>, comparator: Comparator<T>, measureTime: Boolean = true): LinkedListSortStatistic {
    val timeMillis = if (measureTime) {
        val itemsCopyForCleanTime = items.toList().asMutableLinkedList()
        measureTimeMillis {
            sort(itemsCopyForCleanTime, comparator)
        }
    } else {
        0L
    }

    val countingList = CountingMutableLinkedList(items)
    var compares = 0L
    sort(
        countingList,
        comparator = { v1, v2 ->
            compares++
            comparator.compare(v1, v2)
        },
        temporaryNodeBuilder = { child ->
            countingList.run { TemporaryNode(child).asCountingNodes() }
        },
    )

    return LinkedListSortStatistic(
        timeMillis = timeMillis,
        compares = compares,
        nextReads = countingList.readCount,
        valueReads = countingList.valueReadCount,
        writes = countingList.writeCount,
    )
}

data class LinkedListSortStatistic(val timeMillis: Long, val compares: Long, val nextReads: Long, val valueReads: Long, val writes: Long)


