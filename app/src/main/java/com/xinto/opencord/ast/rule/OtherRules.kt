package com.xinto.opencord.ast.rule

import com.xinto.opencord.ast.node.TextNode
import com.xinto.opencord.ast.util.PATTERN_OTHER
import com.xinto.simpleast.*

fun <RC, S> createOtherRule(): Rule<RC, Node<RC>, S> =
    createRule(PATTERN_OTHER) { matcher, _, state ->
        ParseSpec.createTerminal(
            node = TextNode(matcher.group()),
            state = state,
        )
    }