package com.xinto.opencord.domain.usersettings

import com.github.materiiapps.enumutil.FromValue

@FromValue
enum class DomainThemeSetting(val value: String) {
    Dark("dark"),
    Light("light");

    companion object
}
