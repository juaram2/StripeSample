package com.example.stripesample.utils

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.example.stripesample.model.IdentityToken
import com.pddstudio.preferences.encrypted.EncryptedPreferences
import java.time.LocalDateTime
import java.time.chrono.ChronoLocalDateTime


object PrefUtil {

    var ACCESS_TOKEN = "ACCESS_TOKEN-dev"
    var REFRESH_TOKEN = "REFRESH_TOKEN-dev"
    var TOKEN_TYPE = "TOKEN_TYPE-dev"
    var EXPIRES_AT = "EXPIRES_AT-dev"

    private lateinit var prefs: EncryptedPreferences

    fun init(context: Context) {
        prefs = EncryptedPreferences.Builder(context).withEncryptionPassword("cloudhospital.dev").build()
    }

    fun cacheIdentityToken(identityToken: IdentityToken) {
        access_token = identityToken.access_token
        refresh_token = identityToken.refresh_token
        token_type = identityToken.token_type
        expires_at = LocalDateTime.now().plusSeconds(identityToken.expires_in.toLong()).toString()
    }

    fun getCachedIdentityToken(): IdentityToken? {
        var identityToken = IdentityToken(
            access_token = access_token,
            token_type = token_type,
            refresh_token =  refresh_token
        )

        if (identityToken.access_token != "") {
            return identityToken
        } else {
            return null
        }
    }

    fun getCachedAccessToken(): String {
        return access_token
    }

    fun checkIfTokenExpired(): Boolean {
        Log.d("debug","expires_at :: ${expires_at}")
        return if(TextUtils.isEmpty(expires_at)) true
        else LocalDateTime.parse(expires_at).isBefore(ChronoLocalDateTime.from((LocalDateTime.now())))
        //return LocalDate.parse(expires_at).isAfter(ChronoLocalDate.from(now()))
    }

    fun clearIdentityToken() {
        access_token = ""
        refresh_token = ""
        token_type = ""
        expires_at = ""
    }

    private var access_token: String
        get() = prefs.getString(ACCESS_TOKEN, "")
        set(value) = prefs.edit().putString(ACCESS_TOKEN, value).apply()

    private var refresh_token: String
        get() = prefs.getString(REFRESH_TOKEN, "")
        set(value) = prefs.edit().putString(REFRESH_TOKEN, value).apply()

    private var token_type: String
        get() = prefs.getString(TOKEN_TYPE, "")
        set(value) = prefs.edit().putString(TOKEN_TYPE, value).apply()

    private var expires_at: String
        get() = prefs.getString(EXPIRES_AT, "")
        set(value) = prefs.edit().putString(EXPIRES_AT, value).apply()
}