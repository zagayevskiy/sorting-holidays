package com.zagayevskiy.holidays.sort.linkedlist.simple

import com.zagayevskiy.holidays.collection.MutableLinkedList
import com.zagayevskiy.holidays.collection.MutableLinkedList.Node
import com.zagayevskiy.holidays.sort.linkedlist.LinkedListSort

class LinkedListSelectionSort : LinkedListSort {
    override val name = "Selection sort"

    override fun <T> sort(items: MutableLinkedList<T>, comparator: Comparator<T>, temporaryNodeBuilder: (child: Node<T>?) -> Node<T>) {
        items.head = selectionSort(items.head ?: return, comparator, temporaryNodeBuilder)
    }

    private fun <T> selectionSort(head: Node<T>, comparator: Comparator<T>, temporaryNodeBuilder: (child: Node<T>?) -> Node<T>): Node<T> {
        val resultHolder = temporaryNodeBuilder(head)
        var prev = resultHolder
        var cursor = prev.next

        while (cursor != null) {
            var nodeBeforeMin: Node<T> = prev

            var nodeMin: Node<T> = cursor
            var minValue = cursor.value
            var minCursorPrev: Node<T> = cursor
            var minCursor = minCursorPrev.next

            while (minCursor != null) {
                val minCursorValue = minCursor.value
                if (comparator.compare(minCursorValue, minValue) < 0) {
                    nodeBeforeMin = minCursorPrev
                    nodeMin = minCursor
                    minValue = minCursorValue
                }
                minCursorPrev = minCursor
                minCursor = minCursor.next
            }

            if (nodeMin !== cursor) {
                nodeBeforeMin.next = nodeMin.next
                prev.next = nodeMin
                nodeMin.next = cursor
                prev = nodeMin
            } else {
                prev = cursor
                cursor = cursor.next
            }
        }

        return resultHolder.next!!
    }
}