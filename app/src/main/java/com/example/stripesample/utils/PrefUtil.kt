package com.example.stripesample.utils

import android.content.Context
import com.example.stripesample.BuildConfig
import com.example.stripesample.model.IdentityToken
import com.pddstudio.preferences.encrypted.EncryptedPreferences
import java.time.LocalDateTime


object PrefUtil {

    private var ACCESS_TOKEN = "ACCESS_TOKEN-${BuildConfig.FLAVOR}"
    var REFRESH_TOKEN = "REFRESH_TOKEN-${BuildConfig.FLAVOR}"
    var TOKEN_TYPE = "TOKEN_TYPE-${BuildConfig.FLAVOR}"
    var EXPIRES_AT = "EXPIRES_AT-${BuildConfig.FLAVOR}"

    private lateinit var prefs: EncryptedPreferences

    fun init(context: Context) {
        prefs = EncryptedPreferences.Builder(context).withEncryptionPassword("cloudhospital.${BuildConfig.FLAVOR}").build()
    }

    fun cacheIdentityToken(identityToken: IdentityToken) {
        access_token = identityToken.access_token
        refresh_token = identityToken.refresh_token
        token_type = identityToken.token_type
        expires_at = LocalDateTime.now().plusSeconds(identityToken.expires_in.toLong()).toString()
    }

    fun getCachedIdentityToken(): IdentityToken? {
        val identityToken = IdentityToken(
            access_token = access_token,
            token_type = token_type,
            refresh_token =  refresh_token
        )

        return if (identityToken.access_token != "") {
            identityToken
        } else {
            null
        }
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