package racetrack

import grails.test.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class RegistrationServiceSpec extends Specification {

    RegistrationService registrationService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Registration(...).save(flush: true, failOnError: true)
        //new Registration(...).save(flush: true, failOnError: true)
        //Registration registration = new Registration(...).save(flush: true, failOnError: true)
        //new Registration(...).save(flush: true, failOnError: true)
        //new Registration(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //registration.id
    }

    void "test get"() {
        setupData()

        expect:
        registrationService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Registration> registrationList = registrationService.list(max: 2, offset: 2)

        then:
        registrationList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        registrationService.count() == 5
    }

    void "test delete"() {
        Long registrationId = setupData()

        expect:
        registrationService.count() == 5

        when:
        registrationService.delete(registrationId)
        sessionFactory.currentSession.flush()

        then:
        registrationService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Registration registration = new Registration()
        registrationService.save(registration)

        then:
        registration.id != null
    }
}
