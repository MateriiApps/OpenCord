package com.xinto.opencord.ui.components.message

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import com.xinto.opencord.ui.util.messageInlineContent

@Composable
fun MessageContent(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = text,
        inlineContent = messageInlineContent(),
    )
}
