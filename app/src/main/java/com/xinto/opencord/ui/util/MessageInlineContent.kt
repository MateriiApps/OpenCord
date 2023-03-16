package com.xinto.opencord.ui.util

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.sp
import com.xinto.opencord.BuildConfig
import com.xinto.opencord.ui.components.OCImage

@Composable
fun messageInlineContent(): Map<String, InlineTextContent> {
    val emoteSize = (LocalTextStyle.current.fontSize.value + 2f).sp
    return mapOf(
        "emote" to InlineTextContent(
            placeholder = Placeholder(
                width = emoteSize,
                height = emoteSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center,
            ),
        ) { emoteId ->
            OCImage(
                url = "${BuildConfig.URL_CDN}/emojis/$emoteId",
            )
        },
    )
}
