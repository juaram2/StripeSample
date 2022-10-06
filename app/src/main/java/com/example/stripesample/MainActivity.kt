package com.example.stripesample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.stripesample.ui.composable.PaymentScreen
import com.example.stripesample.ui.theme.StripeSampleTheme
import com.example.stripesample.utils.PrefUtil
import com.example.stripesample.viewmodel.MainVM
import com.example.stripesample.viewmodel.PaymentVM
import com.stripe.android.PaymentConfiguration
import java.util.*

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainVM>()
    private val paymentVM by viewModels<PaymentVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val consultationId = "f75d0941-a128-4c25-08ad-08daa6ad944c"

        PrefUtil.init(this)
        PaymentConfiguration.init(
            applicationContext,
            getString(R.string.publishable_key)
        )

        viewModel.onClickSignin()
        viewModel.signedIn.observe(this) {
            if (it == true) {
                viewModel.checkIdentityToken()
            }
        }

        viewModel.profile.observe(this) {
            if (it != null) {
                paymentVM.startCheckoutConsultation(UUID.fromString(consultationId))
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StripeSampleTheme {
        PaymentScreen(MainVM())
    }
}