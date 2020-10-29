package com.example.fyp1

class StaffDesc {
    var desc: String? = null
    var expYear: Int? = null

    constructor()
    constructor(desc: String?, expYear: Int?) {
        this.desc = desc
        this.expYear = expYear
    }

    override fun toString(): String {
        return "StaffDesc(desc=$desc, expYear=$expYear)"
    }


}