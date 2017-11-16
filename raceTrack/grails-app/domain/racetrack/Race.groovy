package racetrack

class Race {
    String name
    Date startDateTime
    String city
    String state
    Float distance
    Float cost
    Integer maxRunners = 100000

    static hasMany = [registrations:Registration]

    static constraints = {
        maxRunners()

        name(maxSize: 50, blank: false, nullable: false)
        startDateTime( min: new Date(), validator: {return (it > new Date())}, nullable: false)
        city(maxSize: 30, blank: false, nullable: false)
        state(inList: ['GA','NC','SC','VA'], blank: false, nullable: false)
        distance(min:3.1f,max:100f, nullable: false)
        cost(min:0f,max:999.99f, nullable: false)
    }

    String toString() {"${this.name} : ${this.city}, ${this.state}" }

}
