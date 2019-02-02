package day7.part2

import java.io.File

data class Worker(
    var task: WorkerTask? = null
) {
    fun getDoneWork(): String? {
        if (task?.isComplete() == true) {
            val completedTask = task
            task = null
            return completedTask!!.stepInProgress
        }
        return null
    }

    fun tick() {
        task?.tick()
    }

    fun takeWorkIfHaveNone(iterator: Iterator<String>) {
        if (task == null) {
            val nextTask = iterator.nextOrNull()
            if (nextTask != null) {
                task = WorkerTask.from(nextTask)
            }
        }
    }

    fun status(): String {
        return task?.stepInProgress ?: " "
    }
}

private fun <T> Iterator<T>.nextOrNull(): T? {
    return if (this.hasNext()) this.next() else null
}

data class WorkerTask(
    var stepInProgress: String,
    var timeLeft: Int
) {
    fun isComplete(): Boolean {
        return timeLeft == 0
    }

    fun tick() {
        timeLeft--
    }

    companion object {
        @JvmStatic
        fun from(letter: String): WorkerTask {
            return WorkerTask(letter, timeForLetter(letter))
        }

        private fun timeForLetter(letter: String): Int {
            val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

            return alphabet.indexOf(letter, 0, true) + 1
        }
    }
}

fun main() {
    println(timeTaken(readInput("day7.txt").getLines(), 1))
}

fun timeTaken(inputSteps: List<Line>, numberOfWorkers: Int): Int {

    val workers = (1..numberOfWorkers).map { Worker() }
    var time = 0
    val doneSteps = mutableListOf<String>()

    while (weStillHaveWork(inputSteps, doneSteps)) {
        //tick workers time
        workers.forEach { it.tick() }
        doneSteps.addAll(workers.mapNotNull { it.getDoneWork() })

        val availableSteps = findAvailableSteps(inputSteps, doneSteps) - workers.stepsInProgress()
        val availableStepsIterator = availableSteps.iterator()
        //allocate available steps to the workers

        workers.forEach { it.takeWorkIfHaveNone(availableStepsIterator) }

        println("Second: ${time}, ${workers.statuses()} Done: ${doneSteps.joinToString("")}")
        time++
    }

    return time - 1
}

private fun List<Worker>.stepsInProgress(): List<String> {
    return this.mapNotNull { it.task?.stepInProgress }
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

fun List<Worker>.statuses(): String {
    return this.map { "Status: " + it.status() }
        .joinToString(" ")
}
