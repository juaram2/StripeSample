package com.example.stripesample.ui.composable

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stripesample.R
import com.example.stripesample.viewmodel.MainVM
import com.example.stripesample.viewmodel.PaymentVM
import com.stripe.android.model.PaymentMethodCreateParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult


@Composable
fun PaymentScreen(viewModel: MainVM) {
    val isLoading = viewModel.loading.observeAsState().value

    if (isLoading == true) {
        LoadingBar()
    } else {
        Payment()
    }
}

@Composable
fun Payment() {
    val context = LocalContext.current
    val paymentVM: PaymentVM = viewModel()
    val inProgress = paymentVM.inProgress.observeAsState(false).value
    val errorMsg = paymentVM.errorMsg.observeAsState().value
    val status = paymentVM.status.observeAsState().value

    val params = PaymentMethodCreateParams.create(
        PaymentMethodCreateParams.Card.Builder()
            .setNumber("4242424242424242")
            .setExpiryMonth(4)
            .setExpiryYear(2024)
            .setCvc("242")
            .build()
    )

    createPaymentLauncher(paymentVM).let { paymentLauncher ->
        Column(
            Modifier.padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    paymentVM.createAndConfirmPaymentIntent(params, paymentLauncher)
                },
                enabled = !inProgress
            ) {
                Text(text = "Pay")
            }

            Text(errorMsg ?: "", color = Color.Red, fontSize = 12.sp)
        }
    }

    if (inProgress) {
        LoadingBar()
    }

    if (status != null) {
        Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun createPaymentLauncher(viewModel: PaymentVM): PaymentLauncher {
    return PaymentLauncher.rememberLauncher(
        publishableKey = stringResource(id = R.string.publishable_key)
    ) {
        when (it) {
            is PaymentResult.Completed -> {
                viewModel.status.value = "PaymentIntent confirmation succeeded"
                viewModel.inProgress.value = false
            }
            is PaymentResult.Canceled -> {
                viewModel.status.value = "PaymentIntent confirmation cancelled"
                viewModel.inProgress.value = false
            }
            is PaymentResult.Failed -> {
                viewModel.errorMsg.value = it.throwable.localizedMessage
                viewModel.inProgress.value = false
            }
        }
    }
}