package com.zagayevskiy.holidays.sort.randomaccess.efficient

import com.zagayevskiy.holidays.sort.randomaccess.RandomAccessSort
import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.min

class MergeSort private constructor(impl: RandomAccessSort) : RandomAccessSort by impl {
    enum class Mode {
        BottomUp,
        TopDown,
    }

    constructor(mode: Mode) : this(mode.toImplementation())
}

private fun MergeSort.Mode.toImplementation() = when (this) {
    MergeSort.Mode.BottomUp -> BottomUpMergeSort()
    MergeSort.Mode.TopDown -> TopDownMergeSort()
}

private class BottomUpMergeSort : RandomAccessSort {
    override val name = "Bottom-up merge sort"
    override val declaredStability = true

    override fun <T> sort(list: MutableList<T>, comparator: Comparator<T>, additionalMemoryConstructor: (List<T>) -> MutableList<T>) {
        if (list.isEmpty()) return
        var chunkSize = 1
        var currentList = list
        var other = additionalMemoryConstructor(list)

        val stepCount = ceil(log2(list.size.toFloat())).toInt()
        //Avoid additional copy at the end
        if (stepCount % 2 == 1) {
            currentList = other
            other = list
        }

        while (chunkSize < list.size) {
            mergeChunksInPairs(currentList, other, chunkSize, comparator)
            chunkSize *= 2

            val tmp = currentList
            currentList = other
            other = tmp
        }
    }

    private fun <T> mergeChunksInPairs(list: MutableList<T>, other: MutableList<T>, chunkSize: Int, comparator: Comparator<T>) {
        var resultIndex = 0

        for (leftChunkStart in 0 until list.size step chunkSize * 2) {
            val rightChunkStart = min(leftChunkStart + chunkSize, list.size)
            val rightChunkEnd = min(rightChunkStart + chunkSize, list.size)

            var leftChunkIndex = leftChunkStart
            var rightChunkIndex = rightChunkStart

            while (leftChunkIndex < rightChunkStart && rightChunkIndex < rightChunkEnd) {
                val left = list[leftChunkIndex]
                val right = list[rightChunkIndex]
                other[resultIndex++] = if (comparator.compare(left, right) <= 0) {
                    left.also { leftChunkIndex++ }
                } else {
                    right.also { rightChunkIndex++ }
                }
            }

            while (leftChunkIndex < rightChunkStart) {
                other[resultIndex++] = list[leftChunkIndex++]
            }

            while (rightChunkIndex < rightChunkEnd) {
                other[resultIndex++] = list[rightChunkIndex++]
            }
        }

        while (resultIndex < list.size) {
            other[resultIndex] = list[resultIndex]
            resultIndex++
        }
    }
}

private class TopDownMergeSort : RandomAccessSort {
    override val name = "Top-down merge sort"
    override val declaredStability = true

    override fun <T> sort(list: MutableList<T>, comparator: Comparator<T>, additionalMemoryConstructor: (List<T>) -> MutableList<T>) {
        mergeSort(src = additionalMemoryConstructor(list), result = list, fromInclusive = 0, toExclusive = list.size, comparator)
    }

    private fun <T> mergeSort(src: MutableList<T>, result: MutableList<T>, fromInclusive: Int, toExclusive: Int, comparator: Comparator<T>) {
        if (toExclusive - fromInclusive <= 1) return

        val middle = fromInclusive + (toExclusive - fromInclusive) / 2

        mergeSort(src = result, result = src, fromInclusive = fromInclusive, toExclusive = middle, comparator)
        mergeSort(src = result, result = src, fromInclusive = middle, toExclusive = toExclusive, comparator)

        src.mergeTo(result = result, fromInclusive = fromInclusive, middle = middle, toExclusive = toExclusive, comparator)
    }

    private fun <T> MutableList<T>.mergeTo(result: MutableList<T>, fromInclusive: Int, middle: Int, toExclusive: Int, comparator: Comparator<T>) {
        var resultIndex = fromInclusive
        var leftIndex = fromInclusive
        var rightIndex = middle

        while (leftIndex < middle && rightIndex < toExclusive) {
            val left = get(leftIndex)
            val right = get(rightIndex)
            result[resultIndex++] = if (comparator.compare(left, right) <= 0) {
                left.also { leftIndex++ }
            } else {
                right.also { rightIndex++ }
            }
        }

        while (leftIndex < middle) {
            result[resultIndex++] = get(leftIndex++)
        }

        while (rightIndex < toExclusive) {
            result[resultIndex++] = get(rightIndex++)
        }
    }
}