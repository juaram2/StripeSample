package com.example.stripesample.service

import com.example.stripesample.model.ExternalLoginInfo
import com.example.stripesample.model.ExternalLogins
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

interface ExternalLoginsApi {
    @GET("api/v1/externalLogins")
    suspend fun apiV1ExternalLoginsGet(): Response<ExternalLogins>

    @POST("api/v1/externalLogins")
    suspend fun apiV1ExternalLoginsPost(@Body externalLoginInfo: ExternalLoginInfo? = null): Response<Unit>

    @HTTP(method = "DELETE", path = "api/v1/externalLogins", hasBody = true)
    suspend fun apiV1ExternalLoginsDelete(@Body externalLoginInfo: ExternalLoginInfo? = null): Response<Unit>
}