package day7.part2

import java.io.File


data class TaskInProgress(
    var step: String,
    private var timeLeft: Int
) {
    fun isComplete() = timeLeft == 0
    fun workForOneSecond() {
        timeLeft -= 1
        if (timeLeft < 0)
            throw IllegalArgumentException("Shouldn't have negative time left!!")
    }
}

fun main() {
    val inputSteps = readInput("day7.txt").getLines()
    val timeTaken = timeTaken(inputSteps = inputSteps, totalNumberOfWorkers = 5, baseTimePerTask = 60)

    println("Answer to puzzle is: $timeTaken")
}

class TasksInProgress {
    private val tasksInProgress: MutableList<TaskInProgress> = mutableListOf()
//    private val doneSteps: MutableList<TaskInProgress> = mutableListOf()

    private val finished
        get() = tasksInProgress.filter { it.isComplete() }

    val size
        get() = tasksInProgress.size

    val steps
        get () = tasksInProgress.map { it.step }

    val finishedSteps
        get() = finished.map { it.step }

    fun removeFinished() = tasksInProgress.removeAll(this.finished)

    fun doOneSecondsWork() = tasksInProgress.forEach { it.workForOneSecond() }

    fun startStep(step: String, baseTimePerTask: Int) {
        tasksInProgress.add(TaskInProgress(step, timeToComplete(step, baseTimePerTask)))
    }
}

@JvmOverloads
fun timeTaken(inputSteps: List<Line>, totalNumberOfWorkers: Int, baseTimePerTask: Int = 0): Int {

    val tasksInProgress = TasksInProgress()
    val doneSteps = mutableListOf<String>()

    var time = 0
    while (weStillHaveWork(inputSteps, doneSteps)) {

        doneSteps.addAll(tasksInProgress.finishedSteps)
        tasksInProgress.removeFinished()

        if (!weStillHaveWork(inputSteps, doneSteps)) break

        //decrement time of steps in progress
        tasksInProgress.doOneSecondsWork()

        //find steps ready to be allocated out
        val availableSteps = findAvailableSteps(inputSteps, doneSteps, tasksInProgress.steps)

        val numberOfFreeWorkers = totalNumberOfWorkers - tasksInProgress.size

        (0 until numberOfFreeWorkers).forEach { index ->
            availableSteps
                .getOrNull(index)
                ?.let {
                    tasksInProgress.startStep(it, baseTimePerTask)
                }
        }

        time++
    }

    return time
}

fun timeToComplete(step: String, baseTime: Int): Int {
    return "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(step) + baseTime
}

fun weStillHaveWork(inputSteps: List<Line>, doneSteps: MutableList<String>): Boolean {
    return (findAllSteps(inputSteps) - doneSteps).isNotEmpty()
}

fun readInput(filename: String): List<String> {
    return File(filename).readLines()
}

@JvmOverloads
fun findAvailableSteps(
    inputLines: List<Line>,
    doneSteps: List<String>,
    stepsInProgress: List<String> = listOf()
): List<String> {
    val stepsToDo = findAllSteps(inputLines) - doneSteps - stepsInProgress

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
