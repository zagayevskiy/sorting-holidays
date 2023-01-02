package com.zagayevskiy.holidays.collection

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CountingMutableLinkedListTest {
    @Test
    fun reads() {
        val list = listOf(1, 2, 3, 4, 5)
        val ll = CountingMutableLinkedList(list.asMutableLinkedList())

        // just iterate over list to trigger link and value read counting
        ll.sum()
        val additional = list.size / 2
        ll.take(additional).sum()

        val expectedReads = list.size.toLong() + additional

        assertEquals(expectedReads, ll.readCount)
        assertEquals(expectedReads, ll.valueReadCount)
    }


    @Test
    fun `link reads don't touches values counter`() {
        val list = listOf(1, 2, 3, 4, 5)
        val ll = CountingMutableLinkedList(list.asMutableLinkedList())

        var cursor = ll.head
        while (cursor != null) cursor = cursor.next // just iterate over list to trigger link read counting. Don't touch values!

        assertEquals(list.size.toLong(), ll.readCount)
        assertEquals(0L, ll.valueReadCount)
    }

    @Test
    fun writes() {
        val list = listOf(1, 2, 3, 4, 5)
        val ll = CountingMutableLinkedList(list.asMutableLinkedList())

        var cursor = ll.head
        while(cursor != null) {
            cursor.next = cursor.next // just relink list to trigger write counting
            cursor = cursor.next
        }

        assertEquals(list.size.toLong(), ll.writeCount)
    }
}