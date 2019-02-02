import day8.part1.Day8_1Kt
import spock.lang.Specification

class Day8_1Test extends Specification {
    def "should parse the sample data and calculate the correct metadata sum"() {
        when:
        def result = Day8_1Kt.parse("day8_sample.txt")

        then:
        result.metadataEntries == [1, 1, 2]

        result.children.size() == 2
        result.children[0].metadataEntries == [10, 11, 12]
        result.children[0].children == []

        result.children[1].metadataEntries == [2]
        result.children[1].children.size() == 1
        result.children[1].children[0].metadataEntries == [99]

        result.metadataTotal == 138
    }
}
