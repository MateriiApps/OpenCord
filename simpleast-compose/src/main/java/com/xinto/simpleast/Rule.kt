package com.xinto.simpleast

import java.util.regex.Matcher
import java.util.regex.Pattern

public abstract class Rule<RC, T : Node<RC>, S>(
    private val matcher: Matcher
) {

    public constructor(pattern: Pattern) : this(pattern.matcher(""))

    public open fun match(
        inspectionSource: CharSequence,
        lastCapture: String?,
        state: S,
    ): Matcher? {
        matcher.reset(inspectionSource)
        return if (matcher.find()) matcher else null
    }

    public abstract fun parse(
        matcher: Matcher,
        parser: Parser<RC, in T, S>, state: S,
    ): ParseSpec<RC, S>

    public abstract class BlockRule<R, T : Node<R>, S>(
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
