package com.zagayevskiy.holidays.collection

interface ListStatsCounter {
    var writeCount: Long
    var readCount: Long
}

class CountingMutableList<T> private constructor(private val wrapped: MutableList<T>, private val counterDelegate: ListStatsCounter) :
    MutableList<T> by wrapped,
    ListStatsCounter by counterDelegate {

    constructor(wrapped: MutableList<T>) : this(wrapped, DefaultStateCounter())

    override fun get(index: Int): T {
        readCount++
        return wrapped[index]
    }

    override fun set(index: Int, element: T): T {
        writeCount++
        return wrapped.set(index, element)
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> = CountingMutableList(wrapped.subList(fromIndex, toIndex), this)

    override fun listIterator(): MutableListIterator<T> = CountingMutableListIterator(wrapped.listIterator(), this)

    override fun iterator(): MutableIterator<T> = listIterator()
}

private class CountingMutableListIterator<T>(private val wrapped: MutableListIterator<T>, counterDelegate: ListStatsCounter) :
    MutableListIterator<T> by wrapped,
    ListStatsCounter by counterDelegate {

    override fun next(): T {
        readCount++
        return wrapped.next()
    }

    override fun set(element: T) {
        wrapped.set(element)
        writeCount++
    }
}

private class DefaultStateCounter : ListStatsCounter {
    override var writeCount: Long = 0L
    override var readCount: Long = 0L
}