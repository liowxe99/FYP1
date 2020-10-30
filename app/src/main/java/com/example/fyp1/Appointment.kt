package com.example.fyp1

class Appointment {
    var apptID :String? = null
    var apptTime :String? = null
    var apptDate:String? = null
    var scheduleID :String? = null
    var patientID :String? = null
    var apptStatus:String? = null

    constructor(
        apptID: String?,
        apptTime: String?,
        apptDate: String?,
        scheduleID: String?,
        patientID: String?,
        apptStatus: String?
    ) {
        this.apptID = apptID
        this.apptTime = apptTime
        this.apptDate = apptDate
        this.scheduleID = scheduleID
        this.patientID = patientID
        this.apptStatus = apptStatus
    }

    constructor()

    override fun toString(): String {
        return "Appointment(apptID=$apptID, apptTime=$apptTime, apptDate=$apptDate, scheduleID=$scheduleID, patientID=$patientID, apptStatus=$apptStatus)"
    }
}