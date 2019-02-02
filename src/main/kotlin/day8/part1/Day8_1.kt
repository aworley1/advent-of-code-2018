package day8.part1

import java.io.File

fun main() {
    println(parse("day8.txt").metadataTotal)
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

    companion object {
        fun from(iterator: Iterator<Int>): Node {
            val numberOfChildren = iterator.next()
            val numberOfMetadata = iterator.next()
            val children = if (numberOfChildren == 0) emptyList()
            else (1..numberOfChildren).map { Node.from(iterator) }

            val metadata = (1..numberOfMetadata).map { iterator.next() }

            return Node(metadata, children)
        }
    }
}
