package com.zagayevskiy.holidays.sort.linkedlist

import com.zagayevskiy.holidays.collection.MutableLinkedList
import com.zagayevskiy.holidays.sort.NamedSort

interface LinkedListSort : NamedSort {
    fun <T> sort(items: MutableLinkedList<T>, comparator: Comparator<T>)
}