package com.zagayevskiy.holidays.sort.linkedlist.simple

import com.zagayevskiy.holidays.collection.MutableLinkedList
import com.zagayevskiy.holidays.collection.MutableLinkedList.Node
import com.zagayevskiy.holidays.collection.MutableLinkedListImpl
import com.zagayevskiy.holidays.sort.linkedlist.LinkedListSort


class LinkedListBubbleSort : LinkedListSort {

    override val name: String = "Bubble Sort"

    override fun <T> sort(items: MutableLinkedList<T>, comparator: Comparator<T>, temporaryNodeBuilder: (child: Node<T>?) -> Node<T>) {
        items.head = bubbleSort(items.head ?: return, comparator, temporaryNodeBuilder)
    }

    private fun <T> bubbleSort(head: Node<T>, comparator: Comparator<T>, temporaryNodeBuilder: (child: Node<T>?) -> Node<T>): Node<T> {
        if (head.next == null) return head

        var endCursor: Node<T>? = null

        val resultHolder: Node<T> = temporaryNodeBuilder(head)

        do {
            var newEndCursor: Node<T>? = null

            var prev = resultHolder
            var cursor: Node<T>? = prev.next

            while (cursor != endCursor) {
                if (swapWithNextIfNeeded(prev, cursor!!, comparator)) {
                    newEndCursor = cursor
                    cursor = prev.next!! // prev.next can not be null here because of swapWithNextIfNeeded implementation
                }
                prev = cursor
                cursor = cursor.next
            }

            endCursor = newEndCursor
        } while (endCursor != null)

        return resultHolder.next!!
    }

    private fun <T> swapWithNextIfNeeded(
        prev: Node<T>,
        node: Node<T>,
        comparator: Comparator<T>,
    ): Boolean {
        val next = node.next ?: return false

        if (comparator.compare(next.value, node.value) < 0) {
            val nextNext = next.next
            next.next = node
            node.next = nextNext
            prev.next = next
            return true
        }

        return false
    }
}