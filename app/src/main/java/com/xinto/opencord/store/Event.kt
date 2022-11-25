package com.xinto.opencord.store

sealed interface Event<T> {
    val data: T?

    data class Add<T>(override val data: T) : Event<T>
    data class Update<T>(override val data: T) : Event<T>
    data class Remove<T>(val id: Long, override val data: T? = null) : Event<T>
}

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Event<T>.fold(
    onAdd: (T) -> Unit,
    onUpdate: (T) -> Unit,
    onRemove: (Long) -> Unit,
) {
    when (this) {
        is Event.Add -> onAdd.invoke(data)
        is Event.Update -> onUpdate.invoke(data)
        is Event.Remove -> onRemove.invoke(id)
    }
}
