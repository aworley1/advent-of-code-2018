package day7.part2

import java.io.File

data class Worker(
    var task: WorkerTask = Free()
) {
    fun getDoneWork(): String? {
        return when (task) {
            is Free -> null
            is DoingSomething -> if (((task as DoingSomething).isComplete())
        }
    }
}

sealed class WorkerTask {
    abstract fun isComplete() : Boolean
}
data class DoingSomething(
    var stepInProgress: String,
    var timeLeft: Int
) : WorkerTask() {
    fun isComplete(): Boolean {
        return timeLeft == 0
    }
}

class Free : WorkerTask() {
    override fun isComplete() = true
}
//
//data class Worker(
//    var stepInProgress: String? = null,
//    var timeLeft: Int = 0
//) {
//    fun getDoneWork(): String? {
//        if (timeLeft == 0) {
//            val step = stepInProgress
//            stepInProgress = null
//            return step
//        } else return null
//    }
//
//    fun tick() {
//        timeLeft--
//    }
//}

fun main() {
    println(timeTaken(readInput("day7.txt").getLines(), 1))
}

fun timeTaken(inputSteps: List<Line>, numberOfWorkers: Int): Int {

    val workers = (1..numberOfWorkers).map { Worker() }
    var time = 0
    val doneSteps = mutableListOf<String>()

    while (weStillHaveWork(inputSteps, doneSteps)) {
        time++
        //tick workers time
        doneSteps.addAll(workers.mapNotNull { it.getDoneWork() })

        val availableSteps = findAvailableSteps(inputSteps, doneSteps)

    }

    return 21
}

fun weStillHaveWork(inputSteps: List<Line>, doneSteps: MutableList<String>): Boolean {
    return (findAllSteps(inputSteps) - doneSteps).isNotEmpty()
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
