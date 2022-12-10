package com.xinto.opencord.ui.components.message.reply

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import com.xinto.opencord.ui.util.messageInlineContent

@Composable
fun MessageReferencedContent(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = text,
        inlineContent = messageInlineContent(),
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}
