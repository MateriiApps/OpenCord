package com.xinto.opencord.ui.screens.login

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavHostController
import com.google.gson.GsonBuilder
import com.hcaptcha.sdk.HCaptcha
import com.xinto.opencord.domain.model.DomainLoginResult
import com.xinto.opencord.ext.authPreferences
import com.xinto.opencord.network.body.LoginBody
import com.xinto.opencord.network.repository.DiscordAuthAPIRepository
import com.xinto.opencord.network.response.CaptchaResponse
import com.xinto.opencord.network.result.DiscordAPIResult
import com.xinto.opencord.ui.MainActivity
import com.xinto.opencord.ui.component.text.OpenCordText
import com.xinto.opencord.ui.component.textfield.OpenCordTextField
import com.xinto.opencord.ui.widgets.button.PrimaryButton
import com.xinto.opencord.util.currentAccountToken
import kotlinx.coroutines.*
import org.koin.androidx.compose.get

@Composable
fun LoginScreen(
    navController: NavHostController,
) {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val repository = get<DiscordAuthAPIRepository>()

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var captchaToken by remember { mutableStateOf<String?>(null) }

    var loginError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    var showLoadingBar by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        OpenCordText(
                            text = "Login",
                            style = MaterialTheme.typography.h2
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.popBackStack() }
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
                if (showLoadingBar) {
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
                OpenCordText(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Welcome back!",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h1
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    OpenCordText(
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
                OpenCordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = login,
                    label = "Email or Phone Number",
                    onValueChange = {
                        login = it
                    },
                    isError = loginError,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false
                    )
                )
                OpenCordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    label = "Password",
                    onValueChange = {
                        password = it
                    },
                    isError = passwordError,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    )
                )
            }
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = !showLoadingBar,
                onClick = {
                    if (login.isEmpty()) {
                        loginError = true
                        return@PrimaryButton
                    }

                    if (password.isEmpty()) {
                        passwordError = true
                        return@PrimaryButton
                    }

                    coroutineScope.launch(Dispatchers.IO) {
                        showLoadingBar = true
                        when (val response = repository.login(
                            LoginBody(
                                login,
                                password,
                                captchaKey = captchaToken
                            )
                        )) {
                            is DiscordAPIResult.Success<DomainLoginResult> -> {
                                val token = response.data.token

                                currentAccountToken = token
                                context.authPreferences.edit {
                                    putString("user_token", token)
                                }

                                withContext(Dispatchers.Main) {
                                    context.startActivity(Intent(context, MainActivity::class.java))
                                }
                            }
                            is DiscordAPIResult.Error -> {
                                val error = response.e.response()?.errorBody()?.string()
                                val gson = GsonBuilder().create()

                                val captchaResponse =
                                    gson.fromJson(error, CaptchaResponse::class.java)
                                val captchaKey = captchaResponse.captcha_sitekey

                                if (captchaKey != null) {
                                    withContext(Dispatchers.Main) {
                                        HCaptcha
                                            .getClient(context)
                                            .verifyWithHCaptcha(captchaKey)
                                            .addOnSuccessListener {
                                                captchaToken = it.tokenResult
                                            }
                                            .addOnFailureListener {
                                                //TODO
                                            }
                                    }
                                }
                            }
                        }
                        showLoadingBar = false
                    }
                }
            ) {
                OpenCordText(text = "Login")
            }
        }
    }
}