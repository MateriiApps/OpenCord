package com.xinto.opencord.ast.node

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.xinto.opencord.ui.theme.Blurple
import com.xinto.simpleast.Node

class ChannelMentionNode<RC>(
    val channel: String?
) : Node<RC>() {

    context(AnnotatedString.Builder)
    override fun render(renderContext: RC) {
        withStyle(SpanStyle(
            color = Blurple,
            background = Blurple.copy(alpha = 0.2f),
            fontWeight = FontWeight.SemiBold
        )) {
            append("#$channel")
        }
    }

}