package day7.part1

import java.io.File

fun main() {
    val dependencies = parseInput(readInput())

    val startingStep = findStartOfTree(dependencies)

    val entities = createEntitiesFromDependencies(dependencies)




}

fun readInput(): List<String> {
    return File("day7.txt").readLines()
}

fun parseInput(input: List<String>): List<Dependency> {
    return input.map {
        val split = it.split(" ")
        Dependency(
            thing = split[1],
            dependsOn = split[7]
        )
    }
}

fun findStartOfTree(dependencies: List<Dependency>): String {
    return (dependencies.map { it.dependsOn }.distinct() - dependencies.map { it.thing }.distinct()).single()
}

fun createEntitiesFromDependencies(input: List<Dependency>): List<Entity> {
    return (input.map { Entity(it.thing) } + input.map { Entity(it.dependsOn) })
        .distinct()
        .sortedBy { it.id }
}

private fun List<Entity>.addDependencies(input: List<Dependency>): List<Entity> {
    return this.map { entity ->
        val depencencyIds = input.filter { it.thing == entity.id }
            .map { it.dependsOn }

        val dependencyEntities = this.filter { depencencyIds.contains(it.id) }

        entity.withDependencies(dependencyEntities)
    }
}

data class Entity(
    val id: String,
    val dependsOn: List<Entity> = emptyList()
) {
    fun withDependencies(dependencies: List<Entity>): Entity {
        return this.copy(dependsOn = dependencies)
    }
}

data class Dependency(val thing: String, val dependsOn: String)
