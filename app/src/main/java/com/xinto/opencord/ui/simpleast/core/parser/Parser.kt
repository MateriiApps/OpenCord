package com.xinto.opencord.ui.simpleast.core.parser

import com.xinto.opencord.ui.simpleast.core.exception.ParseException
import com.xinto.opencord.ui.simpleast.core.node.Node
import com.xinto.opencord.ui.simpleast.core.rule.Rule
import java.util.*
import kotlin.collections.ArrayList

class Parser<RC, T : Node<RC>, S> {

    private val rules = ArrayList<Rule<RC, out T, S>>()

    fun rules(block: RuleScope.() -> Unit) {
        RuleScope().apply(block)
    }

    fun parse(
        source: CharSequence,
        initialState: S,
        rules: List<Rule<RC, out T, S>> = this.rules,
    ): MutableList<T> {
        val remainingParses = Stack<ParseSpec<RC, S>>()
        val topLevelRootNode = Node<RC>()

        var lastCapture: String? = null

        if (source.isNotEmpty()) {
            remainingParses.add(
                ParseSpec.createNonterminal(
                    node = topLevelRootNode,
                    state = initialState,
                    startIndex = 0,
                    endIndex = source.length
                )
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
                    val matcher = rule.match(inspectionSource, lastCapture, builder.state)
                    if (matcher == null) {
                        null
                    } else {
                        rule to matcher
                    }
                }
                ?: throw ParseException("failed to find rule to match source", source)

            val matcherSourceEnd = matcher.end() + offset
            val newBuilder = rule.parse(matcher, this, builder.state)

            val parent = builder.root
            parent.children {
                child(newBuilder.root)
            }

            if (matcherSourceEnd != builder.endIndex) {
                remainingParses.push(
                    ParseSpec.createNonterminal(
                        node = parent,
                        state = builder.state,
                        startIndex = matcherSourceEnd,
                        endIndex = builder.endIndex
                    )
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
                    cause = throwable
                )
            }
        }

        @Suppress("UNCHECKED_CAST")
        return topLevelRootNode.nodeChildren.toMutableList() as MutableList<T>
    }

    inner class RuleScope {

        fun rule(newRule: Rule<RC, out T, S>) =
            rules.add(newRule)

        fun rules(vararg newRules: Rule<RC, out T, S>) =
            rules(newRules.toList())

        fun rules(newRules: List<Rule<RC, out T, S>>) =
            rules.addAll(newRules)

    }

}