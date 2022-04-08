package com.xinto.opencord.ast.node

import androidx.compose.ui.text.AnnotatedString
import com.xinto.simpleast.Node

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