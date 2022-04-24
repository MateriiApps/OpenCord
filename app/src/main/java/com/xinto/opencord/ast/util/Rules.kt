package com.xinto.opencord.ast.util

import androidx.compose.ui.text.SpanStyle
import com.xinto.opencord.ast.node.StyledNode
import com.xinto.simpleast.*
import java.util.regex.Pattern

fun <RC, S> createSimpleTextRule(
    pattern: Pattern,
    styles: Collection<SpanStyle>
): Rule<RC, Node<RC>, S> =
    createRule(pattern) { matcher, _, state ->
        val node = StyledNode<RC>(styles)
        ParseSpec.createNonTerminal(
            node = node,
            state = state,
            startIndex = matcher.start(1),
            endIndex = matcher.end(1)
        )
    }