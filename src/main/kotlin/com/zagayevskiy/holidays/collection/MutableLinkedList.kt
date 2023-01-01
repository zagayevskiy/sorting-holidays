package com.zagayevskiy.holidays.collection

interface MutableLinkedList<T> : Iterable<T> {
    interface Node<T> : Iterable<T> {
        val value: T
        var next: Node<T>?
    }

    var head: Node<T>?
}

class MutableLinkedListImpl<T> private constructor(override var head: MutableLinkedList.Node<T>? = null) : MutableLinkedList<T> {

    constructor(iterable: Iterable<T>) : this(iterable.asNodes())

    class NodeImpl<T>(override val value: T, override var next: MutableLinkedList.Node<T>? = null) : MutableLinkedList.Node<T> {
        override fun iterator(): Iterator<T> = MutableLinkedListNodeIterator(this)
        override fun toString(): String {
            return """$value -> $next"""
        }
    }

    override fun iterator(): Iterator<T> = MutableLinkedListNodeIterator(head)

    override fun toString(): String {
        return "MLL[${head?.joinToString() ?: "EMPTY"}]"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is MutableLinkedList<*>) return false

        var cursor = head
        var otherCursor = other.head
        while (cursor != null && otherCursor != null) {
            if (cursor.value != otherCursor.value) return false
            cursor = cursor.next
            otherCursor = otherCursor.next
        }

        return cursor == null && otherCursor == null
    }

    override fun hashCode(): Int {
        return fold(17) { acc, value -> 13 * acc + 31 * value.hashCode() }
    }
}

fun <T> MutableLinkedList<T>.size(): Int = fold(0) { acc, _ -> acc + 1 }

private fun <T> Iterable<T>.asNodes(): MutableLinkedList.Node<T>? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null

    val resultHead = MutableLinkedListImpl.NodeImpl(iterator.next())
    var resultCursor = resultHead

    while (iterator.hasNext()) {
        val next = MutableLinkedListImpl.NodeImpl(iterator.next())
        resultCursor.next = next
        resultCursor = next
    }

    return resultHead
}


private class MutableLinkedListNodeIterator<T>(private var cursor: MutableLinkedList.Node<T>?) : Iterator<T> {
    override fun hasNext(): Boolean = cursor != null

    override fun next(): T {
        cursor?.let { current ->
            cursor = current.next
            return current.value
        }

        throw NoSuchElementException()
    }
}