package com.xinto.opencord.ui.simpleast.impl.rule

import com.xinto.opencord.ui.simpleast.core.node.Node
import com.xinto.opencord.ui.simpleast.core.parser.ParseSpec
import com.xinto.opencord.ui.simpleast.core.parser.Parser
import com.xinto.opencord.ui.simpleast.core.rule.Rule
import com.xinto.opencord.ui.simpleast.impl.node.SpoilerNode
import com.xinto.opencord.ui.simpleast.impl.node.TextNode
import com.xinto.opencord.ui.simpleast.impl.util.PATTERN_ESCAPE
import com.xinto.opencord.ui.simpleast.impl.util.PATTERN_SPOILER
import java.util.regex.Matcher

fun <RC, S> createSpoilerRule() =
    object : Rule<RC, Node<RC>, S>(PATTERN_SPOILER) {

        override fun parse(
            matcher: Matcher,
            parser: Parser<RC, Node<RC>, S>,
            state: S,
        ): ParseSpec<RC, S> {
            val node = SpoilerNode<RC>()
            return ParseSpec.createNonterminal(
                node = node,
                state = state,
                startIndex = matcher.start(1),
                endIndex = matcher.end(1)
            )
        }

    }

fun <RC, S> createEscapeRule() =
    object : Rule<RC, Node<RC>, S>(PATTERN_ESCAPE) {

        override fun parse(
            matcher: Matcher,
            parser: Parser<RC, in Node<RC>, S>,
            state: S,
        ) = ParseSpec.createTerminal(
            node = TextNode<RC>(matcher.group(1)!!),
            state = state
        )

    }