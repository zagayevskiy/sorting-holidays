package com.zagayevskiy.holidays.collection


fun <T> Iterable<T>.asMutableLinkedList(): MutableLinkedList<T> = MutableLinkedListImpl(this)

fun <T> MutableList<T>.asCountingList() = CountingMutableList(this)

fun <T> MutableList<T>.swap(i: Int, j: Int) {
    val valueI = get(i)
    set(i, get(j))
    set(j, valueI)
}