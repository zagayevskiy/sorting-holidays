package com.zagayevskiy.holidays.sort.linkedlist

import com.zagayevskiy.holidays.collection.MutableLinkedList
import com.zagayevskiy.holidays.collection.MutableLinkedList.Node
import com.zagayevskiy.holidays.sort.NamedSort

interface LinkedListSort : NamedSort {
    override val declaredStability get() = false //TODO
    fun <T> sort(items: MutableLinkedList<T>, comparator: Comparator<T>, temporaryNodeBuilder: (child: Node<T>?) -> Node<T> = ::TemporaryNode)
}

class TemporaryNode<T>(override var next: Node<T>?) : Node<T> {
    override val value: T
        get() = badIdea()

    override fun iterator(): Iterator<T> = badIdea()
}

private fun badIdea(): Nothing = throw IllegalStateException("It's bad idea to do that with temporary node")