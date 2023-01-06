package com.zagayevskiy.holidays.sort.linkedlist.simple

import com.zagayevskiy.holidays.collection.MutableLinkedList
import com.zagayevskiy.holidays.collection.MutableLinkedList.Node
import com.zagayevskiy.holidays.sort.linkedlist.LinkedListSort

class LinkedListInsertionSort : LinkedListSort {
    override val name = "Insertion sort"
    override val declaredStability = true

    override fun <T> sort(items: MutableLinkedList<T>, comparator: Comparator<T>, temporaryNodeBuilder: (child: Node<T>?) -> Node<T>) {
        items.head = insertionSort(head = items.head ?: return, comparator, temporaryNodeBuilder)
    }

    private fun <T> insertionSort(head: Node<T>, comparator: Comparator<T>, temporaryNodeBuilder: (child: Node<T>?) -> Node<T>): Node<T> {
        val resultHolder = temporaryNodeBuilder(head)

        var cursorPrev = head
        var cursor = head.next
        while (cursor != null) {
            val value = cursor.value
            var insertPrev = resultHolder
            var insertCursor: Node<T> = resultHolder.next!! // It's always ok
            while (insertCursor !== cursor && comparator.compare(insertCursor.value, value) <= 0) {
                insertPrev = insertCursor
                insertCursor = insertCursor.next!! //It's ok because we inside head..cursor and can't see null
            }
            if (insertCursor !== cursor) {
                val cursorNext = cursor.next

                cursorPrev.next = cursorNext

                cursor.next = insertCursor
                insertPrev.next = cursor

                cursor = cursorNext
            } else {
                cursorPrev = cursor
                cursor = cursor.next
            }
        }

        return resultHolder.next!!
    }

}