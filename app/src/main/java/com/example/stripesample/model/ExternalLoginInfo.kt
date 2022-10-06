package com.example.stripesample.model

import com.squareup.moshi.Json

data class ExternalLoginInfo (
        @Json(name = "loginProvider")
        val loginProvider: kotlin.String? = null,
        @Json(name = "providerKey")
        val providerKey: kotlin.String? = null,
        @Json(name = "providerDisplayName")
        val providerDisplayName: kotlin.String? = null
)
