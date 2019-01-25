import day7.part1.Day7_1Kt
import spock.lang.Specification

class Day7_1Test extends Specification {
    def "should solve the sample problem correctly"() {
        expect:
        Day7_1Kt.solvePuzzle("day7_sample.txt") == "CABDFE"
    }

    def "should use the first alphabetical available step"() {
        expect:
        Day7_1Kt.solvePuzzle("day7_sample.txt").startsWith("CA")
    }

    def "should do the next step next"() {
        expect:
        Day7_1Kt.solvePuzzle("day7_sample.txt").startsWith("CAB")
    }

    def "should find the first step to be done"() {
        given:
        def inputLines = Day7_1Kt.readInput("day7_sample.txt")

        when:
        def result = Day7_1Kt.findStart(inputLines)

        then:
        result == ["C"]
    }
}