package com.xinto.opencord.simpleast.impl.util

import androidx.compose.ui.text.SpanStyle
import com.xinto.opencord.simpleast.core.node.Node
import com.xinto.opencord.simpleast.core.parser.ParseSpec
import com.xinto.opencord.simpleast.core.parser.Parser
import com.xinto.opencord.simpleast.core.rule.Rule
import com.xinto.opencord.simpleast.impl.node.StyledNode
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
        return ParseSpec.createNonterminal(
            node = node,
            state = state,
            startIndex = matcher.start(1),
            endIndex = matcher.end(1)
        )
    }

}