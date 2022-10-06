package com.example.stripesample.service

import CloudHospitalApi.infrastructure.ApiClient
import CloudHospitalApi.infrastructure.OffsetDateTimeAdapter
import CloudHospitalApi.infrastructure.UUIDAdapter
import android.util.Log
import com.example.stripesample.utils.AuthInterceptor
import com.example.stripesample.utils.TokenAuthenticator
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


object ApiClients {
    var apiClient: ApiClient
    var identityApiClient: ApiClient

    private val okHttpClientBuilder = OkHttpClient()
        .newBuilder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .authenticator(TokenAuthenticator())
        .addInterceptor(HttpLoggingInterceptor { message -> Log.d("debug", "okHttpClientBuilder: $message") }.apply {
            HttpLoggingInterceptor.Level.BODY
        })

    private val moshi = Moshi.Builder()
        .add(UUIDAdapter())
        .add(OffsetDateTimeAdapter())
        .add(KotlinJsonAdapterFactory())

    init {
        identityApiClient = ApiClient(baseUrl = "https://identity-int.icloudhospital.com", okHttpClientBuilder = okHttpClientBuilder)
        identityApiClient.addAuthorization("oauth", AuthInterceptor())
        apiClient = ApiClient(baseUrl = "https://api-int.icloudhospital.com", okHttpClientBuilder = okHttpClientBuilder)
    }
}