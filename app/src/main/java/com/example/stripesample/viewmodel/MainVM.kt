package com.example.stripesample.viewmodel

import CloudHospitalApi.apis.ProfilesApi
import CloudHospitalApi.models.UserModel
import android.accounts.Account
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stripesample.model.ExternalLogins
import com.example.stripesample.model.IdentityToken
import com.example.stripesample.service.AccountsApi
import com.example.stripesample.service.ApiClients
import com.example.stripesample.service.AuthsApi
import com.example.stripesample.service.ExternalLoginsApi
import com.example.stripesample.utils.PrefUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainVM(): ViewModel() {
    private val authsApi = ApiClients.identityApiClient.createService(AuthsApi::class.java)
    private val accountApi = ApiClients.identityApiClient.createService(AccountsApi::class.java)
    private val profilesApi = ApiClients.apiClient.createService(ProfilesApi::class.java)
    private val accountsApi = ApiClients.identityApiClient.createService(AccountsApi::class.java)
    private val externalLoginsApi = ApiClients.identityApiClient.createService(ExternalLoginsApi::class.java)

    var email = MutableLiveData<String?>()
    val password = MutableLiveData<String?>()
    val confirmPassword = MutableLiveData<String?>()
    val code = MutableLiveData<String?>()

    private val _startGoogleSigninActivity = MutableLiveData<Boolean>()
    val startGoogleSigninActivity: LiveData<Boolean>
        get() = _startGoogleSigninActivity

    private var _signedIn = MutableLiveData<Boolean>(false)
    val signedIn: LiveData<Boolean>
        get() = _signedIn

    private val _externalLoginInfos = MutableLiveData<ExternalLogins?>()
    val externalLoginInfos: LiveData<ExternalLogins?> = _externalLoginInfos

    private val handler = Handler()

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _account = MutableLiveData<Account?>(null)
    val account: LiveData<Account?> = _account

    private val _profile = MutableLiveData<UserModel?>(null)
    val profile: LiveData<UserModel?> = _profile

    private val _signInChecked = MutableLiveData<Boolean?>()
    val signInChecked: LiveData<Boolean?> = _signInChecked

    fun checkIdentityToken() {
        val identityToken = PrefUtil.getCachedIdentityToken()

        Log.d("debug", "update identityToken: $identityToken")

        if (identityToken != null) {
            getUserAccount()
            Log.d("debug", "has identityToken")
        } else {
            signInChecked()
        }
    }

    private fun getUserAccount() {
        _loading.value = true
        val actionName = "apiV1AccountsGet"
        Log.d("debug", "$actionName started")
        viewModelScope.launch(Dispatchers.Main) {
            val response = accountApi.apiV1AccountsGet()
            try {
                _loading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("debug", "$actionName: ${response.body()}")
                        _account.postValue(it)
                        getUserProfile()
                    }
                } else {
                    _account.postValue(null)
                    _profile.postValue(null)
                    PrefUtil.clearIdentityToken()
                }
            }
            catch (e: java.lang.Exception) {
                _loading.value = false
                _account.postValue(null)
                _profile.postValue(null)
                PrefUtil.clearIdentityToken()
            }

        }
    }

    private fun getUserProfile() {
        _loading.value = true
        val actionName = "apiV2ProfilesGet"
        Log.d("debug", "$actionName started")
        viewModelScope.launch(Dispatchers.Main) {
            val response = profilesApi.apiV2ProfilesGet()
            try {
                if (response.isSuccessful) {
                    _loading.value = false
                    response.body()?.let { data ->
                        Log.d("debug", "$actionName: ${response.body()}")
                        _profile.postValue(data)
                        signInChecked()
                    }
                }
            }
            catch (e: Exception) {
                Log.e("error", "$actionName: ${e.localizedMessage}")
                _profile.postValue(null)
                _loading.value = false
            }
        }
    }

    private fun signInChecked() {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            _signInChecked.postValue(true)
        }, 500)
    }

    fun onClickSignin() {
        val actionName = "signInWithEmail"
        Log.d("debug","$actionName started")
        Log.d("debug", "${email.value.toString()} ${password.value.toString()}")
        _loading.value = true

        viewModelScope.launch(Dispatchers.Main) {
            val response = authsApi.signinWithEmail(
                "com.cloudhospital.int",
                "CloudHospitalSecret",
                "openid email profile roles CloudHospital_api IdentityServerApi offline_access",
                "password",
                email.value.toString(),
                password.value.toString()
            )
            Log.e("Debug", "response : $response")
            try {
                _loading.value = false
                Log.e("Debug", "responseTry : $response")
                if (response.isSuccessful) {
                    response.body()?.let {
                        PrefUtil.cacheIdentityToken(
                            IdentityToken(
                                it.access_token,
                                it.expires_in,
                                it.token_type,
                                it.refresh_token,
                                it.scope
                            )
                        )
                        Log.e("Debug", "IdentityToken : "+response.body().toString())
                        Log.d("debug", "$actionName: ${response.body()}")
                        _signedIn.postValue(true)
                    }
                }
            } catch (e: Exception) {
                _loading.value = false
                Log.e("error", "$actionName: ${e.localizedMessage}")
            }
        }
    }
}