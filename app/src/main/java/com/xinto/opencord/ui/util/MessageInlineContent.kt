package com.xinto.opencord.ui.util

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.sp
import com.xinto.opencord.BuildConfig
import com.xinto.opencord.ui.components.OCImage
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentHashMapOf

@Composable
fun messageInlineContent(): ImmutableMap<String, InlineTextContent> {
    val emoteSize = (LocalTextStyle.current.fontSize.value + 2f).sp

    return remember(emoteSize) {
        persistentHashMapOf(
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
}
