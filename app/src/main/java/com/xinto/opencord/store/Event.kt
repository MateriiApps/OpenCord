package com.xinto.opencord.store

// TODO: Partialable<T> once DomainMessagePartial can preserve inheritance
sealed interface Event<T> {
    data class Add<T>(val data: T) : Event<T>
    data class Update<T>(val data: T) : Event<T>
    data class Remove<T>(val id: Long) : Event<T>
}

@Suppress("NOTHING_TO_INLINE")
inline fun <T, R> Event<T>.fold(
    onAdd: (T) -> R,
    onUpdate: (T) -> R,
    onRemove: (Long) -> R,
): R {
    return when (this) {
        is Event.Add -> onAdd.invoke(data)
        is Event.Update -> onUpdate.invoke(data)
        is Event.Remove -> onRemove.invoke(id)
    }
}
