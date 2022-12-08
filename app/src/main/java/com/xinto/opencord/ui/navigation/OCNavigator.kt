package com.xinto.opencord.ui.navigation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

@Composable
inline fun <reified T : Parcelable> rememberOCNavigatorBackstack(
    initial: T
): OCNavigator<T> {
    return remember {
        OCNavigatorBackstackImpl(initial)
    }
}

interface OCNavigator<T : Parcelable> {
    val current: T

    fun navigate(screen: T)
    fun back(): Boolean
}

class OCNavigatorBackstackImpl<T : Parcelable>(initial: T) : OCNavigator<T> {
    private val _current = mutableStateListOf(initial)

    override val current: T
        get() = _current.last()

    override fun navigate(screen: T) {
        _current.add(screen)
    }

    override fun back(): Boolean {
        if (_current.isNotEmpty()) {
            _current.removeLast()
            return true
        }
        return false
    }
}
