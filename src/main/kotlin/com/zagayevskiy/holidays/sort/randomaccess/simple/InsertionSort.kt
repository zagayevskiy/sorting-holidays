package com.zagayevskiy.holidays.sort.randomaccess.simple

import com.zagayevskiy.holidays.sort.randomaccess.RandomAccessSort

class InsertionSort : RandomAccessSort {
    override val name = "Insertion sort"
    override val declaredStability = true
    override fun <T> sort(list: MutableList<T>, comparator: Comparator<T>, additionalMemoryConstructor: (List<T>) -> MutableList<T>) {
        for (i in 1 until list.size) {
            val current = list[i]
            val insertIndex = list.binarySearchForInsert(current, fromInclusive = 0, toExclusive = i, comparator)
            if (i != insertIndex) {
                for (j in i - 1 downTo insertIndex) {
                    list[j + 1] = list[j]
                }
                list[insertIndex] = current
            }
        }
    }


    private fun <T> List<T>.binarySearchForInsert(
        insertValue: T,
        fromInclusive: Int,
        toExclusive: Int,
        comparator: Comparator<T>
    ): Int {
        var from = fromInclusive
        var to = toExclusive - 1

        while (from < to) {
            val mid = (from + to) / 2
            val cmp = comparator.compare(insertValue, get(mid))

            if (cmp < 0) {
                to = mid.dec()
            } else {
                from = mid.inc()
            }
        }

        return if (to < from) {
            from
        } else {
            if (comparator.compare(insertValue, get(to)) < 0) {
                to
            } else {
                to.inc()
            }
        }
    }
}