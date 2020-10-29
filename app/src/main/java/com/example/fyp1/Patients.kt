package com.example.fyp1

class Patients {
    var NRIC: String? = null
    var address: String? = null
    var dateOfBirth: String? = null
    var email: String? = null
    var gender: String? = null
    var loginPw: String? = null
    var name: String? = null
    var patientID: String? = null
    var phoneNo: String? = null
    var status: String? = null
    var userID: String? = null

    constructor(
        NRIC: String?,
        address: String?,
        dateOfBirth: String?,
        email: String?,
        gender: String?,
        loginPw: String?,
        name: String?,
        patientID: String?,
        phoneNo: String?,
        status: String?,
        userID: String?
    ) {
        this.NRIC = NRIC
        this.address = address
        this.dateOfBirth = dateOfBirth
        this.email = email
        this.gender = gender
        this.loginPw = loginPw
        this.name = name
        this.patientID = patientID
        this.phoneNo = phoneNo
        this.status = status
        this.userID = userID
    }

    constructor() {}


}