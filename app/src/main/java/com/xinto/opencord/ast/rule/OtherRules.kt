package com.xinto.opencord.ast.rule

import com.xinto.opencord.ast.node.TextNode
import com.xinto.opencord.ast.util.PATTERN_OTHER
import com.xinto.simpleast.Node
import com.xinto.simpleast.ParseSpec
import com.xinto.simpleast.Parser
import com.xinto.simpleast.Rule
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