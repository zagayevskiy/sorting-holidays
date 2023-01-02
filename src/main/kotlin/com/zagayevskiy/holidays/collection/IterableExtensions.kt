package com.zagayevskiy.holidays.collection


fun <T> Iterable<T>.asMutableLinkedList(): MutableLinkedList<T> = MutableLinkedListImpl(this)

fun <T> MutableList<T>.asCountingList() = CountingMutableList(this)