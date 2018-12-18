import day7.part1.Day7_1Kt
import day7.part1.Dependency
import day7.part1.Entity
import spock.lang.Specification

class Day7_1Test extends Specification {
    def "should add two dependencies"() {
        given:
        def entityA = new Entity("A", [])
        def entityB = new Entity("B", [])
        def entityC = new Entity("C", [])

        when:
        def result = entityA.withDependencies([entityB, entityC])

        then:
        result.dependsOn == [entityB, entityC]
    }

    def "should create list of entities with dependencies"() {
        given:
        def dependency1 = new Dependency("C", "B")
        def dependency2 = new Dependency("B", "A")
        def dependency3 = new Dependency("B", "Z")

        when:
        def result = Day7_1Kt.createEntitiesFromDependencies([dependency1, dependency2, dependency3])

        then:
        result*.id == ["A", "B", "C", "Z"]
        result*.dependsOn*.id == [[], ["A", "Z"], ["B"], []]
    }

    def "should find start of tree"() {
        given:
        def dependency1 = new Dependency("A", "B")

        expect:
        Day7_1Kt.findStartOfTree([dependency1]) == "B"

    }

}