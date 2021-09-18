package com.xinto.opencord.ui.simpleast.core.node

import androidx.compose.ui.text.AnnotatedString

open class Node<RC>(
    private var children: MutableCollection<Node<RC>> = arrayListOf(),
) {

    val nodeChildren: Collection<Node<RC>> get() = children

    val hasChildren get() = children.isNotEmpty()

    fun children(block: ChildrenScope.() -> Unit) {
        ChildrenScope().apply(block)
    }

    open fun render(
        builder: AnnotatedString.Builder,
        renderContext: RC,
    ) {
    }

    open class Parent<RC>(
        vararg children: Node<RC>?,
    ) : Node<RC>(children.mapNotNull { it }.toMutableList()) {

        override fun render(
            builder: AnnotatedString.Builder,
            renderContext: RC,
        ) {
            nodeChildren.forEach {
                it.render(builder, renderContext)
            }
        }
    }

    inner class ChildrenScope {

        fun child(node: Node<RC>) {
            children.add(node)
        }

        fun children(vararg nodes: Node<RC>) {
            children(nodes.toList())
        }

        fun children(nodes: Collection<Node<RC>>) {
            children.addAll(nodes)
        }
    }

}