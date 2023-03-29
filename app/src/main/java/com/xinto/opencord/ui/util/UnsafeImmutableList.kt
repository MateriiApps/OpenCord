package com.xinto.opencord.ui.util

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

/**
 * A compose-stable wrapper over a list for performance.
 *
 * This does NOT guarantee stability. It is merely a stable wrapper over another list,
 * and assumes the reader knows that it shouldn't change through crucial parts of rendering.
 */
@Immutable
class UnsafeImmutableList<T>(
    private val items: List<T>,
) : ImmutableList<T> {
    override val size = items.size

    override fun get(index: Int) = items[index]
    override fun isEmpty() = items.isEmpty()
    override fun iterator() = items.iterator()
    override fun listIterator() = items.listIterator()
    override fun listIterator(index: Int) = items.listIterator(index)
    override fun lastIndexOf(element: T) = items.lastIndexOf(element)
    override fun indexOf(element: T) = items.indexOf(element)
    override fun containsAll(elements: Collection<T>) = items.containsAll(elements)
    override fun contains(element: T) = items.contains(element)
}

fun <T> List<T>.toUnsafeImmutableList(): ImmutableList<T> =
    UnsafeImmutableList(this)
