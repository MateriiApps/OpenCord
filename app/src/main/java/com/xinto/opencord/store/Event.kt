@file:Suppress("NOTHING_TO_INLINE")

package com.xinto.opencord.store

sealed interface Event<A, U, D> {
    data class Add<A, U, D>(val data: A) : Event<A, U, D>
    data class Update<A, U, D>(val data: U) : Event<A, U, D>
    data class Delete<A, U, D>(val id: D) : Event<A, U, D>

    @Suppress("FunctionName")
    companion object {
        inline fun <A, U, D> Add(data: A) = Event.Add<A, U, D>(data)
        inline fun <A, U, D> Update(data: U) = Event.Update<A, U, D>(data)
        inline fun <A, U, D> Delete(data: D) = Event.Delete<A, U, D>(data)
    }
}

inline fun <A, U, D, R> Event<A, U, D>.fold(
    onAdd: (A) -> R,
    onUpdate: (U) -> R,
    onDelete: (D) -> R,
): R {
    return when (this) {
        is Event.Add -> onAdd.invoke(data)
        is Event.Update -> onUpdate.invoke(data)
        is Event.Delete -> onDelete.invoke(id)
    }
}
