package com.xinto.opencord.ui.util

import dev.olshevski.navigation.reimagined.NavController

fun <T> NavController<T>.topDestination() =
    backstack.entries.lastOrNull()?.destination
