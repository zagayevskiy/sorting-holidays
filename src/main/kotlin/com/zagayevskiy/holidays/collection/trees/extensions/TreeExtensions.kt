package com.zagayevskiy.holidays.collection.trees.extensions

import com.zagayevskiy.holidays.collection.trees.Tree
import com.zagayevskiy.holidays.collection.trees.Tree.Node

fun <TValue> Tree<TValue>.insertAll(iterable: Iterable<TValue>) {
    iterable.forEach(::insert)
}

fun <TValue> Tree<TValue>.asDfsIterable(): Iterable<TValue> {
    class Visited(val node: Node<TValue>, var leftVisited: Boolean = false)

    fun ArrayDeque<Visited>.push(node: Node<TValue>) = addLast(Visited(node))
    fun ArrayDeque<Visited>.peek() = last()
    fun ArrayDeque<Visited>.pop() = removeLast()

    return object : Iterable<TValue> {
        override fun iterator(): Iterator<TValue> = iterator {
            val stack = ArrayDeque<Visited>()
            root?.let(stack::push)

            while (stack.isNotEmpty()) {
                val last = stack.peek()
                val lastLeft = last.node.left
                if (last.leftVisited || lastLeft == null) {
                    stack.pop()
                    yield(last.node.value)
                    last.node.right?.let(stack::push)
                } else {
                    last.leftVisited = true
                    stack.push(lastLeft)
                }
            }
        }
    }
}
