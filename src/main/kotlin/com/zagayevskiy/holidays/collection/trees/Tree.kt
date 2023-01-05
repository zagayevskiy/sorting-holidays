package com.zagayevskiy.holidays.collection.trees

interface Tree<TValue> {
    interface Node<out TValue> {
        val value: TValue
        val left: Node<TValue>?
        val right: Node<TValue>?
    }

    val root: Node<TValue>?

    fun insert(value: TValue)
}