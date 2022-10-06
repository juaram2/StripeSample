package com.example.stripesample.model

data class Account (
    var id: String,
    var userName: String,
    var hasPassword: Boolean,
    var email: String,
    var emailConfirmed: Boolean,
    var phoneNumber: String?,
    var phoneNumberConfirmed: Boolean,
    var twoFactorEnabled: Boolean,
    var lockoutEnd: String?,
    var lockoutEnabled: Boolean,
    var accessFailedCount: Int
)