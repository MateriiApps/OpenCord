package com.xinto.opencord.domain.activity

import androidx.compose.runtime.Immutable
import com.github.materiiapps.enumutil.FromValue

@Immutable
@FromValue
enum class ActivityType(val value: Int) {
    Game(0),
    Streaming(1),
    Listening(2),
    Watching(3),
    Custom(4),
    Competing(5),
    Unknown(-1);

    companion object
}
