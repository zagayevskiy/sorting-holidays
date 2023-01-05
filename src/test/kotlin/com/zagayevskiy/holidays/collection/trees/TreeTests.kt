package com.zagayevskiy.holidays.collection.trees

import com.zagayevskiy.holidays.collection.trees.extensions.asDfsIterable
import com.zagayevskiy.holidays.collection.trees.extensions.insertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import java.util.stream.StreamSupport
import kotlin.random.Random
import kotlin.test.assertEquals

class TreeTests {
    companion object {

        @JvmStatic
        private fun testCases() = treeBuilders() * data()

        private fun data() = listOf(
            emptyList(),
            listOf("q"),
            listOf("q", "a"),
            listOf("q", "a", "w"),
            listOf("q", "a", "w", "a"),
            listOf("q", "a", "w", "a", "qwerty"),
            listOf("q", "a", "w", "a", "qwerty", "uiop"),
            (0..10).shuffled(Random(1L)).map { it.toString() },
            ((0..100) + (0..100)).shuffled(Random(1L)).map { it.toString() },
            (0..1000).shuffled(Random(1L)).map { it.toString() },
        )

        private fun treeBuilders() = listOf<TreeBuilder>(
            { NaiveSearchTree(compareBy { it }) },
        )
    }

    @ParameterizedTest
    @MethodSource("testCases")
    fun isSearchTree(case: TestCase) {
        val tree = case.asTree()

        val expected = case.list.sorted()
        val actual = tree.asDfsIterable().toList()
        assertEquals(expected, actual)

    }
}

private typealias TreeBuilder = () -> Tree<String>

class TestCase(val list: List<String>, val treeBuilder: TreeBuilder) {
    override fun toString(): String {
        return """${treeBuilder().javaClass.simpleName} $list"""
    }
}

private fun TestCase.asTree(): Tree<String> {
    return treeBuilder().apply {
        insertAll(list)
    }
}

private operator fun List<TreeBuilder>.times(data: List<List<String>>): Stream<TestCase> {
    val cases = map { builder ->
        data.map { list -> TestCase(list, builder) }
    }.flatten()

    return StreamSupport.stream(cases.spliterator(), false)
}
