package racetrack

import grails.gorm.services.Service

@Service(Race)
interface RaceService {

    Race get(Serializable id)

    List<Race> list(Map args)

    Long count()

    void delete(Serializable id)

    Race save(Race race)

}