package com.deviprasaddayal.depiciat.models

data class RowPolicyModel (
    var policyNumber: String,
    var name: String,
    var phoneNumber: String,
    var whatsappNumber: String,
    var profilePhotoPath: String,
    var premium: Int,
    var premiumPeriod: Int,
    var lastPremiumPaidOn: Long,
    var pendingDues: Boolean,
    var pendingAmount: Int
)