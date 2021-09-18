package com.xinto.opencord.ui.simpleast.impl.node

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import com.xinto.opencord.ui.simpleast.core.node.Node

class SpoilerNode<RC> : Node.Parent<RC>() {

    override fun render(
        builder: AnnotatedString.Builder,
        renderContext: RC,
    ) {
        val startIndex = builder.length

        super.render(builder, renderContext)

        //TODO Finish spoiler node
        builder.addStyle(
            style = SpanStyle(background = Color.Black),
            start = startIndex,
            end = builder.length
        )

    }

}