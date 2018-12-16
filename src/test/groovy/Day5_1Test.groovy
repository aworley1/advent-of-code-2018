import day5.part1.Day5_2Kt
import spock.lang.Specification

class Day5_1Test extends Specification {
    def "should return the same string if no repeating characters"() {
        when:
        def result = Day5_2Kt.removeRepeaingCharacters("abcdef")

        then:
        result == "abcdef"
    }

    def "should remove a repeating character in different case"() {
        when:
        def result = Day5_2Kt.removeRepeaingCharacters("aAbcdef")

        then:
        result == "bcdef"
    }

    def "should not remove a repeating character in the same case"() {
        when:
        def result = Day5_2Kt.removeRepeaingCharacters("aabcdef")

        then:
        result == "aabcdef"
    }

    def "should not remove a second repeating character in a different case"() {
        when:
        def result = Day5_2Kt.removeRepeaingCharacters("aAabcdef")

        then:
        result == "abcdef"
    }

    def "should remove repeating characters if no more are generated after doing it once"() {
        when:
        def result = Day5_2Kt.keepRemovingRepeatingCharactersUntilAllGone("aAabcdef")

        then:
        result == "abcdef"
    }

    def "should keep recursing to remove repeating characters"() {
        when:
        def result = Day5_2Kt.keepRemovingRepeatingCharactersUntilAllGone("abBAcdef")

        then:
        result == "cdef"
    }
}
