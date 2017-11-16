package racetrack

import grails.gorm.services.Service

@Service(Registration)
interface RegistrationService {

    Registration get(Serializable id)

    List<Registration> list(Map args)

    Long count()

    void delete(Serializable id)

    Registration save(Registration registration)

}