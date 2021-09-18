package com.xinto.opencord.ui.simpleast.core.parser

import com.xinto.opencord.ui.simpleast.core.node.Node

class ParseSpec<RC, S> private constructor(
    val root: Node<RC>,
    val isTerminal: Boolean,
    val state: S,
    var startIndex: Int = 0,
    var endIndex: Int = 0,
) {

    fun applyOffset(offset: Int) {
        startIndex += offset
        endIndex += offset
    }

    companion object {

        fun <RC, S> createNonterminal(
            node: Node<RC>,
            state: S,
            startIndex: Int,
            endIndex: Int,
        ) = ParseSpec(
            root = node,
            isTerminal = false,
            state = state,
            startIndex = startIndex,
            endIndex = endIndex
        )

        fun <RC, S> createTerminal(
            node: Node<RC>,
            state: S,
        ) = ParseSpec(
            root = node,
            isTerminal = true,
            state = state
        )
    }
}