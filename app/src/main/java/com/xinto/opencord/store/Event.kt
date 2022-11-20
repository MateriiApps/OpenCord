package com.xinto.opencord.store

sealed interface Event<out T> {
    val data: T?

    class Add<out T>(override val data: T) : Event<T>
    class Update<out T>(override val data: T): Event<T>
    class Remove<out T>(id: Long, override val data: T? = null, ): Event<T>
}