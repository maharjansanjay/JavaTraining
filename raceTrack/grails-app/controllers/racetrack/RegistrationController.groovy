package racetrack

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class RegistrationController {

    RegistrationService registrationService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond registrationService.list(params), model:[registrationCount: registrationService.count()]
    }

    def show(Long id) {
        respond registrationService.get(id)
    }

    def create() {
        respond new Registration(params)
    }

    def save(Registration registration) {
        if (registration == null) {
            notFound()
            return
        }

        try {
            registrationService.save(registration)
        } catch (ValidationException e) {
            respond registration.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'registration.label', default: 'Registration'), registration.id])
                redirect registration
            }
            '*' { respond registration, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond registrationService.get(id)
    }

    def update(Registration registration) {
        if (registration == null) {
            notFound()
            return
        }

        try {
            registrationService.save(registration)
        } catch (ValidationException e) {
            respond registration.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'registration.label', default: 'Registration'), registration.id])
                redirect registration
            }
            '*'{ respond registration, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        registrationService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'registration.label', default: 'Registration'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'registration.label', default: 'Registration'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
