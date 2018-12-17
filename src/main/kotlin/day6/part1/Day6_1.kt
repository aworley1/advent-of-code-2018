package day6.part1

import java.io.File
import kotlin.math.absoluteValue

fun main() {
    val inputSites = parseInput(readInput())
    val grid = buildGridBigEnoughForAllSquares(inputSites.map { it.square })

    val gridWithNearestSites = GridWithNearestSites.build(grid, inputSites)

    val infiniteAreas = gridWithNearestSites.infiniteAreas()

    val biggestArea = gridWithNearestSites.squares.groupBy { it.nearestSite }
        .map { it.key to it.value.size }
        .filter { it.first != null }
        .filter { !infiniteAreas.contains(it.first!!.id) }
        .maxBy { it.second }

    println(biggestArea)

    true
}

fun readInput(): List<String> {
    return File("day6.txt").readLines()
}

fun buildGridBigEnoughForAllSquares(input: List<Square>): Grid {
    //Add or minus one so there is a border around the grid to capture
    //infinite areas
    val minX = input.map { it.x }.min()!! - 1
    val maxX = input.map { it.x }.max()!! + 1
    val minY = input.map { it.y }.min()!! - 1
    val maxY = input.map { it.y }.max()!! + 1

    return Grid.build(startX = minX, endX = maxX, startY = minY, endY = maxY)
}

fun parseInput(input: List<String>): List<Site> {
    return input.mapIndexed { index, text ->
        val splitText = text.split(",")
        val x = splitText[0].trim().toInt()
        val y = splitText[1].trim().toInt()
        Site(id = index, square = Square(x, y))
    }
}

class Grid(val squares: List<Square>) {
    companion object {
        @JvmStatic
        fun build(startX: Int, startY: Int, endX: Int, endY: Int): Grid {
            val squares = (startX..endX).flatMap { x ->
                (startY..endY).map { y ->
                    Square(x, y)
                }
            }

            return Grid(squares)
        }
    }
}

class GridWithNearestSites(val squares: List<SquareWithNearestSite>) {
    fun printed(): String {
        return squares
            .sortedBy { it.square.x }
            .groupBy { it.square.y }
            .toSortedMap()
            .map { it.value.map { it.display() }.joinToString("") }
            .joinToString("\n", "\n")
    }

    fun infiniteAreas(): List<Int> {
        val minX = squares.map { it.square.x }.min()!!
        val maxX = squares.map { it.square.x }.max()!!
        val minY = squares.map { it.square.y }.min()!!
        val maxY = squares.map { it.square.y }.max()!!

        val outsideSquares =
            squares.filter { it.square.x == minX } +
                    squares.filter { it.square.y == minY } +
                    squares.filter { it.square.x == maxX } +
                    squares.filter { it.square.y == maxY }

        return outsideSquares.mapNotNull { it.nearestSite?.id }.distinct()

    }

    companion object {
        @JvmStatic
        fun build(grid: Grid, sites: List<Site>): GridWithNearestSites {
            return GridWithNearestSites(grid.squares.map {
                SquareWithNearestSite(it, it.nearestSite(sites))
            }
            )
        }
    }
}

data class Site(val id: Int, val square: Square)

data class Square(val x: Int, val y: Int) {
    fun distanceFrom(other: Square): Int {
        return (x - other.x).absoluteValue + (y - other.y).absoluteValue
    }

    fun nearestSite(sites: List<Site>): Site? {
        val sitesSortedByDistance = sites.map { it to distanceFrom(it.square) }
            .sortedBy { it.second }

        if (sitesSortedByDistance[0].second == sitesSortedByDistance[1].second) return null

        return sitesSortedByDistance.first().first
    }

    fun nearestSiteId(sites: List<Site>): Int? = nearestSite(sites)?.id
}

data class SquareWithNearestSite(val square: Square, val nearestSite: Site?) {
    fun display(): String {
        return if (nearestSite == null) "."
        else nearestSite.id.toString()
    }
}