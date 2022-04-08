package com.xinto.simpleast

import androidx.compose.ui.text.AnnotatedString

public open class Node<RC>(
    private var children: MutableCollection<Node<RC>> = arrayListOf(),
) {

    public val nodeChildren: Collection<Node<RC>> get() = children

    public val hasChildren: Boolean get() = children.isNotEmpty()

    public fun children(block: ChildrenScope.() -> Unit) {
        ChildrenScope().apply(block)
    }

    public open fun render(
        builder: AnnotatedString.Builder,
        renderContext: RC,
    ) {
    }

    public open class Parent<RC>(
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

    public inner class ChildrenScope {

        public fun child(node: Node<RC>) {
            children.add(node)
        }

        public fun children(vararg nodes: Node<RC>) {
            children(nodes.toList())
        }

        public fun children(nodes: Collection<Node<RC>>) {
            children.addAll(nodes)
        }
    }

}