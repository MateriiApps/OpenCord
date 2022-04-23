package com.xinto.opencord.ast.node

import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.text.AnnotatedString
import com.xinto.simpleast.Node

class EmoteNode<RC>(
    val emoteId: String
) : Node<RC>() {

    context(AnnotatedString.Builder)
    override fun render(renderContext: RC) {
        appendInlineContent("emote", emoteId)
    }

}