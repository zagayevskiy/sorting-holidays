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

fun <T> MutableList<T>.buildMaxHeap(comparator: Comparator<T>, heapSize: Int = size) {
    if (heapSize <= 1) return
    for (current in heapSize / 2 downTo 0) {
        heapifyMax(current, comparator, heapSize)
    }
}

fun <T> MutableList<T>.heapifyMax(index: Int, comparator: Comparator<T>, heapSize: Int = size) {
    if (heapSize < 0 || heapSize > size) throw IllegalArgumentException("heapSize($heapSize) must in 0..<size($size)")
    var largestIndex = index.takeIf { it in 0 until heapSize } ?: throw IllegalArgumentException("index($index) must be in 0..<heapSize($heapSize)")
    do {
        val currentIndex = largestIndex

        var largestValue = getOrNull(largestIndex) ?: return
        val leftIndex = (currentIndex * 2 + 1).takeIf { it < heapSize } ?: return

        getOrNull(leftIndex)?.let { leftValue ->
            if (comparator.compare(largestValue, leftValue) < 0) {
                largestValue = leftValue
                largestIndex = leftIndex
            }
        }

        (leftIndex + 1).takeIf { it < heapSize }?.let { rightIndex ->
            getOrNull(rightIndex)?.let { rightValue ->
                if (comparator.compare(largestValue, rightValue) < 0) {
                    largestValue = rightValue
                    largestIndex = rightIndex
                }
            }
        }

        if (largestIndex != currentIndex) {
            swap(largestIndex, currentIndex)
        } else {
            return
        }

    } while (true)
}