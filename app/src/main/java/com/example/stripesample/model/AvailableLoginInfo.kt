package com.example.stripesample.model

import com.squareup.moshi.Json

data class AvailableLoginInfo(
    @Json(name = "name")
    val name: kotlin.String? = null,
    @Json(name = "handlerType")
    val handlerType: kotlin.String? = null,
    @Json(name = "displayName")
    val displayName: kotlin.String? = null
)
