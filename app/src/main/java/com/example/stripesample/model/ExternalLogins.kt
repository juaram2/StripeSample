package com.example.stripesample.model

data class ExternalLogins (
    val currentLogins: List<ExternalLoginInfo>? = null,
    val otherLogins: List<AvailableLoginInfo>? = null,
    var canRemoveCurrentLogin: Boolean?
)
