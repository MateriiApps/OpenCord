package com.xinto.opencord.ui.screens.home.panels.messagemenu

sealed interface HomeMessageMenuPinState {
    object Pinnable : HomeMessageMenuPinState
    object Unpinnable : HomeMessageMenuPinState
    object None : HomeMessageMenuPinState
}
