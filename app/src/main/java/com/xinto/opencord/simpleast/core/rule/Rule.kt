package com.xinto.opencord.simpleast.core.rule

import com.xinto.opencord.simpleast.core.node.Node
import com.xinto.opencord.simpleast.core.parser.ParseSpec
import com.xinto.opencord.simpleast.core.parser.Parser
import java.util.regex.Matcher
import java.util.regex.Pattern

abstract class Rule<RC, T : Node<RC>, S>(val matcher: Matcher) {

    constructor(pattern: Pattern) : this(pattern.matcher(""))

    open fun match(
        inspectionSource: CharSequence,
        lastCapture: String?,
        state: S,
    ): Matcher? {
        matcher.reset(inspectionSource)
        return if (matcher.find()) matcher else null
    }

    abstract fun parse(
        matcher: Matcher,
        parser: Parser<RC, in T, S>, state: S,
    ): ParseSpec<RC, S>

    abstract class BlockRule<R, T : Node<R>, S>(
        pattern: Pattern,
    ) : Rule<R, T, S>(pattern) {

        override fun match(
            inspectionSource: CharSequence,
            lastCapture: String?,
            state: S,
        ): Matcher? {
            if (lastCapture?.endsWith('\n') != false) {
                return super.match(inspectionSource, lastCapture, state)
            }
            return null
        }
    }
}
