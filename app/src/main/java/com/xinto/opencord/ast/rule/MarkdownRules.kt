package com.xinto.opencord.ast.rule

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.xinto.simpleast.Node
import com.xinto.simpleast.ParseSpec
import com.xinto.simpleast.Parser
import com.xinto.simpleast.Rule
import com.xinto.opencord.ast.node.StyledNode
import com.xinto.opencord.ast.util.*
import java.util.regex.Matcher

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
            return ParseSpec.createNonTerminal(node, state, startIndex, endIndex)
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