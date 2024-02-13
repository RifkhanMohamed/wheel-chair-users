package com.example.wheelchairusers.model

import com.google.firebase.database.DatabaseReference

data class Help(
    var user: Users? = null,
    var carer: Users? = null,
    var status: String? = null,
    var latitude: String? = null,
    var longitude: String? = null
)
