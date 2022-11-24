package com.xinto.opencord.store

sealed interface Event<out T> {
    val data: T?

    data class Add<out T>(override val data: T) : Event<T>
    data class Update<out T>(override val data: T) : Event<T>
    data class Remove<out T>(val id: Long, override val data: T? = null) : Event<T>
}