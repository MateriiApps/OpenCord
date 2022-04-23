package com.xinto.simpleast

import androidx.compose.ui.text.AnnotatedString

public fun <RC, S> Parser<RC, Node<RC>, S>.render(
    source: String,
    initialState: S,
    renderContext: RC,
): AnnotatedString.Builder {
    return render(
        builder = AnnotatedString.Builder(),
        nodes = parse(source, initialState),
        renderContext = renderContext,
    )
}

public fun <RC> render(
    builder: AnnotatedString.Builder = AnnotatedString.Builder(),
    nodes: Collection<Node<RC>>,
    renderContext: RC,
): AnnotatedString.Builder {
    return builder.apply {
        for (node in nodes) {
            node.render(renderContext)
        }
    }
}