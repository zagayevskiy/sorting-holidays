package com.zagayevskiy.holidays.sort.randomaccess.tree

import com.zagayevskiy.holidays.collection.trees.NaiveSearchTree
import com.zagayevskiy.holidays.collection.trees.Tree
import com.zagayevskiy.holidays.collection.trees.extensions.asDfsIterable
import com.zagayevskiy.holidays.collection.trees.extensions.insertAll
import com.zagayevskiy.holidays.sort.randomaccess.RandomAccessSort

class TreeSort(private val mode: Mode) : RandomAccessSort {

    override val name: String = "${mode.namePrefix} tree sort"
    override val declaredStability = true

    enum class Mode(val namePrefix: String) {
        Naive("Naive"),
    }

    override fun <T> sort(list: MutableList<T>, comparator: Comparator<T>, additionalMemoryConstructor: (List<T>) -> MutableList<T>) {
        val tree: Tree<T> = when (mode) {
            Mode.Naive -> NaiveSearchTree(comparator)
        }

        tree.insertAll(list)
        list.clear()
        list.addAll(tree.asDfsIterable())
    }
}