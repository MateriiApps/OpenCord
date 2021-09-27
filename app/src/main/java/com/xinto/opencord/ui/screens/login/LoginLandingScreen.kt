package com.xinto.opencord.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.xinto.opencord.R
import com.xinto.opencord.ui.component.text.Text
import com.xinto.opencord.ui.widgets.button.PrimaryButton
import com.xinto.opencord.ui.widgets.button.SecondaryButton

@Composable
fun LoginLandingScreen(
    navController: NavHostController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.ic_discord_logo_text),
                contentDescription = "Discord logo",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
            )
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.img_login_splash),
                contentDescription = "Splash image"
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Welcome to OpenCord",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h1
            )
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Join over 0 people who use OpenCord as an alternative to the stock Discord app.",
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    style = MaterialTheme.typography.body2
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate("login") }
            ) {
                Text(text = "Login")
            }
            SecondaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { }
            ) {
                Text(text = "Register")
            }
        }
    }
}