package com.example.wheelchairusers.model

data class Users(
    var name : String? = null,
    var email : String? = null,
    var phone : String? = null,
    var sid : String? = null,
    var uid : String? = null,
    var admin : Boolean? = null,
    var carers : Boolean? = null
)
