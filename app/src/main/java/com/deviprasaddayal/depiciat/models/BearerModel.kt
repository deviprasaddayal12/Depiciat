package com.deviprasaddayal.depiciat.models

data class BearerModel(
    val policyNumber: String,
    var name: String,
    var age: Int,
    var gender: Int,
    var parentType: Int,
    var parentName: String,
    var phoneNumber: String,
    var whatsappNumber: String,
    var emailId: String
)