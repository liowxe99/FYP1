package com.example.fyp1

class StaffDescription {
    var staffID: String? = null
    var position: String? = null
    var name: String? = null
    var gender: String? = null
    var desc: String? = null
    var expYear: Int? = null

    constructor(
        staffID: String?,
        position: String?,
        name: String?,
        gender: String?,
        desc: String?,
        expYear: Int?
    ) {
        this.staffID = staffID
        this.position = position
        this.name = name
        this.gender = gender
        this.desc = desc
        this.expYear = expYear
    }

    override fun toString(): String {
        return "StaffDescription(staffID=$staffID, position=$position, name=$name, gender=$gender, desc=$desc, expYear=$expYear)"
    }
}