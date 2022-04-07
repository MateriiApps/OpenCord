package com.xinto.opencord.simpleast.impl.rule

import com.xinto.opencord.simpleast.core.node.Node
import com.xinto.opencord.simpleast.core.parser.ParseSpec
import com.xinto.opencord.simpleast.core.parser.Parser
import com.xinto.opencord.simpleast.core.rule.Rule
import com.xinto.opencord.simpleast.impl.node.TextNode
import com.xinto.opencord.simpleast.impl.util.PATTERN_OTHER
import java.util.regex.Matcher

fun <RC, S> createOtherRule() =
    object : Rule<RC, Node<RC>, S>(PATTERN_OTHER) {

        override fun parse(
            matcher: Matcher,
            parser: Parser<RC, in Node<RC>, S>,
            state: S,
        ) = ParseSpec.createTerminal(
            node = TextNode<RC>(matcher.group()),
            state = state,
        )

    }