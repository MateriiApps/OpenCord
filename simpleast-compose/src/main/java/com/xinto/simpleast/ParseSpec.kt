package com.xinto.simpleast

public class ParseSpec<RC, S> private constructor(
    public val root: Node<RC>,
    public val isTerminal: Boolean,
    public val state: S,
    public var startIndex: Int,
    public var endIndex: Int,
) {

    public fun applyOffset(offset: Int) {
        startIndex += offset
        endIndex += offset
    }

    public companion object {

        public fun <RC, S> createNonTerminal(
            node: Node<RC>,
            state: S,
            startIndex: Int,
            endIndex: Int,
        ): ParseSpec<RC, S> {
            return ParseSpec(
                root = node,
                isTerminal = false,
                state = state,
                startIndex = startIndex,
                endIndex = endIndex
            )
        }

        public fun <RC, S> createTerminal(
            node: Node<RC>,
            state: S,
        ): ParseSpec<RC, S> {
            return ParseSpec(
                root = node,
                isTerminal = true,
                state = state,
                startIndex = 0,
                endIndex = 0
            )
        }
    }
}