package com.xinto.opencord.ast.node

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import com.xinto.simpleast.Node
import com.xinto.opencord.ui.theme.DiscordBlurple

class ChannelMentionNode<RC>(
    val channel: String?
) : Node<RC>() {

    override fun render(
        builder: AnnotatedString.Builder,
        renderContext: RC
    ) {
        val startIndex = builder.length

        builder.append("#$channel")

        builder.addStyle(
            style = SpanStyle(
                color = DiscordBlurple,
                background = DiscordBlurple.copy(alpha = 0.2f),
                fontWeight = FontWeight.SemiBold
            ),
            start = startIndex,
            end = builder.length
        )
    }

}