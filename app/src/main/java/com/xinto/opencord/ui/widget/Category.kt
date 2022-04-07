package com.xinto.opencord.ui.widget

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import java.util.*

@Composable
fun WidgetCategory(
    title: String,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(
        LocalContentAlpha provides ContentAlpha.medium,
        LocalTextStyle provides MaterialTheme.typography.h6
    ) {
        Text(
            modifier = modifier,
            text = title.uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.h6
        )
    }
}