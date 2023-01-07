package com.zagayevskiy.holidays.sort.randomaccess.simple

import com.zagayevskiy.holidays.sort.randomaccess.RandomAccessSort

class CycleSort : RandomAccessSort {

    override val name = "Cycle sort"
    override val declaredStability = false

    override fun <T> sort(list: MutableList<T>, comparator: Comparator<T>, additionalMemoryConstructor: (List<T>) -> MutableList<T>) {
        for (cycleStart in 0 until list.size - 1) {

            var currentValue = list[cycleStart]

            var position = list.findPositionFor(currentValue, cycleStart, comparator)
            if (position == cycleStart) continue // Already at place

            while (comparator.compare(currentValue, list[position]) == 0) ++position

            do {
                val tmp = list[position]
                list[position] = currentValue
                currentValue = tmp

                position = list.findPositionFor(currentValue, cycleStart, comparator)
                while (comparator.compare(currentValue, list[position]) == 0) ++position
            } while (position != cycleStart)

            list[cycleStart] = currentValue
        }
    }

    private fun <T> List<T>.findPositionFor(value: T, start: Int, comparator: Comparator<T>): Int = (start + 1 until size).fold(start) { acc, index ->
        if (comparator.compare(get(index), value) < 0) {
            acc + 1
        } else {
            acc
        }
    }
}