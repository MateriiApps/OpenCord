package com.xinto.opencord.ui.simpleast.impl.rule

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.xinto.opencord.ui.simpleast.core.node.Node
import com.xinto.opencord.ui.simpleast.core.parser.ParseSpec
import com.xinto.opencord.ui.simpleast.core.parser.Parser
import com.xinto.opencord.ui.simpleast.core.rule.Rule
import com.xinto.opencord.ui.simpleast.impl.node.StyledNode
import com.xinto.opencord.ui.simpleast.impl.node.TextNode
import com.xinto.opencord.ui.simpleast.impl.util.*
import java.util.regex.Matcher

fun <RC, S> createTextRule() =
    object : Rule<RC, Node<RC>, S>(PATTERN_TEXT) {
        override fun parse(
            matcher: Matcher,
            parser: Parser<RC, in Node<RC>, S>,
            state: S,
        ): ParseSpec<RC, S> {
            val node = TextNode<RC>(matcher.group())
            return ParseSpec.createTerminal(node, state)
        }
    }

fun <RC, S> createBoldTextRule() =
    createSimpleTextRule<RC, S>(
        pattern = PATTERN_BOLD_TEXT,
        styles = listOf(
            SpanStyle(
                fontWeight = FontWeight.Bold
            )
        )
    )

fun <RC, S> createItalicTextRule() =
    object : Rule<RC, Node<RC>, S>(PATTERN_ITALIC_TEXT) {

        override fun parse(
            matcher: Matcher,
            parser: Parser<RC, in Node<RC>, S>,
            state: S,
        ): ParseSpec<RC, S> {
            val startIndex: Int
            val endIndex: Int
            val asteriskMatch = matcher.group(2)
            if (asteriskMatch != null && asteriskMatch.isNotEmpty()) {
                startIndex = matcher.start(2)
                endIndex = matcher.end(2)
            } else {
                startIndex = matcher.start(1)
                endIndex = matcher.end(1)
            }

            val node = StyledNode<RC>(listOf(SpanStyle(fontStyle = FontStyle.Italic)))
            return ParseSpec.createNonterminal(node, state, startIndex, endIndex)
        }
    }

fun <RC, S> createUnderlineTextRule() =
    createSimpleTextRule<RC, S>(
        pattern = PATTERN_UNDERLINE,
        styles = listOf(
            SpanStyle(textDecoration = TextDecoration.Underline)
        )
    )

fun <RC, S> createStrikeThroughRule() =
    createSimpleTextRule<RC, S>(
        pattern = PATTERN_STRIKETHROUGH,
        styles = listOf(
            SpanStyle(textDecoration = TextDecoration.LineThrough)
        )
    )