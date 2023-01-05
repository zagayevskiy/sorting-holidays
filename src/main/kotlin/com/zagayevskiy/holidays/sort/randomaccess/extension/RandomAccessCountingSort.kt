package com.zagayevskiy.holidays.sort.randomaccess.extension

import com.zagayevskiy.holidays.collection.asCountingList
import com.zagayevskiy.holidays.collection.hasStableIndices
import com.zagayevskiy.holidays.sort.randomaccess.RandomAccessSort

data class RandomAccessSortStatistics(val compares: Long, val writes: Long, val reads: Long, val sortedStably: Boolean)

fun <T> MutableList<T>.sortCounting(sort: RandomAccessSort, comparator: Comparator<T>): RandomAccessSortStatistics {

    val countingList = withIndex().asCountingList()

    var compares = 0L

    sort.sort(
        countingList,
        comparator = { (_, v1), (_, v2) ->
            compares++
            comparator.compare(v1, v2)
        },
        additionalMemoryConstructor = { it.asCountingList() }
    )

    val reads = countingList.readCount
    val writes = countingList.writeCount

    clear()
    addAll(countingList.map { (_, value) -> value })

    return RandomAccessSortStatistics(
        compares = compares,
        writes = writes,
        reads = reads,
        sortedStably = countingList.hasStableIndices(comparator),
    )
}
