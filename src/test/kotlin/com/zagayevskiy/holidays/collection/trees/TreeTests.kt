package com.zagayevskiy.holidays.collection.trees

import com.zagayevskiy.holidays.collection.trees.extensions.asDfsIterable
import com.zagayevskiy.holidays.collection.trees.extensions.insertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import java.util.stream.StreamSupport
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TreeTests {
    companion object {

        @JvmStatic
        private fun testCases() = treeBuilders() * data()

        @JvmStatic
        private fun data() = listOf(
            emptyList(),
            listOf("q"),
            listOf("q", "a"),
            listOf("a", "q"),
            listOf("q", "a", "w"),
            listOf("1", "2", "3"),
            listOf("3", "2", "1"),
            listOf("s", "q", "a", "w", "a"),
            listOf("q", "w", "a", "w", "a", "qwerty"),
            listOf("q", "a", "w", "a", "qwerty", "uiop"),
            (0..10).shuffled(Random(1L)).map { it.toString() },
            ((0..100) + (0..100)).shuffled(Random(1L)).map { it.toString() },
            (0..1000).shuffled(Random(1L)).map { it.toString() },
        )

        private fun treeBuilders() = listOf<TreeBuilder>(
            { RedBlackTree(compareBy { it }) },
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

    @ParameterizedTest
    @MethodSource("data")
    fun checkRbTreeRequirements(list: List<String>) {
        with(RedBlackTree<String>(compareBy { it })) {
            insertAll(list)
            assertRootBlack()
            assertChildrenOfRedIsBlack()
            assertAllPathsContainsSameBlackNodesCount()
        }
    }

    private fun RedBlackTree<*>.assertRootBlack() {
        assertTrue { root?.black ?: true }
    }

    private fun RedBlackTree<*>.assertChildrenOfRedIsBlack() {
        val stack = ArrayDeque<RedBlackTree.Node<*>>()
        root?.let(stack::addLast)
        while (stack.isNotEmpty()) {
            val node = stack.removeLast()
            if (node.red) {
                assertTrue(node.left?.black ?: true)
                assertTrue(node.right?.black ?: true)
            }
            node.left?.let(stack::addLast)
            node.right?.let(stack::addLast)
        }
    }

    private fun RedBlackTree<*>.assertAllPathsContainsSameBlackNodesCount() {
        root?.countAndCheckBlackNodes()
    }

    private fun RedBlackTree.Node<*>.countAndCheckBlackNodes(): Int {
        val leftCount = left?.countAndCheckBlackNodes() ?: 0
        val rightCount = right?.countAndCheckBlackNodes() ?: 0

        assertEquals(leftCount, rightCount, "black nodes count in left and right must be same")

        return if (black) {
            leftCount + 1;
        } else {
            leftCount
        }
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
