package com.example.fyp1

class Staff {
    var staffID: String? = null
    var position: String? = null
    var name: String? = null
    var gender: String? = null

    constructor(staffID: String?, position: String?, name: String?, gender: String?) {
        this.staffID = staffID
        this.position = position
        this.name = name
        this.gender = gender
    }

    constructor()

    override fun toString(): String {
        return "Staff(staffID=$staffID, position=$position, name=$name, gender=$gender)"
    }


}