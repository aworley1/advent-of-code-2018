package day7.part2

import java.io.File


fun main() {
    val timeTaken = calculateTimeTaken(
        inputSteps = readInput("day7.txt").getLines(),
        totalNumberOfWorkers = 5,
        baseTimePerTask = 60
    )

    println("Answer to puzzle is: $timeTaken")
}

@JvmOverloads
fun calculateTimeTaken(inputSteps: List<Line>, totalNumberOfWorkers: Int, baseTimePerTask: Int = 0): Int {

    val work = Work(totalNumberOfWorkers, baseTimePerTask)
    var timer = 0

    while (work.isNotDone(inputSteps)) {
        work.completeFinishedSteps()
        if (work.isAllDone(inputSteps)) break
        timer++
        work.doOneSecondsWork()
        work.allocateNewTasksToWaitingWorkers(inputSteps)
    }

    return timer
}

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

class Work(
    private val totalNumberOfWorkers: Int,
    private val baseTimePerTask: Int,
    private val tasksInProgress: MutableList<TaskInProgress> = mutableListOf(),
    private val done: MutableList<String> = mutableListOf()
) {
    fun doOneSecondsWork() = tasksInProgress.forEach { it.workForOneSecond() }

    fun completeFinishedSteps() {
        done.addAll(finishedSteps)
        tasksInProgress.removeAll(finished)
    }

    fun isAllDone(inputLines: List<Line>): Boolean = (findAllSteps(inputLines) - doneSteps).isEmpty()

    fun isNotDone(inputLines: List<Line>): Boolean = !isAllDone(inputLines)

    fun allocateNewTasksToWaitingWorkers(inputSteps: List<Line>) {
        val availableSteps = findAvailableSteps(inputSteps, this)
        val numberOfFreeWorkers = totalNumberOfWorkers - tasksInProgress.size

        (0 until numberOfFreeWorkers).forEach { index ->
            availableSteps
                .getOrNull(index)
                ?.let {
                    startStep(it, baseTimePerTask)
                }
        }

    }

    fun areTheseStepsAllDone(steps: List<String>) = doneSteps.containsAll(steps)

    val stepsDoneOrInProgress
        get() = doneSteps + stepsInProgress

    private val doneSteps
        get() = done.toList()

    private val stepsInProgress
        get () = tasksInProgress.map { it.step }

    private fun startStep(step: String, baseTimePerTask: Int) {
        tasksInProgress.add(TaskInProgress(step, timeNeededToCompleteStep(step, baseTimePerTask)))
    }

    private val finished
        get() = tasksInProgress.filter { it.isComplete() }

    private val finishedSteps
        get() = finished.map { it.step }

    private fun timeNeededToCompleteStep(step: String, baseTime: Int): Int {
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(step) + baseTime
    }

}

fun readInput(filename: String): List<String> {
    return File(filename).readLines()
}

fun findAvailableSteps(
    inputLines: List<Line>,
    work: Work
): List<String> {
    val stepsToDo = findAllSteps(inputLines) - work.stepsDoneOrInProgress

    return stepsToDo
        .map { it to getAllThingsBeforeIt(it, inputLines) }
        .filter { (_, dependencies) -> work.areTheseStepsAllDone(dependencies) }
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
