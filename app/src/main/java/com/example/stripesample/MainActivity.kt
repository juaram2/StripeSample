package com.example.stripesample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.stripesample.ui.theme.StripeSampleTheme
import com.example.stripesample.viewmodel.MainVM
import com.example.stripesample.viewmodel.PaymentVM
import com.stripe.android.PaymentConfiguration
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import java.util.*

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // LoginInfo : Email = "aram.test002@gmail.com", Password = "PaymentTest1"

        PaymentConfiguration.init(
            applicationContext,
            ""
        )

        viewModel.onClickSignin()
        viewModel.signedIn.observe(this) {
            if (it == true) {
                viewModel.checkIdentityToken()
            }
        }

        setContent {
            StripeSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    PaymentScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun PaymentScreen(viewModel: MainVM) {
    val context = LocalContext.current
    val paymentVM: PaymentVM = viewModel()
    val signInChecked = viewModel.signInChecked.observeAsState().value

    LaunchedEffect(signInChecked) {

        if (signInChecked == true) {
            paymentVM.startCheckoutConsultation(UUID.fromString("59c48803-1779-4e36-c4cc-08daa1c92a61"))
        }
    }

    Button(
        onClick = {

        },
        enabled = signInChecked == true
    ) {
        Text(text = "Pay")
    }
}

@Composable
fun createPaymentLauncher(): PaymentLauncher {
    val context = LocalContext.current
    val viewModel: PaymentVM = viewModel()

    return PaymentLauncher.rememberLauncher(
        publishableKey = "publishableKey"
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StripeSampleTheme {
        PaymentScreen(MainVM())
    }
}