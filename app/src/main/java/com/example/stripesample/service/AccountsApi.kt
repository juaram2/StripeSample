package com.example.stripesample.service

import android.accounts.Account
import retrofit2.Response
import retrofit2.http.GET

interface AccountsApi {
    @GET("api/v1/accounts")
    suspend fun apiV1AccountsGet(): Response<Account>
}