package com.xinto.opencord.ui.simpleast.impl.node

import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.text.AnnotatedString
import com.xinto.opencord.ui.simpleast.core.node.Node

class EmoteNode<RC>(
    val emoteId: String
) : Node<RC>() {

    override fun render(
        builder: AnnotatedString.Builder,
        renderContext: RC
    ) {
        builder.appendInlineContent("emote", emoteId)
    }

}