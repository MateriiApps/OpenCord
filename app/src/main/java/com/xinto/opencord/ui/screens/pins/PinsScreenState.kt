package com.xinto.opencord.ui.screens.pins

sealed interface PinsScreenState {
    object Loading : PinsScreenState
    object Loaded : PinsScreenState
    object Error : PinsScreenState
}