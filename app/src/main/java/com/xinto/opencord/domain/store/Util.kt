package com.xinto.opencord.domain.store

class Event<out T>(val data: T)

sealed interface ListEvent<out T> {
    val data: T

    class Add<out T>(override val data: T) : ListEvent<T>
    class Update<out T>(override val data: T): ListEvent<T>
    class Remove<out T>(override val data: T): ListEvent<T>
}