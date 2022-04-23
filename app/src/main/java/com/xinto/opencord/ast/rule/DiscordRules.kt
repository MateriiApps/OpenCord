package com.xinto.opencord.ast.rule

import com.xinto.opencord.ast.node.*
import com.xinto.opencord.ast.util.*
import com.xinto.simpleast.*

fun <RC, S> createSpoilerRule(): Rule<RC, Node<RC>, S> =
    createRule(PATTERN_SPOILER) { matcher, _, state ->
        ParseSpec.createNonTerminal(
            node = SpoilerNode(),
            state = state,
            startIndex = matcher.start(1),
            endIndex = matcher.end(1)
        )
    }

fun <RC, S> createEmoteRule(): Rule<RC, Node<RC>, S> =
    createRule(PATTERN_EMOTE) { matcher, _, state ->
        ParseSpec.createTerminal(
            node = EmoteNode(matcher.group(3)!!),
            state = state,
        )
    }

fun <RC, S> createUserMentionRule(): Rule<RC, Node<RC>, S> =
    createRule(PATTERN_USER_MENTION) { matcher, _, state ->
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

        ParseSpec.createNonTerminal(
            node = UserMentionNode(userId),
            state = state,
            startIndex = startIndex,
            endIndex = endIndex
        )
    }

fun <RC, S> createChannelMentionRule(): Rule<RC, Node<RC>, S> =
    createRule(PATTERN_CHANNEL_MENTION) { matcher, _, state ->
        ParseSpec.createTerminal(
            node = ChannelMentionNode(matcher.group(1)!!),
            state = state
        )
    }

fun <RC, S> createEscapeRule(): Rule<RC, Node<RC>, S> =
    createRule(PATTERN_CHANNEL_MENTION) { matcher, _, state ->
        ParseSpec.createTerminal(
            node = TextNode(matcher.group(1)!!),
            state = state
        )
    }