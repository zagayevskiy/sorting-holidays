package com.zagayevskiy.holidays.sort.linkedlist.efficient

import com.zagayevskiy.holidays.collection.MutableLinkedList
import com.zagayevskiy.holidays.collection.size
import com.zagayevskiy.holidays.sort.linkedlist.LinkedListSort

class LinkedListMergeSort : LinkedListSort {
    override val name = "Merge sort"
    override val declaredStability = true

    override fun <T> sort(items: MutableLinkedList<T>, comparator: Comparator<T>, temporaryNodeBuilder: (child: MutableLinkedList.Node<T>?) -> MutableLinkedList.Node<T>) {
        items.head = mergeSort(items.head ?: return, items.size(), comparator)
    }

    private fun <T> mergeSort(head: MutableLinkedList.Node<T>, size: Int, comparator: Comparator<T>): MutableLinkedList.Node<T> {

        if (size <= 1) return head //Single-element list is sorted

        val half = size / 2

        val right = head.splitBy(half)

        val sortedLeft = mergeSort(head, half, comparator)
        val sortedRight = mergeSort(right, size - half, comparator)

        return mergeSortedLists(leftHead = sortedLeft, rightHead = sortedRight, comparator)
    }

    private fun <T> MutableLinkedList.Node<T>.splitBy(index: Int): MutableLinkedList.Node<T> {
        val middle = findNodeAt(index)
        val right = middle.next ?: throw IllegalStateException("Invalid split index $index: found ${middle.joinToString()} on head ${joinToString()}")
        middle.next = null
        return right
    }

    private fun <T> MutableLinkedList.Node<T>.findNodeAt(index: Int): MutableLinkedList.Node<T> {
        var cursor = this
        for (i in 0 until index - 1) {
            cursor = cursor.next!!
        }
        return cursor
    }

    private fun <T> mergeSortedLists(
        leftHead: MutableLinkedList.Node<T>,
        rightHead: MutableLinkedList.Node<T>,
        comparator: Comparator<T>,
    ): MutableLinkedList.Node<T> {
        var leftCursor: MutableLinkedList.Node<T>? = leftHead
        var rightCursor: MutableLinkedList.Node<T>? = rightHead

        val resultHead = if (comparator.compare(leftHead.value, rightHead.value) <= 0) {
            leftCursor = leftHead.next
            leftHead
        } else {
            rightCursor = rightHead.next
            rightHead
        }
        var resultCursor = resultHead

        while (leftCursor != null && rightCursor != null) {
            val next = if (comparator.compare(leftCursor.value, rightCursor.value) <= 0) {
                leftCursor.apply { leftCursor = next }
            } else {
                rightCursor.apply { rightCursor = next }
            }
            resultCursor.next = next
            resultCursor = next
        }

        // at that point only one of those can be not null
        resultCursor.next = leftCursor ?: rightCursor

        return resultHead
    }
}