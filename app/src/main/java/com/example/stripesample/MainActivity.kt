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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.stripesample.ui.theme.StripeSampleTheme
import com.example.stripesample.viewmodel.MainVM

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ConsultationId = "59c48803-1779-4e36-c4cc-08daa1c92a61"
        // LoginInfo : Email = "aram.test002@gmail.com", Password = "PaymentTest1"

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
    val isSignIn = viewModel.signInChecked.observeAsState().value

    LaunchedEffect(isSignIn) {
        if (isSignIn == true) {

        }
    }

    Button(
        onClick = {

        },
        enabled = isSignIn == true
    ) {
        Text(text = "Pay")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StripeSampleTheme {
        PaymentScreen(MainVM())
    }
}