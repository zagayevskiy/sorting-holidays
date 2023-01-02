package com.zagayevskiy.holidays.sort.linkedlist.efficient

import com.zagayevskiy.holidays.collection.MutableLinkedList
import com.zagayevskiy.holidays.collection.MutableLinkedList.Node
import com.zagayevskiy.holidays.sort.linkedlist.LinkedListSort

class LinkedListQuickSort : LinkedListSort {
    override val name = "Qsort"

    override fun <T> sort(items: MutableLinkedList<T>, comparator: Comparator<T>, temporaryNodeBuilder: (child: Node<T>?) -> Node<T>) {
        items.head = items.head?.qsort(comparator)
    }

    private fun <T> Node<T>.qsort(comparator: Comparator<T>): Node<T> {
        if (next == null) return this

        val partition = partition(comparator)
        val sortedLeft = partition.left?.qsort(comparator)
        val sortedRight = partition.right?.qsort(comparator)

        return join(pivot = partition.pivot, left = sortedLeft, right = sortedRight)
    }

    private class Partition<T>(val pivot: Node<T>, val left: Node<T>?, val right: Node<T>?)

    private fun <T> Node<T>.partition(comparator: Comparator<T>): Partition<T> {
        var left: Node<T>? = null
        var right: Node<T>? = null

        val pivot = value
        var cursor = next

        while (cursor != null) {
            val cursorNext = cursor.next
            cursor.next = if (comparator.compare(cursor.value, pivot) < 0) {
                left.also { left = cursor }
            } else {
                right.also { right = cursor }
            }
            cursor = cursorNext
        }

        next = null
        return Partition(this, left, right)
    }

    private fun <T> join(pivot: Node<T>, left: Node<T>?, right: Node<T>?): Node<T> {
        pivot.next = right

        if (left == null) {
            return pivot
        }

        var cursor: Node<T> = left
        while (cursor.next != null) {
            cursor = cursor.next!!
        }

        cursor.next = pivot

        return left
    }
}