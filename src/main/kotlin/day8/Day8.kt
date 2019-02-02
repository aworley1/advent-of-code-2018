package day8

import java.io.File

fun main() {
    println("Sum of metadata: " + parse("day8.txt").metadataTotal)
    println("Value of root node: " + parse("day8.txt").value)
}

fun parse(filename: String): Node {
    val inputIterator = getAllNumbersFromFile(filename).iterator()
    return Node.from(inputIterator)
}

fun getAllNumbersFromFile(filename: String): List<Int> {
    return File(filename).readLines()
        .flatMap { it.split(" ") }
        .map { it.toInt() }
}

data class Node(val metadataEntries: List<Int>, val children: List<Node>) {
    val metadataTotal: Int
        get() {
            return metadataEntries.sum() + children.sumBy { it.metadataTotal }
        }

    val value: Int
        get() {
            if (children.isEmpty()) return metadataTotal
            return metadataEntries
                .filter { it != 0 }
                .map { children.getOrNull(it - 1)?.value ?: 0 }
                .sum()
        }

    companion object {
        fun from(iterator: Iterator<Int>): Node {
            val numberOfChildren = iterator.next()
            val numberOfMetadata = iterator.next()
            val children = if (numberOfChildren == 0) emptyList()
            else (1..numberOfChildren).map { from(iterator) }

            val metadata = (1..numberOfMetadata).map { iterator.next() }

            return Node(metadata, children)
        }
    }

}
