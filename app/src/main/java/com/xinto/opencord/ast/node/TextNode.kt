package com.xinto.opencord.ast.node

import androidx.compose.ui.text.AnnotatedString
import com.xinto.simpleast.Node

open class TextNode<RC>(
    val content: String,
) : Node<RC>() {
    //@formatter:off
    context(AnnotatedString.Builder)
    override fun render(renderContext: RC) { //@formatter:on
        append(content)
    }
}
