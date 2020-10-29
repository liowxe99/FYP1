package com.example.fyp1

class Schedule {
    var endTime: String? = null
    var schDate: String? = null
    var scheduleID: String? = null
    var staffID: String? = null
    var startTime: String? = null
    var status: String? = null

    constructor(
        endTime: String?,
        schDate: String?,
        scheduleID: String?,
        staffID: String?,
        startTime: String?,
        status: String?
    ) {
        this.endTime = endTime
        this.schDate = schDate
        this.scheduleID = scheduleID
        this.staffID = staffID
        this.startTime = startTime
        this.status = status
    }

    constructor()

    override fun toString(): String {
        return "Schedule(endTime=$endTime, schDate=$schDate, scheduleID=$scheduleID, staffID=$staffID, startTime=$startTime, status=$status)"
    }
}