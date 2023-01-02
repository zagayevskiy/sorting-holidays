package com.zagayevskiy.holidays.sort.randomaccess.extension

import com.zagayevskiy.holidays.collection.asCountingList
import com.zagayevskiy.holidays.sort.randomaccess.RandomAccessSort
import com.zagayevskiy.holidays.sort.randomaccess.sortWith

data class RandomAccessSortStatistics(val compares: Long, val writes: Long, val reads: Long)

fun <T> MutableList<T>.sortCounting(sort: RandomAccessSort, comparator: Comparator<T>): RandomAccessSortStatistics {
    val countingList = asCountingList()
    var compares = 0L
    countingList.sortWith(sort) { v1, v2 ->
        compares++
        comparator.compare(v1, v2)
    }

    return RandomAccessSortStatistics(
        compares = compares,
        writes = countingList.writeCount,
        reads = countingList.readCount,
    )
}
