package com.xinto.opencord.ui.component.text

import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import java.util.*

@Composable
fun OpenCordListCategory(
    text: String
) {
    CompositionLocalProvider(
        LocalContentAlpha provides ContentAlpha.medium
    ) {
        OpenCordText(
            text = text.uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.h6
        )
    }
}