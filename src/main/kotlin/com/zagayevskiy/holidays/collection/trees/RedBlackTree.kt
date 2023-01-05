package com.zagayevskiy.holidays.collection.trees

class RedBlackTree<T>(private val comparator: Comparator<T>) : Tree<T> {
    class Node<T>(
        override val value: T,
        override var left: Node<T>? = null,
        override var right: Node<T>? = null,
        var parent: Node<T>?,
        black: Boolean = true,
    ) : Tree.Node<T> {
        var black: Boolean = black
            private set

        val red: Boolean
            get() = !black

        fun setBlack() {
            black = true
        }

        fun setRed() {
            black = false
        }

        override fun toString(): String {
            return "[${if(black) "B" else "R"} $value ${left ?: "n"} ${right ?: "n"}]"
        }
    }

    override var root: Node<T>? = null
        private set

    override fun insert(value: T) {
        val localRoot = root
        if (localRoot == null) {
            root = Node(value, parent = null)
            return
        }
        var parent: Node<T>?
        var cursor = root
        while (cursor != null) {
            parent = cursor
            if (comparator.compare(value, cursor.value) < 0) {
                cursor = cursor.left
                if (cursor == null) {
                    val insertedNode = Node(value, parent = parent, black = false)
                    parent.left = insertedNode
                    fixAfterInsert(insertedNode)
                    return
                }
            } else {
                cursor = cursor.right
                if (cursor == null) {
                    val insertedNode = Node(value, parent = parent, black = false)
                    parent.right = insertedNode
                    fixAfterInsert(insertedNode)
                    return
                }
            }
        }
    }

    private fun fixAfterInsert(insertedNode: Node<T>) {
        var x = insertedNode

        while (x.parent?.red == true) {

            var xParent = x.parent!!
            var xGrandParent = xParent.parent

            if (xParent === xGrandParent?.left) {
                val y = xGrandParent.right
                if (y?.red == true) {
                    xParent.setBlack()
                    y.setBlack()
                    xGrandParent.setRed()
                    x = xGrandParent
                } else {
                    if (x === xParent.right) {
                        x = xParent
                        x.rotateLeft()
                        xParent = x.parent!!
                        xGrandParent = xParent.parent
                    }
                    xParent.setBlack()
                    xGrandParent?.setRed()
                    xGrandParent?.rotateRight()
                }
            } else {
                val y = xGrandParent?.left
                if (y?.red == true) {
                    xParent.setBlack()
                    y.setBlack()
                    xGrandParent!!.setRed()
                    x = xGrandParent
                } else {
                    if (x === xParent.left) {
                        x = xParent
                        x.rotateRight()
                        xParent = x.parent!!
                        xGrandParent = xParent.parent
                    }
                    xParent.setBlack()
                    xGrandParent?.setRed()
                    xGrandParent?.rotateLeft()
                }
            }
        }

        root!!.setBlack()
    }

    private fun Node<T>.rotateLeft() {
        val r = right
        val rLeft = r?.left
        right = rLeft
        rLeft?.parent = this

        val p = parent
        r?.parent = p
        when {
            p == null -> root = r
            p.left === this -> p.left = r
            else -> p.right = r
        }
        r?.left = this
        parent = r
    }

    private fun Node<T>.rotateRight() {
        val l = left
        val lRight = l?.right
        left = lRight
        lRight?.parent = this

        val p = parent
        l?.parent = p
        when {
            p == null -> root = l
            p.right === this -> p.right = l
            else -> p.left = l
        }
        l?.right = this
        parent = l
    }
}