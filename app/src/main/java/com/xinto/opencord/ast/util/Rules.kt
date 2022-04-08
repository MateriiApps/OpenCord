package com.xinto.opencord.ast.util

import androidx.compose.ui.text.SpanStyle
import com.xinto.simpleast.Node
import com.xinto.simpleast.ParseSpec
import com.xinto.simpleast.Parser
import com.xinto.simpleast.Rule
import com.xinto.opencord.ast.node.StyledNode
import java.util.regex.Matcher
import java.util.regex.Pattern

fun <RC, S> createSimpleTextRule(
    pattern: Pattern,
    styles: Collection<SpanStyle>,
) = object : Rule<RC, Node<RC>, S>(pattern) {

    override fun parse(
        matcher: Matcher,
        parser: Parser<RC, in Node<RC>, S>,
        state: S,
    ): ParseSpec<RC, S> {
        val node = StyledNode<RC>(styles)
        return ParseSpec.createNonTerminal(
            node = node,
            state = state,
            startIndex = matcher.start(1),
            endIndex = matcher.end(1)
        )
    }

}