package com.example.stripesample.viewmodel

import CloudHospitalApi.apis.ConsultationsApi
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stripesample.service.ApiClients
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.PaymentMethodCreateParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class PaymentVM(): ViewModel() {
    private val consultationsApi = ApiClients.apiClient.createService(ConsultationsApi::class.java)

    private val _paymentIntentClientSecret = MutableLiveData<String>()
    val paymentIntentClientSecret: LiveData<String> = _paymentIntentClientSecret

    val inProgress = MutableLiveData<Boolean>()
    val status = MutableLiveData<String>()
    val errorMsg = MutableLiveData<String>()

    fun startCheckoutConsultation(id: UUID) {
        val actionName = "apiV2ConsultationsConsultationIdPayPost"
        viewModelScope.launch(Dispatchers.Main) {
            val result = consultationsApi.apiV2ConsultationsConsultationIdPayPost(id)
            try {
                if (result.isSuccessful) {
                    Log.d("debug", "$actionName success")
                    if (result.code() == 200) {
                        result.body()?.let { data ->
                            _paymentIntentClientSecret.postValue(data)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("debug", "$actionName failure: ${e.message}")
            }
        }
    }

    fun createAndConfirmPaymentIntent(
        params: PaymentMethodCreateParams,
        paymentLauncher: PaymentLauncher
    ) {
        Log.d("debug", "params : $params")
        inProgress.postValue(true)
        _paymentIntentClientSecret.value?.let {
            Log.d("debug", "paymentIntentClientSecret : $it")
            val confirmPaymentIntentParams =
                ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
                    paymentMethodCreateParams = params,
                    clientSecret = it
                )
            paymentLauncher.confirm(confirmPaymentIntentParams)
        } ?: run {
            inProgress.postValue(false)
        }
    }
}