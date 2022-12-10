package com.xinto.opencord.domain.usersettings

import androidx.compose.runtime.Immutable
import com.github.materiiapps.enumutil.FromValue

@Immutable
@FromValue
enum class DomainThemeSetting(val value: String) {
    Dark("dark"),
    Light("light");

    companion object
}
