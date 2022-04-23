package com.xinto.simpleast

import java.util.regex.Matcher
import java.util.regex.Pattern

public inline fun <RC, reified T : Node<RC>, S> createRule(
    pattern: Pattern,
    crossinline block: (Matcher, Parser<RC, in T, S>, state: S) -> ParseSpec<RC, S>
): Rule<RC, T, S> {
    return object : Rule<RC, T, S>(pattern) {
        override fun parse(
            matcher: Matcher,
            parser: Parser<RC, in T, S>,
            state: S
        ): ParseSpec<RC, S> {
            return block(matcher, parser, state)
        }
    }
}