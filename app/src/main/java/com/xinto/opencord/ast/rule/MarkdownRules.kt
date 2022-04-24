package com.xinto.opencord.ast.rule

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.xinto.opencord.ast.util.*
import com.xinto.simpleast.*

fun <RC, S> createBoldTextRule() =
    createSimpleTextRule<RC, S>(
        pattern = PATTERN_BOLD_TEXT,
        styles = listOf(
            SpanStyle(
                fontWeight = FontWeight.Bold
            )
        )
    )

fun <RC, S> createItalicAsteriskTextRule(): Rule<RC, Node<RC>, S> =
    createSimpleTextRule(
        pattern = PATTERN_ITALIC_ASTERISK_TEXT,
        styles = listOf(SpanStyle(fontStyle = FontStyle.Italic))
    )

fun <RC, S> createItalicUnderscoreTextRule(): Rule<RC, Node<RC>, S> =
    createSimpleTextRule(
        pattern = PATTERN_ITALIC_UNDERSCORE_TEXT,
        styles = listOf(SpanStyle(fontStyle = FontStyle.Italic))
    )

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