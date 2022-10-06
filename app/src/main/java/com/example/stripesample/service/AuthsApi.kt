package com.example.stripesample.service

import com.example.stripesample.model.IdentityToken
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthsApi {
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("/connect/token")
    suspend fun signinWithEmail(@Field("client_id") client_id: String, @Field("client_secret") client_secret: String, @Field("scope") scope: String,
                                @Field("grant_type") grant_type: String, @Field("username") username: String, @Field("password") password: String,): Response<IdentityToken>

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("/connect/token")
    fun refreshToken(@Field("client_id") client_id: String, @Field("client_secret") client_secret: String, @Field("grant_type") grant_type: String, @Field("refresh_token") refresh_token: String): Call<IdentityToken>
}