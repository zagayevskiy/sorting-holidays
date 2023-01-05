package com.zagayevskiy.holidays.sort.linkedlist.extension

import com.zagayevskiy.holidays.collection.CountingMutableLinkedList
import com.zagayevskiy.holidays.collection.MutableLinkedList
import com.zagayevskiy.holidays.collection.asMutableLinkedList
import com.zagayevskiy.holidays.collection.hasStableIndices
import com.zagayevskiy.holidays.sort.linkedlist.LinkedListSort
import com.zagayevskiy.holidays.sort.linkedlist.TemporaryNode
import kotlin.system.measureTimeMillis


fun <T> LinkedListSort.countingSort(items: MutableLinkedList<T>, comparator: Comparator<T>): LinkedListSortStatistic {

    val countingList = CountingMutableLinkedList(items.withIndex().asMutableLinkedList())
    var compares = 0L
    sort(
        countingList,
        comparator = { (_, v1), (_, v2) ->
            compares++
            comparator.compare(v1, v2)
        },
        temporaryNodeBuilder = { child ->
            countingList.run { TemporaryNode(child).asCountingNodes() }
        },
    )

    val readCount = countingList.readCount
    val valueReadCount = countingList.valueReadCount
    val writeCount = countingList.writeCount


    val timeMillis = measureTimeMillis {
        sort(items, comparator)
    }

    val checkIdentity = items.zip(countingList) { i1, (_, i2) ->
        comparator.compare(i1, i2)
    }.all { it == 0 }
    if (!checkIdentity) throw IllegalStateException("Counting and not counting sorts has different results")


    return LinkedListSortStatistic(
        timeMillis = timeMillis,
        compares = compares,
        nextReads = readCount,
        valueReads = valueReadCount,
        writes = writeCount,
        sortedStably = countingList.hasStableIndices(comparator),
    )
}

data class LinkedListSortStatistic(
    val timeMillis: Long,
    val compares: Long,
    val nextReads: Long,
    val valueReads: Long,
    val writes: Long,
    val sortedStably: Boolean,
)


