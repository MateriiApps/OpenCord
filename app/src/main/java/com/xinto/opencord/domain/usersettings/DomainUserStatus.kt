package com.xinto.opencord.domain.usersettings

import androidx.compose.runtime.Immutable
import com.github.materiiapps.enumutil.FromValue

@Immutable
@FromValue
enum class DomainUserStatus(val value: String) {
    Online("online"),
    Idle("idle"),
    Dnd("dnd"),
    Invisible("invisible");

    companion object
}
