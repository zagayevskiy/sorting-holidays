package com.zagayevskiy.holidays.collection

import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals

class MutableLinkedListTest {

    @Test
    fun `created from empty iterable must be empty`() {
        val ll = emptyList<Nothing>().asMutableLinkedList()

        assertEquals(null, ll.head)
    }

    @Test
    fun `size return actual size of`() {
        val iterable = (0..Random.nextInt(100, 1000)).map { Random.nextInt() }

        val ll = iterable.asMutableLinkedList()

        assertEquals(iterable.size, ll.size())
    }

    @Test
    fun `created from iterable must contains all elements`() {
        val iterable = listOf("1", "q", "a", "s", "d", "w", "qwerty", "123", "WASP", "uiop")

        val ll = iterable.asMutableLinkedList()

        assertEquals(iterable.size, ll.size())

        var cursor = ll.head
        for (i in iterable) {
            assertEquals(i, cursor!!.value)
            cursor = cursor.next
        }
    }
}