package day7.part2


import spock.lang.Specification

class Day7_2Test extends Specification {

    private static final inputFile = [
            new Line("C", "A"),
            new Line("C", "F"),
            new Line("A", "B"),
            new Line("A", "D"),
            new Line("B", "E"),
            new Line("D", "E"),
            new Line("F", "E")
    ]

    def "should calculate available steps"() {
        given:
        def inputFile = [new Line("A", "B")]
        def doneSteps = []
        def work = new Work(0, 0, [], doneSteps)

        when:
        def availableSteps = Day7_2Kt.findAvailableSteps(inputFile, work)

        then:
        availableSteps == ["A"]
    }

    def "should calculate available steps 2"() {
        given:
        def inputFile = [new Line("A", "B")]
        def doneSteps = ["A"]
        def work = new Work(0, 0, [], doneSteps)


        when:
        def availableSteps = Day7_2Kt.findAvailableSteps(inputFile, work)

        then:
        availableSteps == ["B"]
    }

    def "should give C as the starting point"() {
        given:
        def doneSteps = []
        def work = new Work(0, 0, [], doneSteps)


        when:
        def availableSteps = Day7_2Kt.findAvailableSteps(inputFile, work)

        then:
        availableSteps == ["C"]
    }

    def "when you've done C, available steps are A and F"() {
        given:
        def doneSteps = ["C"]
        def work = new Work(0, 0, [], doneSteps)


        when:
        def availableSteps = Day7_2Kt.findAvailableSteps(inputFile, work)

        then:
        availableSteps == ["A", "F"]
    }

    def "when you've done C and A, available steps are B,D,F"() {
        given:
        def doneSteps = ["C", "A"]
        def work = new Work(0, 0, [], doneSteps)


        when:
        def availableSteps = Day7_2Kt.findAvailableSteps(inputFile, work)

        then:
        availableSteps == ["B", "D", "F"]
    }

    def "when you've done C and are doing A, available steps are F"() {
        given:
        def doneSteps = ["C"]
        def doingSteps = [new TaskInProgress("A", 1)]
        def work = new Work(0, 0, doingSteps, doneSteps)


        when:
        def availableSteps = Day7_2Kt.findAvailableSteps(inputFile, work)

        then:
        availableSteps == ["F"]
    }

    def "one worker should take 21 seconds"() {
        given:

        when:
        def timeTaken = Day7_2Kt.calculateTimeTaken(inputFile, 1)

        then:
        timeTaken == 21
    }

    def "two workers should take 15 seconds"() {
        given:

        when:
        def timeTaken = Day7_2Kt.calculateTimeTaken(inputFile, 2)

        then:
        timeTaken == 15
    }

    def "should find the real answer"() {
        given:
        def realInput = Day7_2Kt.getLines(Day7_2Kt.readInput("day7.txt"))

        when:
        def timeTaken = Day7_2Kt.calculateTimeTaken(realInput, 5, 60)

        then:
        timeTaken == 941
    }

    //get all beforeCanBegin + mustBeDone - in a list
    //remove everything from that list that has already been done
    //remove anything which has an unsatisfied dependency (in the done list)


}