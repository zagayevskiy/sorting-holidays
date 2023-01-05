package com.zagayevskiy.holidays.collection.trees

class NaiveSearchTree<T>(private val comparator: Comparator<T>) : Tree<T> {

    class Node<T>(
        override val value: T,
        override var left: Node<T>? = null,
        override var right: Node<T>? = null,
    ) : Tree.Node<T>

    override var root: Node<T>? = null
        private set

    override fun insert(value: T) {
        val localRoot = root
        if (localRoot == null) {
            root = Node(value)
            return
        }
        val queue = ArrayDeque<Node<T>>()
        queue.addFirst(localRoot)

        while (queue.isNotEmpty()) {
            val node = queue.removeLast()
            if (comparator.compare(value, node.value) < 0) {
                val left = node.left
                if (left == null) {
                    node.left = Node(value)
                    return
                }

                queue.addFirst(left)
            } else {
                val right = node.right
                if (right == null) {
                    node.right = Node(value)
                    return
                }

                queue.addFirst(right)
            }
        }
    }
}