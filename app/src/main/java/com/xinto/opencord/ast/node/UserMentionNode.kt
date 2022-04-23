package com.xinto.opencord.ast.node

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.xinto.opencord.ui.theme.DiscordBlurple
import com.xinto.simpleast.Node

class UserMentionNode<RC>(
    private val userId: String?
) : Node<RC>() {

    context(AnnotatedString.Builder)
    override fun render(renderContext: RC) {
        withStyle(SpanStyle(
            color = DiscordBlurple,
            background = DiscordBlurple.copy(alpha = 0.2f),
            fontWeight = FontWeight.SemiBold
        )) {
            append("@$userId")
        }
    }

}