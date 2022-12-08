package com.xinto.opencord.ast.node

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import com.xinto.simpleast.Node

class StyledNode<RC>(
    val styles: Collection<SpanStyle>,
) : Node.Parent<RC>() {
    //@formatter:off
    context(AnnotatedString.Builder)
    override fun render(renderContext: RC) { //@formatter:on
        var style = SpanStyle()

        styles.forEach {
            style += it
        }

        withStyle(style) {
            super.render(renderContext)
        }
    }
}
