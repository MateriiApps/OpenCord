package com.xinto.opencord.simpleast.core.parser

import com.xinto.opencord.simpleast.core.node.Node

class ParseSpec<RC, S> private constructor(
    val root: Node<RC>,
    val isTerminal: Boolean,
    val state: S,
    var startIndex: Int,
    var endIndex: Int,
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
            state = state,
            startIndex = 0,
            endIndex = 0
        )
    }
}