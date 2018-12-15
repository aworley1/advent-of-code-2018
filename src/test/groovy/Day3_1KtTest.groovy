import spock.lang.Specification

import static Status.ONE

class Day3_1KtTest extends Specification {
    def "should add one-square claim to board"() {
        given:
        def board = Board.build(10, 10)
        def input = new ParsedInput(new Coords(2, 2), 1, 1)

        when:
        board.addToBoard(input)

        then:
        board.get(2).get(2).status == ONE
        board.countMultiSquares() == 0
    }

    def "should add one-square claim to board twice"() {
        given:
        def board = Board.build(10, 10)
        def input = new ParsedInput(new Coords(2, 2), 1, 1)

        when:
        board.addToBoard(input)
        board.addToBoard(input)

        then:
        board.get(2).get(2).status == Status.MULTI
        board.countMultiSquares() == 1
    }

    def "should add a 5x3 claim to the board"() {
        given:
        def board = Board.build(10, 10)
        def input = new ParsedInput(new Coords(1, 1), 5, 3)

        when:
        board.addToBoard(input)

        then:
        board.printBoard() == """
..........
.ooooo....
.ooooo....
.ooooo....
..........
..........
..........
..........
..........
.........."""
    }

    def "should correctly overlap two squares"() {
        given:
        def board = Board.build(10, 10)
        def input1 = new ParsedInput(new Coords(1, 1), 5, 3)
        def input2 = new ParsedInput(new Coords(4, 2), 5, 3)

        when:
        board.addToBoard(input1)
        board.addToBoard(input2)

        then:
        board.printBoard() == """
..........
.ooooo....
.ooommooo.
.ooommooo.
....ooooo.
..........
..........
..........
..........
.........."""

        board.countMultiSquares() == 4
    }


}
