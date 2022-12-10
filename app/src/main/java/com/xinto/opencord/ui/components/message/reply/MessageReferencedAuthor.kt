package com.xinto.opencord.ui.components.message.reply

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun MessageReferencedAuthor(
    author: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = author,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}
