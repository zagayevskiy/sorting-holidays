package com.zagayevskiy.holidays.collection


fun <T> Iterable<T>.asMutableLinkedList(): MutableLinkedList<T> = MutableLinkedListImpl(this)

fun <T> Iterable<T>.asCountingList() = CountingMutableList(this)

fun <T> Iterable<IndexedValue<T>>.hasStableIndices(comparator: Comparator<T>): Boolean = asSequence()
    .windowed(size = 2, step = 2)
    .all { (left, right) ->
        comparator.compare(left.value, right.value) != 0 || left.index < right.index
    }

fun <T> MutableList<T>.swap(i: Int, j: Int) {
    val valueI = get(i)
    set(i, get(j))
    set(j, valueI)
}