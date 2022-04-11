package com.xinto.opencord.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xinto.opencord.R

@Composable
fun LoginLandingScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.login_landing_title),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displaySmall
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.login_landing_subtitle),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onLoginClick,
                ) {
                    Text(stringResource(R.string.login_action_login))
                }
                FilledTonalButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onRegisterClick,
                ) {
                    Text(stringResource(R.string.login_action_login))
                }
            }
        }
    }
}