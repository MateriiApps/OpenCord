package com.xinto.opencord.ui.simpleast.render

import androidx.compose.ui.text.AnnotatedString
import com.xinto.opencord.ui.simpleast.core.node.Node
import com.xinto.opencord.ui.simpleast.core.parser.Parser

fun <RC, S> Parser<RC, Node<RC>, S>.render(
    source: String,
    initialState: S,
    renderContext: RC,
) = render(
    builder = AnnotatedString.Builder(),
    nodes = parse(source, initialState),
    renderContext = renderContext,
)

fun <RC> render(
    builder: AnnotatedString.Builder,
    nodes: Collection<Node<RC>>,
    renderContext: RC,
): AnnotatedString.Builder {
    for (node in nodes) {
        node.render(builder, renderContext)
    }
    return builder
}