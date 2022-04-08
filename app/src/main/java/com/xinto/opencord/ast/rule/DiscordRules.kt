package com.xinto.opencord.ast.rule

import com.xinto.simpleast.Node
import com.xinto.simpleast.ParseSpec
import com.xinto.simpleast.Parser
import com.xinto.simpleast.Rule
import com.xinto.opencord.ast.node.*
import com.xinto.opencord.ast.util.*
import java.util.regex.Matcher

fun <RC, S> createSpoilerRule() =
    object : Rule<RC, Node<RC>, S>(PATTERN_SPOILER) {

        override fun parse(
            matcher: Matcher,
            parser: Parser<RC, Node<RC>, S>,
            state: S,
        ) = ParseSpec.createNonTerminal(
            node = SpoilerNode<RC>(),
            state = state,
            startIndex = matcher.start(1),
            endIndex = matcher.end(1)
        )

    }

fun <RC, S> createEmoteRule() =
    object : Rule<RC, Node<RC>, S>(PATTERN_EMOTE) {

        override fun parse(
            matcher: Matcher,
            parser: Parser<RC, in Node<RC>, S>,
            state: S
        ) = ParseSpec.createTerminal(
            node = EmoteNode<RC>(matcher.group(3)!!),
            state = state,
        )

    }

fun <RC, S> createUserMentionRule() =
    object : Rule<RC, Node<RC>, S>(PATTERN_USER_MENTION) {

        override fun parse(
            matcher: Matcher,
            parser: Parser<RC, in Node<RC>, S>,
            state: S
        ): ParseSpec<RC, S> {
            val startIndex: Int
            val endIndex: Int

            val userId: String?

            val matchEveryone = matcher.group(2)
            if (matchEveryone != null) {
                userId = matchEveryone
                startIndex = matcher.start(2)
                endIndex = matcher.end(2)
            } else {
                userId = matcher.group(1)
                startIndex = matcher.start(1)
                endIndex = matcher.end(1)
            }

            return ParseSpec.createNonTerminal(
                node = UserMentionNode(userId),
                state = state,
                startIndex = startIndex,
                endIndex = endIndex
            )
        }

    }

fun <RC, S> createChannelMentionRule() =
    object : Rule<RC, Node<RC>, S>(PATTERN_CHANNEL_MENTION) {

        override fun parse(
            matcher: Matcher,
            parser: Parser<RC, in Node<RC>, S>,
            state: S,
        ) = ParseSpec.createTerminal(
            node = ChannelMentionNode<RC>(matcher.group(1)!!),
            state = state
        )

    }

fun <RC, S> createEscapeRule() =
    object : Rule<RC, Node<RC>, S>(PATTERN_ESCAPE) {

        override fun parse(
            matcher: Matcher,
            parser: Parser<RC, in Node<RC>, S>,
            state: S,
        ) = ParseSpec.createTerminal(
            node = TextNode<RC>(matcher.group(1)!!),
            state = state
        )

    }