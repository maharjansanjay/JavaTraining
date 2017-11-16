package racetrack

import grails.test.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class RaceServiceSpec extends Specification {

    RaceService raceService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Race(...).save(flush: true, failOnError: true)
        //new Race(...).save(flush: true, failOnError: true)
        //Race race = new Race(...).save(flush: true, failOnError: true)
        //new Race(...).save(flush: true, failOnError: true)
        //new Race(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //race.id
    }

    void "test get"() {
        setupData()

        expect:
        raceService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Race> raceList = raceService.list(max: 2, offset: 2)

        then:
        raceList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        raceService.count() == 5
    }

    void "test delete"() {
        Long raceId = setupData()

        expect:
        raceService.count() == 5

        when:
        raceService.delete(raceId)
        sessionFactory.currentSession.flush()

        then:
        raceService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Race race = new Race()
        raceService.save(race)

        then:
        race.id != null
    }
}
