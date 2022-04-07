package com.xinto.opencord.simpleast.impl.node

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import com.xinto.opencord.simpleast.core.node.Node
import com.xinto.opencord.ui.theme.DiscordBlurple

class UserMentionNode<RC>(
    val userId: String?
) : Node<RC>() {

    override fun render(
        builder: AnnotatedString.Builder,
        renderContext: RC
    ) {
        val startIndex = builder.length

        builder.append("@$userId")

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