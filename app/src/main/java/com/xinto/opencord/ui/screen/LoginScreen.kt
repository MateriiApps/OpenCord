package com.xinto.opencord.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hcaptcha.sdk.HCaptcha
import com.xinto.opencord.ui.component.OCButton
import com.xinto.opencord.ui.component.OCTextField
import com.xinto.opencord.ui.viewmodel.LoginViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    viewModel: LoginViewModel = getViewModel()
) {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(
                            text = "Login",
                            style = MaterialTheme.typography.h2
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = "Return back"
                            )
                        }
                    },
                    backgroundColor = MaterialTheme.colors.background,
                    elevation = 0.dp
                )
                if (viewModel.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Welcome back!",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h1
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "We're so excited to see you again!",
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OCTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.username,
                    label = "Email or Phone Number",
                    onValueChange = viewModel::updateUsername,
                    isError = viewModel.usernameError,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false
                    )
                )
                OCTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.password,
                    label = "Password",
                    onValueChange = viewModel::updatePassword,
                    isError = viewModel.passwordError,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    )
                )
            }
            OCButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.login() },
                enabled = !viewModel.isLoading,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary
                )
            ) {
                Text(text = "Login")
            }
        }
    }

    if (viewModel.showCaptcha) {
        HCaptcha
            .getClient(context)
            .verifyWithHCaptcha(viewModel.captchaSiteKey!!)
            .addOnSuccessListener {
                viewModel.login(it.tokenResult)
            }
            .addOnFailureListener {
                //TODO
            }
    }
}