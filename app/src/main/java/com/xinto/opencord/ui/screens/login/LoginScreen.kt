package com.xinto.opencord.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.xinto.opencord.ui.component.text.OpenCordText
import com.xinto.opencord.ui.component.textfield.OpenCordTextField
import com.xinto.opencord.ui.widgets.button.PrimaryButton

@Composable
fun LoginScreen(
    navController: NavHostController
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
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
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    )
                )
            }
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { }
            ) {
                OpenCordText(text = "Login")
            }
        }
    }
}