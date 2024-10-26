package com.xinto.opencord.ui.screens.home.panels.messagemenu

sealed interface HomeMessageMenuPanelState {
    object Loading : HomeMessageMenuPanelState
    object Loaded : HomeMessageMenuPanelState
    object Closing : HomeMessageMenuPanelState
}