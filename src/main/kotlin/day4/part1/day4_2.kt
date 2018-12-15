package day4.part1

import java.io.File
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

fun readInput(): List<String> {
    return File("day4.txt").readLines()
}

fun main() {
    val shifts = readInput()
        .parseLines()
        .sortedBy { it.timestamp }
        .withGuardNumbers()
        .toShifts()

    val guardWithMostTimeAsleep = shifts.map { it.guardId to it.sleepPeriods.totalTimeAsleep() }
        .groupBy { it.first }
        .map { it.key to it.value.map { it.second }.sum() }
        .maxBy { it.second }!!
        .first

    val minuteMostAsleep = shifts.filter { it.guardId == guardWithMostTimeAsleep }
        .flatMap { it.sleepPeriods.flatMap { it.getMinutesInSleepPeriod() } }
        .groupBy { it }
        .maxBy { it.value.size }!!
        .key

    println("Guard Id: $guardWithMostTimeAsleep")
    println("Minute most asleep: $minuteMostAsleep")

    println(guardWithMostTimeAsleep.replace("#", "").toInt() * minuteMostAsleep)
}

private fun List<ParsedInput>.toShifts(): List<Shift> {
    val outputList = mutableListOf<Shift>()
    var currentShift: Shift? = null

    val iterator = this.listIterator()

    iterator.forEach {
        if (it.type == ParsedInput.Type.BEGINS_SHIFT && currentShift != null) {
            outputList.add(currentShift!!)
        }

        if (it.type == ParsedInput.Type.BEGINS_SHIFT) {
            currentShift = Shift(guardId = it.guard!!, sleepPeriods = mutableListOf())
        } else if (it.type == ParsedInput.Type.FALLS_ASLEEP) {
            currentShift!!.sleepPeriods.add(SleepPeriod(it.timestamp, this[iterator.nextIndex()].timestamp))
        }
    }

    outputList.add(currentShift!!)

    return outputList.toList()
}

class Shift(val guardId: String, val sleepPeriods: MutableList<SleepPeriod>)

class SleepPeriod(val startTime: Instant, val endTime: Instant) {
    fun getMinutesAsleep(): Long {
        return Duration.between(startTime, endTime).toMinutes()
    }

    fun getMinutesInSleepPeriod(): List<Int> {
        val outputList = mutableListOf<Int>()

        var currentMinute = startTime
        while (currentMinute < endTime) {
            outputList.add(currentMinute.atZone(ZoneId.of("UTC")).minute)
            currentMinute += Duration.ofMinutes(1)
        }

        return outputList.toList()
    }
}

private fun List<SleepPeriod>.totalTimeAsleep(): Long {
    return this.map { it.getMinutesAsleep() }.sum()
}

private fun List<ParsedInput>.withGuardNumbers(): List<ParsedInput> {
    val outputList = mutableListOf<ParsedInput>()
    var currentGuard: String? = null

    this.forEach {
        outputList.add(
            if (it.guard != null) {
                currentGuard = it.guard
                it
            } else {
                it.copy(guard = currentGuard)
            }
        )
    }

    return outputList.toList()
}

private fun List<String>.parseLines(): List<ParsedInput> {
    return this.map { ParsedInputBuilder.from(it) }
}

data class ParsedInput(
    val timestamp: Instant,
    val restOfLine: String,
    val type: Type,
    val guard: String?
) {
    enum class Type(val matcher: String) {
        BEGINS_SHIFT("Guard"),
        FALLS_ASLEEP("asleep"),
        WAKES_UP("wakes");

        companion object {
            fun fromInput(input: String): Type {
                return Type.values().first { input.contains(it.matcher) }
            }
        }
    }
}

object ParsedInputBuilder {
    fun from(input: String): ParsedInput {
        val timeString = input.substring(1, 17).replace(" ", "T") + ":00Z"

        val restOfLine = input.substring(19)
        return ParsedInput(
            timestamp = Instant.parse(timeString),
            restOfLine = restOfLine,
            type = ParsedInput.Type.fromInput(input),
            guard = parseGuard(input)
        )
    }

    private fun parseGuard(input: String): String? {
        return """\s#.*?\s"""
            .toRegex()
            .find(input)
            ?.value?.trim()
    }

}