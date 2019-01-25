package day7.part1

import java.io.File

fun main() {
    println(solvePuzzle("day7.txt"))
}

fun solvePuzzle(filename: String): String {
    val puzzleInput = readInput(filename)

    val startingSteps = findStart(puzzleInput)

    val availableSteps = mutableSetOf<String>()
    availableSteps.addAll(startingSteps)
    val outputList = mutableListOf<String>()

    while (availableSteps.isNotEmpty()) {
        doSteps(availableSteps, outputList, puzzleInput)
    }
    return outputList.joinToString("")
}

fun readInput(filename: String): List<String> {
    return File(filename).readLines()
}

fun findStart(inputLines: List<String>): List<String> {
    val stepsBefore = inputLines.getLines()
        .map { it.mustBeFinished }
        .distinct()

    val stepsAfter = inputLines.getLines()
        .map { it.beforeCanBegin }
        .distinct()

    return (stepsBefore - stepsAfter).sorted()

}

fun List<String>.getLines(): List<Line> {
    return this
        .map { it.split(" ") }
        .map { Line(it[1], it[7]) }
}

data class Line(val mustBeFinished: String, val beforeCanBegin: String)

fun doSteps(
    availableSteps: MutableSet<String>,
    outputList: MutableList<String>,
    inputLines: List<String>
) {
    //use the first available step
    val stepBeingDone = availableSteps.sorted().first()

    //find all steps that come after
    val newlyAvailableSteps = inputLines.findStepsAfterThisOne(stepBeingDone)

    //check these are really available and add these newly available steps
    val reallyAvailableSteps = filterToReallyAvailableSteps(stepBeingDone, inputLines, newlyAvailableSteps, outputList)
    availableSteps.addAll(reallyAvailableSteps)

    //remove this step from available steps and add to outputList
    availableSteps.remove(stepBeingDone)
    outputList.add(stepBeingDone)
}

fun filterToReallyAvailableSteps(
    stepJustDone: String,
    inputLines: List<String>,
    newlyAvailableSteps: List<String>,
    outputList: MutableList<String>
): List<String> {
    val doneSteps = outputList + stepJustDone

    return newlyAvailableSteps.map { it to getAllThingsBeforeIt(it, inputLines) }
        .filter { doneSteps.containsAll(it.second) }
        .map { it.first }

    //for each newly available step, get its dependencies
    //check if all of its dependencies are in the outputlist + this letter
    //output it

}

fun getAllThingsBeforeIt(letter: String, inputLines: List<String>): List<String> {
    return inputLines.getLines().filter { letter == it.beforeCanBegin }.map { it.mustBeFinished }
}

private fun List<String>.findStepsAfterThisOne(letter: String): List<String> {
    return this.getLines().filter { it.mustBeFinished == letter }
        .map { it.beforeCanBegin }
}

