package com.xinto.opencord.ui.simpleast.impl.node

import androidx.compose.ui.text.AnnotatedString
import com.xinto.opencord.ui.simpleast.core.node.Node

open class TextNode<RC>(
    val content: String,
) : Node<RC>() {

    override fun render(
        builder: AnnotatedString.Builder,
        renderContext: RC,
    ) {
        builder.append(content)
    }

}