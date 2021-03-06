import day7.part2.Day7_2Kt
import day7.part2.Line
import day7.part2.WorkerTask
import spock.lang.Specification

class Day7_2Test extends Specification {

    def "should calculate available steps"() {
        given:
        def inputFile = [new Line("A", "B")] // lines
        def doneSteps = [] // letters

        when:
        def availableSteps = Day7_2Kt.findAvailableSteps(inputFile, doneSteps)

        then:
        availableSteps == ["A"]
    }

    def "should calculate available steps 2"() {
        given:
        def inputFile = [new Line("A", "B")] // lines
        def doneSteps = ["A"] // letters

        when:
        def availableSteps = Day7_2Kt.findAvailableSteps(inputFile, doneSteps)

        then:
        availableSteps == ["B"]
    }

    def "should give C as the starting point"() {
        given:
        def inputFile = [
                new Line("C", "A"),
                new Line("C", "F"),
                new Line("A", "B"),
                new Line("A", "D"),
                new Line("B", "E"),
                new Line("D", "E"),
                new Line("F", "E")
        ]

        def doneSteps = [] // letters

        when:
        def availableSteps = Day7_2Kt.findAvailableSteps(inputFile, doneSteps)

        then:
        availableSteps == ["C"]
    }

    def "when you've done C, available steps are A and F"() {
        given:
        def inputFile = [
                new Line("C", "A"),
                new Line("C", "F"),
                new Line("A", "B"),
                new Line("A", "D"),
                new Line("B", "E"),
                new Line("D", "E"),
                new Line("F", "E")
        ]

        def doneSteps = ["C"]

        when:
        def availableSteps = Day7_2Kt.findAvailableSteps(inputFile, doneSteps)

        then:
        availableSteps == ["A", "F"]
    }

    def "when you've done C and A, available steps are B,D,F"() {
        given:
        def inputFile = [
                new Line("C", "A"),
                new Line("C", "F"),
                new Line("A", "B"),
                new Line("A", "D"),
                new Line("B", "E"),
                new Line("D", "E"),
                new Line("F", "E")
        ]

        def doneSteps = ["C", "A"]

        when:
        def availableSteps = Day7_2Kt.findAvailableSteps(inputFile, doneSteps)

        then:
        availableSteps == ["B", "D", "F"]
    }

    def "one worker should take 21 seconds"() {
        given:
        def inputFile = [
                new Line("C", "A"),
                new Line("C", "F"),
                new Line("A", "B"),
                new Line("A", "D"),
                new Line("B", "E"),
                new Line("D", "E"),
                new Line("F", "E")
        ]

        when:
        def timeTaken = Day7_2Kt.timeTaken(inputFile, 1)

        then:
        timeTaken == 21
    }

    def "two workers should take 15 seconds"() {
        given:
        def inputFile = [
                new Line("C", "A"),
                new Line("C", "F"),
                new Line("A", "B"),
                new Line("A", "D"),
                new Line("B", "E"),
                new Line("D", "E"),
                new Line("F", "E")
        ]

        when:
        def timeTaken = Day7_2Kt.timeTaken(inputFile, 2)

        then:
        timeTaken == 15
    }

    //get all beforeCanBegin + mustBeDone - in a list
    //remove everything from that list that has already been done
    //remove anything which has an unsatisfied dependency (in the done list)

    def "letter C should take 3 seconds"() {
        when:
        def task = WorkerTask.from("C")

        then:
        task.stepInProgress == "C"
        task.timeLeft == 3

    }

}