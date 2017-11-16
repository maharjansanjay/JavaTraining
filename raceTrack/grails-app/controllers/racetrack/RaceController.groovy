package racetrack

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class RaceController {

    RaceService raceService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond raceService.list(params), model:[raceCount: raceService.count()]
    }

    def show(Long id) {
        respond raceService.get(id)
    }

    def create() {
        respond new Race(params)
    }

    def save(Race race) {
        if (race == null) {
            notFound()
            return
        }

        try {
            raceService.save(race)
        } catch (ValidationException e) {
            respond race.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'race.label', default: 'Race'), race.id])
                redirect race
            }
            '*' { respond race, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond raceService.get(id)
    }

    def update(Race race) {
        if (race == null) {
            notFound()
            return
        }

        try {
            raceService.save(race)
        } catch (ValidationException e) {
            respond race.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'race.label', default: 'Race'), race.id])
                redirect race
            }
            '*'{ respond race, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        raceService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'race.label', default: 'Race'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    def search(){
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'race.label', default: 'Race'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
