package com.xinto.simpleast

import java.util.*

public inline fun <RC, T : Node<RC>, S> createParser(
    rules: Parser<RC, T, S>.RuleScope.() -> Unit,
): Parser<RC, T, S> {
    return Parser<RC, T, S>().apply {
        RuleScope().apply(rules)
    }
}

public class Parser<RC, T : Node<RC>, S> {
    private val rules = ArrayList<Rule<RC, out T, S>>()

    public fun parse(
        source: CharSequence,
        initialState: S,
        rules: List<Rule<RC, out T, S>> = this.rules,
    ): MutableList<T> {
        val remainingParses = Stack<ParseSpec<RC, S>>()
        val topLevelRootNode = Node<RC>()

        var lastCapture: String? = null

        if (source.isNotEmpty()) {
            remainingParses.add(
                ParseSpec.createNonTerminal(
                    node = topLevelRootNode,
                    state = initialState,
                    startIndex = 0,
                    endIndex = source.length,
                ),
            )
        }

        while (!remainingParses.isEmpty()) {
            val builder = remainingParses.pop()

            if (builder.startIndex >= builder.endIndex) {
                break
            }

            val inspectionSource = source.subSequence(builder.startIndex, builder.endIndex)
            val offset = builder.startIndex

            val (rule, matcher) = rules
                .firstNotNullOfOrNull { rule ->
                    rule.match(inspectionSource, lastCapture, builder.state)?.let { matcher ->
                        rule to matcher
                    }
                } ?: throw ParseException("failed to find rule to match source", source)

            val matcherSourceEnd = matcher.end() + offset
            val newBuilder = rule.parse(matcher, this, builder.state)

            val parent = builder.root
            parent.addChild(newBuilder.root)

            if (matcherSourceEnd != builder.endIndex) {
                remainingParses.push(
                    ParseSpec.createNonTerminal(
                        node = parent,
                        state = builder.state,
                        startIndex = matcherSourceEnd,
                        endIndex = builder.endIndex,
                    ),
                )
            }

            if (!newBuilder.isTerminal) {
                newBuilder.applyOffset(offset)
                remainingParses.push(newBuilder)
            }

            try {
                lastCapture = matcher.group(0)
            } catch (throwable: Throwable) {
                throw ParseException(
                    message = "matcher found no matches",
                    source = source,
                    cause = throwable,
                )
            }
        }

        @Suppress("UNCHECKED_CAST")
        val children = topLevelRootNode.nodeChildren?.toMutableList() as? MutableList<T>
        return children ?: ArrayList()
    }

    public inner class RuleScope {
        public fun rule(newRule: Rule<RC, out T, S>) {
            rules.add(newRule)
        }

        public fun rules(vararg newRules: Rule<RC, out T, S>) {
            rules(newRules.toList())
        }

        public fun rules(newRules: List<Rule<RC, out T, S>>) {
            rules.addAll(newRules)
        }
    }
}
