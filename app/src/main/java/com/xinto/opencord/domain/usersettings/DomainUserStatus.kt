package com.xinto.opencord.domain.usersettings

import com.github.materiiapps.enumutil.FromValue

@FromValue
enum class DomainUserStatus(val value: String) {
    Online("online"),
    Idle("idle"),
    Dnd("dnd"),
    Invisible("invisible");

    companion object
}
