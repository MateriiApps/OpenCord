package com.xinto.opencord.ui.component.appbar

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    navigation: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface,
        contentPadding = PaddingValues(horizontal = 4.dp),
        elevation = 0.dp,
    ) {
        CompositionLocalProvider(
            LocalContentAlpha provides ContentAlpha.high,
            LocalTextStyle provides TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        ) {
            if (navigation != null) {
                navigation()
                Spacer(modifier = Modifier.width(4.dp))
            }
            Box(modifier = Modifier.weight(1f)) {
                title()
            }
            actions()
        }
    }
}