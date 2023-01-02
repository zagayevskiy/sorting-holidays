package com.zagayevskiy.holidays.collection

import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class CountingMutableListTest {

    private lateinit var list: MutableList<Int>
    private lateinit var countingList: CountingMutableList<Int>

    @BeforeTest
    fun setup() {
        list = (0..20).toMutableList()
        countingList = list.asCountingList()
    }

    @Test
    fun `reads by iteration`() {
        // just iterate over list to trigger link and value read counting
        countingList.sum()
        val additional = list.size / 3
        countingList.take(additional).sum()

        val expectedReads = list.size.toLong() + additional

        assertEquals(expectedReads, countingList.readCount)
    }

    @Test
    fun `manual reads`() {
        val manualReads = list.size / 2
        for (i in 0 until manualReads) {
            countingList[i]
        }

        assertEquals(manualReads.toLong(), countingList.readCount)
    }

    @Test
    fun `subList reads`() {
        val subList = countingList.subList(list.size / 5, list.size / 2)

        for (i in subList.indices) subList[i]

        assertEquals(subList.size.toLong(), countingList.readCount)
    }

    @Test
    fun writes() {
        for (i in list.indices) countingList[i] = 0

        val expectedWrites = list.size.toLong()

        assertEquals(expectedWrites, countingList.writeCount)
    }

    @Test
    fun `subList writes`() {
        val subList = countingList.subList(list.size / 6, list.size)
        for (i in subList.indices) subList[i] = 0

        assertEquals(subList.size.toLong(), countingList.writeCount)
    }
}