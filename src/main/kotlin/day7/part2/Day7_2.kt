package day7.part2

import java.io.File

fun main() {
    println(solvePuzzle("day7.txt"))
}

fun solvePuzzle(filename: String): String {
    val puzzleInput = readInput(filename)
    return ""
}

fun readInput(filename: String): List<String> {
    return File(filename).readLines()
}

fun findAvailableSteps(inputLines: List<Line>, doneSteps: List<String>): List<String> {
    val stepsToDo = findAllSteps(inputLines) - doneSteps

    return stepsToDo
        .map { it to getAllThingsBeforeIt(it, inputLines) }
        .filter { (_, dependencies) -> doneSteps.containsAll(dependencies) }
        .map { (step, _) -> step }
        .sorted()

}


fun findAllSteps(inputLines: List<Line>): List<String> {
    return (inputLines.map { it.beforeCanBegin } + inputLines.map { it.mustBeFinished }).distinct()
}

fun List<String>.getLines(): List<Line> {
    return this
        .map { it.split(" ") }
        .map { Line(it[1], it[7]) }
}

data class Line(val mustBeFinished: String, val beforeCanBegin: String)


fun getAllThingsBeforeIt(letter: String, inputLines: List<Line>): List<String> {
    return inputLines.filter { letter == it.beforeCanBegin }.map { it.mustBeFinished }
}
