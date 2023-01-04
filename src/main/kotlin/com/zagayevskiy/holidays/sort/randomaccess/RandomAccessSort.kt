package com.zagayevskiy.holidays.sort.randomaccess

import com.zagayevskiy.holidays.sort.NamedSort

interface RandomAccessSort : NamedSort {
    fun <T> sort(list: MutableList<T>, comparator: Comparator<T>, additionalMemoryConstructor: (List<T>) -> MutableList<T> = { it.toMutableList() })
}

fun <T : Comparable<T>> Collection<T>.sortedWith(sort: RandomAccessSort): List<T> {
    return sortedWith(sort, compareBy { it })
}

fun <T> Collection<T>.sortedWith(sort: RandomAccessSort, comparator: Comparator<T>): List<T> {
    return toMutableList().apply { sortWith(sort, comparator) }
}

fun <T> MutableList<T>.sortWith(sort: RandomAccessSort, comparator: Comparator<T>) {
    sort.sort(this, comparator)
}