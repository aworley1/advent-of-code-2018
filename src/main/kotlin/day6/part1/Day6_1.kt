package day6.part1

import java.io.File
import kotlin.math.absoluteValue

fun main() {
    val inputSites = parseInput(readInput())
    val grid = buildGridBigEnoughForAllSquares(inputSites.map { it.square })

    val gridWithNearestSites = GridWithNearestSites.build(grid, inputSites)

    true
}

fun readInput(): List<String> {
    return File("day6.txt").readLines()
}

fun buildGridBigEnoughForAllSquares(input: List<Square>): Grid {
    val minX = input.map { it.x }.min()!!
    val maxX = input.map { it.x }.max()!!
    val minY = input.map { it.y }.min()!!
    val maxY = input.map { it.y }.min()!!

    return Grid.build(minX, maxX, minY, maxY)
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
            val squares = (startX..startX + endX).flatMap { x ->
                (startY..startY + endY).map { y ->
                    Square(x, y)
                }
            }

            return Grid(squares)
        }
    }
}

class GridWithNearestSites(val squares: List<SquareWithNearestSite>) {
    companion object {
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

data class SquareWithNearestSite(val square: Square, val nearestSite: Site?)