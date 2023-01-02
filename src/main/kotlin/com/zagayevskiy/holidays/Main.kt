package com.zagayevskiy.holidays

import com.zagayevskiy.holidays.collection.asMutableLinkedList
import com.zagayevskiy.holidays.sort.linkedlist.LinkedListSort
import com.zagayevskiy.holidays.sort.linkedlist.efficient.LinkedListQuickSort
import com.zagayevskiy.holidays.sort.linkedlist.extension.countingSort
import com.zagayevskiy.holidays.sort.linkedlist.simple.LinkedListBubbleSort

fun main() {
    val list = (0..40000).shuffled()
    test(LinkedListQuickSort(), list)
    test(LinkedListBubbleSort(), list)
}

fun test(sort: LinkedListSort, case: List<Int>) {
    val ll = case.asMutableLinkedList()

    val result = sort.countingSort(ll, compareBy { it })

    with(result) {
        println("${sort.name} time: ${timeMillis / 1000f}s, compares: $compares, next reads: $nextReads, writes: $writes, value reads: $valueReads")
    }

    if (ll.toList() != case.sorted()) throw IllegalStateException("wrong order $ll")
}