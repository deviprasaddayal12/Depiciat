package com.deviprasaddayal.depiciat.models

import com.deviprasaddayal.depiciat.interfaces.PolicyNumber

data class BearerModel(
    val policyNumber: Int,
    var name: String,
    var age: Int,
    var gender: Int,
    var parentType: Int,
    var parentName: String,
    var phoneNumber: String,
    var emailId: String
)