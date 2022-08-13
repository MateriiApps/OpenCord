package com.xinto.opencord.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.hcaptcha.sdk.HCaptcha
import com.xinto.opencord.R
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
            LoginAppBar(
                modifier = Modifier.fillMaxWidth(),
                onBackClick = onBackClick,
                isLoading = viewModel.isLoading,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = stringResource(R.string.login_login_title),
                    style = MaterialTheme.typography.displaySmall
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = stringResource(R.string.login_login_subtitle),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.username,
                    label = { Text(stringResource(R.string.login_field_username)) },
                    onValueChange = viewModel::updateUsername,
                    isError = viewModel.usernameError,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false
                    )
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.password,
                    label = { Text(stringResource(R.string.login_field_password)) },
                    onValueChange = viewModel::updatePassword,
                    isError = viewModel.passwordError,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    )
                )
            }
            Spacer(Modifier.weight(1f))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.login() },
                enabled = !viewModel.isLoading,
            ) {
                Text(stringResource(R.string.login_action_login))
            }
        }
    }

    if (viewModel.showCaptcha) {
        HCaptcha
            .getClient(context)
            .verifyWithHCaptcha(viewModel.captchaSiteKey)
            .addOnSuccessListener {
                viewModel.login(it.tokenResult)
            }
            .addOnFailureListener {
                //TODO
            }
    }

    if (viewModel.showMfa) {
        AlertDialog(
            title = { Text(stringResource(R.string.login_mfa_title)) },
            text = {
                OutlinedTextField(
                    modifier = Modifier.padding(8.dp),
                    value = viewModel.mfaCode,
                    onValueChange = viewModel::updateMfaCode,
                    label = { Text(stringResource(R.string.login_mfa_code)) },
                    singleLine = true,
                    isError = viewModel.mfaError,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false
                    )
                )
            },
            onDismissRequest = viewModel::dismissMfa,
            confirmButton = {
                Button(onClick = { viewModel.verifyTwoFactor(viewModel.mfaCode) }) {
                    Text(stringResource(R.string.login_mfa_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissMfa) {
                    Text(stringResource(R.string.login_mfa_cancel))
                }
            }
        )
    }
}

@Composable
private fun LoginAppBar(
    onBackClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SmallTopAppBar(
            title = { Text(stringResource(R.string.login_action_login)) },
            navigationIcon = {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.navigation_back)
                    )
                }
            },
        )
        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
