package day4.part1

import java.io.File
import java.time.Instant

fun readInput(): List<String> {
    return File("day4.txt").readLines()
}

fun main() {
    readInput().parseLines()

}

private fun List<String>.parseLines(): List<ParsedInput> {
    return this.map { ParsedInputBuilder.from(it) }
}

class ParsedInput(val timestamp: Instant, val restOfLine: String) {
}

object ParsedInputBuilder {
    fun from(input: String): ParsedInput {
        val timeString = input.substring(1, 17).replace(" ", "T") + ":00Z"

        val restOfLine = input.substring(19)
        return ParsedInput(
            timestamp = Instant.parse(timeString),
            restOfLine = restOfLine
        )
    }
}