package com.example.fyp1

class Comments {
    var commDate: String? = null
    var commTime: String? = null
    var commentContent: String? = null
    var commentID: String? = null
    var patientID: String? = null
    var rating: String? = null
    var status: String? = null

    constructor(
        commDate: String?,
        commTime: String?,
        commentContent: String?,
        commentID: String?,
        patientID: String?,
        rating: String?,
        status: String?
    ) {
        this.commDate = commDate
        this.commTime = commTime
        this.commentContent = commentContent
        this.commentID = commentID
        this.patientID = patientID
        this.rating = rating
        this.status = status
    }

    constructor()

    override fun toString(): String {
        return "Comments(commDate=$commDate, commTime=$commTime, commentContent=$commentContent, commentID=$commentID, patientID=$patientID, rating=$rating, status=$status)"
    }

}