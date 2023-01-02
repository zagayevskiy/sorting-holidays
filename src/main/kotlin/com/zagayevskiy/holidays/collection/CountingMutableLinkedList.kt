package com.zagayevskiy.holidays.collection

class CountingMutableLinkedList<T>(private val wrapped: MutableLinkedList<T>) : MutableLinkedList<T> {
    var valueReadCount = 0L
        private set
    var readCount = 0L
        private set
    var writeCount = 0L
        private set

    override var head: MutableLinkedList.Node<T>? = wrapped.head?.asCountingNodes()
        set(value) {
            wrapped.head = when (value) {
                null -> null
                is CountingMutableLinkedList<*>.CountingNode<*> -> value.wrapped as MutableLinkedList.Node<T>
                else -> throw IllegalStateException("It's forbidden to insert something other then Counting Node $value ${value.joinToString()}")
            }
            field = value
        }

    private class NextFieldInitialValue<T> : MutableLinkedList.Node<T> {
        override fun iterator(): Iterator<T> = throw IllegalStateException()

        override val value: T
            get() = throw IllegalStateException()
        override var next: MutableLinkedList.Node<T>?
            get() = throw IllegalStateException()
            set(value) {
                throw IllegalStateException()
            }
    }

    inner class CountingNode<T>(val wrapped: MutableLinkedList.Node<T>) : MutableLinkedList.Node<T> {

        override val value: T
            get() {
                valueReadCount++
                return wrapped.value
            }
        override var next: MutableLinkedList.Node<T>? = NextFieldInitialValue()
            get() {
                readCount++
                return field
            }
            set(value) {
                wrapped.next = when (value) {
                    null -> null
                    is CountingMutableLinkedList<*>.CountingNode<*> -> value.wrapped as MutableLinkedList.Node<T>
                    else -> throw IllegalStateException("Why does sort want to insert something? $value ${value.joinToString()}")
                }
                if (field !is NextFieldInitialValue) {
                    writeCount++
                }
                field = value
            }

        override fun iterator(): Iterator<T> = MutableLinkedListNodeIterator(this)
    }

    override fun iterator(): Iterator<T> = MutableLinkedListNodeIterator(head)

    fun MutableLinkedList.Node<T>.asCountingNodes(): CountingNode<T> {
        val resultHead = CountingNode(this@asCountingNodes)

        var resultCursor: CountingNode<T>? = resultHead
        var cursor: MutableLinkedList.Node<T>? = this

        while (cursor != null) {
            cursor = cursor.next
            val next = cursor?.let(::CountingNode)
            resultCursor!!.next = next
            resultCursor = next
        }

        return resultHead
    }
}

