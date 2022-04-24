package com.xinto.opencord.ast.util

import java.util.regex.Pattern

val PATTERN_OTHER: Pattern =
    Pattern.compile("""^[\s\S]+?(?=[^0-9A-Za-z\s\u00c0-\uffff]|\n| {2,}\n|\w+:\S|${'$'})""")
val PATTERN_BOLD_TEXT: Pattern = Pattern.compile("""^\*\*([\s\S]+?)\*\*(?!\*)""")
val PATTERN_ITALIC_ASTERISK_TEXT: Pattern = Pattern.compile("""^\*(?=\S)((?:\*\*|\s+(?:[^*\s]|\*\*)|[^\s*])+?)\*(?!\*)""")
val PATTERN_ITALIC_UNDERSCORE_TEXT: Pattern = Pattern.compile("""^\b_((?:__|\\[\s\S]|[^\\_])+?)_\b""")
val PATTERN_SPOILER: Pattern = Pattern.compile("""^\|\|([\s\S]+?)\|\|""")
val PATTERN_STRIKETHROUGH: Pattern = Pattern.compile("""^~~(?=\S)([\s\S]+?\S)~~""")
val PATTERN_ESCAPE: Pattern = Pattern.compile("""^\\([^0-9A-Za-z\s])""")
val PATTERN_UNDERLINE: Pattern = Pattern.compile("""^__([\s\S]+?)__(?!_)""")
val PATTERN_EMOTE: Pattern = Pattern.compile("""^<(a)?:([a-zA-Z_0-9]+):(\d+)>""")
val PATTERN_USER_MENTION: Pattern = Pattern.compile("""^<@!?(\d+)>""")
val PATTERN_EVERYONE_MENTION: Pattern = Pattern.compile("""^@(everyone|here)""")
val PATTERN_CHANNEL_MENTION: Pattern = Pattern.compile("""^<#(\d+)>""")