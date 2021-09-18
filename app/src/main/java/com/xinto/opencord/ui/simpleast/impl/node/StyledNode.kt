package com.xinto.opencord.ui.simpleast.impl.node

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import com.xinto.opencord.ui.simpleast.core.node.Node

class StyledNode<RC>(
    val styles: Collection<SpanStyle>,
) : Node.Parent<RC>() {

    override fun render(
        builder: AnnotatedString.Builder,
        renderContext: RC,
    ) {
        val startIndex = builder.length

        super.render(builder, renderContext)

        styles.forEach { newSpan ->
            builder.addStyle(
                style = newSpan,
                start = startIndex,
                end = builder.length
            )
        }
    }

}