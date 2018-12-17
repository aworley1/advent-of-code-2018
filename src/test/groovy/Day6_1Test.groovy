import day6.part1.Day6_1Kt
import day6.part1.Grid
import day6.part1.GridWithNearestSites
import day6.part1.Site
import day6.part1.Square
import spock.lang.Specification

class Day6_1Test extends Specification {
    def "should build a grid of the right size"() {
        expect:
        Grid.build(0, 0, 5, 10).squares.size() == 66
    }

    def "should calculate distance between squares"() {
        given:
        def square1 = new Square(3, 7)
        def square2 = new Square(10, 10)

        expect:
        square1.distanceFrom(square2) == 10
        square2.distanceFrom(square1) == 10
    }

    def "should parse input"() {
        given:
        def input = [" 81, 46 ", " 330, 289 "]

        when:
        def result = Day6_1Kt.parseInput(input)

        then:
        result[0].square.x == 81
        result[0].square.y == 46
        result[1].square.x == 330
        result[1].square.y == 289
    }

    def "should find nearest site for a square"() {
        given:
        def sites = [new Site(1, new Square(2, 2)),
                     new Site(2, new Square(8, 8))]

        expect:
        new Square(3, 3).nearestSiteId(sites) == 1
    }

    def "should return null if more than one site is nearest"() {
        given:
        def sites = [new Site(1, new Square(2, 2)),
                     new Site(2, new Square(4, 4))]

        expect:
        new Square(3, 3).nearestSiteId(sites) == null
    }

    def "should print grid with sites"() {
        given:
        def sites = [new Site(1, new Square(1, 1)),
                     new Site(2, new Square(4, 4))]

        when:
        def grid = Day6_1Kt.buildGridBigEnoughForAllSquares([new Square(1, 1), new Square(4, 4)])
        def gridWithSites = GridWithNearestSites.build(grid, sites)
        def printed = gridWithSites.printed()

        then:
        printed == """
1111..
1111..
111.22
11.222
..2222
..2222"""

    }

    def "should capture infinite areas"() {
        given:
        def sites = [new Site(1, new Square(1, 1)),
                     new Site(2, new Square(4, 4)),
                     new Site(3, new Square(3, 3))]

        when:
        def grid = Day6_1Kt.buildGridBigEnoughForAllSquares([new Square(1, 1), new Square(4, 4), new Square(3,3)])
        def gridWithSites = GridWithNearestSites.build(grid, sites)
        def infiniteAreas = gridWithSites.infiniteAreas()

        then:
        infiniteAreas == [1,2]

    }

}
