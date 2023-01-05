package com.zagayevskiy.holidays

import com.zagayevskiy.holidays.collection.asCountingList
import com.zagayevskiy.holidays.collection.asMutableLinkedList
import com.zagayevskiy.holidays.sort.linkedlist.LinkedListSort
import com.zagayevskiy.holidays.sort.linkedlist.efficient.LinkedListMergeSort
import com.zagayevskiy.holidays.sort.linkedlist.efficient.LinkedListQuickSort
import com.zagayevskiy.holidays.sort.linkedlist.extension.countingSort
import com.zagayevskiy.holidays.sort.linkedlist.simple.LinkedListBubbleSort
import com.zagayevskiy.holidays.sort.linkedlist.simple.LinkedListSelectionSort
import com.zagayevskiy.holidays.sort.randomaccess.RandomAccessSort
import com.zagayevskiy.holidays.sort.randomaccess.efficient.MergeSort
import com.zagayevskiy.holidays.sort.randomaccess.efficient.QuickSort
import com.zagayevskiy.holidays.sort.randomaccess.extension.sortCounting
import com.zagayevskiy.holidays.sort.randomaccess.simple.BubbleSort
import com.zagayevskiy.holidays.sort.randomaccess.simple.SelectionSort

fun main() {
    val list = (0..10000).shuffled() + (0..100).shuffled()
    test(LinkedListMergeSort(), list)
    test(LinkedListQuickSort(), list)
    test(LinkedListSelectionSort(), list)
    test(LinkedListBubbleSort(), list)

    test(MergeSort(MergeSort.Mode.BottomUp), list)
    test(MergeSort(MergeSort.Mode.TopDown), list)
    test(QuickSort(), list)
    test(SelectionSort(), list)
    test(BubbleSort(), list)
}

fun test(sort: LinkedListSort, case: List<Int>) {
    val ll = case.asMutableLinkedList()

    val result = sort.countingSort(ll, compareBy { it })

    with(result) {
        println("LL ${sort.name} compares: $compares, next reads: $nextReads, writes: $writes, value reads: $valueReads, time: ${timeMillis / 1000f}s, stability: $sortedStably")
    }

    if (ll.toList() != case.sorted()) throw IllegalStateException("wrong order $ll")
}

fun test(sort: RandomAccessSort, case: List<Int>) {
    val mutable = case.toMutableList().asCountingList()

    val result = mutable.sortCounting(sort, compareBy { it })

    with(result) {
        println("RA ${sort.name} compares: $compares, reads: $reads, writes: $writes, stability: $sortedStably")
    }

    if (mutable.toList() != case.sorted()) throw IllegalStateException("wrong order $mutable")
}