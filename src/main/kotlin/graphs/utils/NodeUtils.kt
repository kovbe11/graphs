package graphs.utils

import graphs.MutableNode
import graphs.Node
import graphs.SimpleMutableNode
import graphs.SimpleNode

val <T> MutableNode<T>.immutable: Node<T>
    get() {
        return SimpleNode(value)
    }

val <T> Node<T>.mutable: MutableNode<T>
    get() {
        return SimpleMutableNode(value)
    }


val <T> T.mutableNode: MutableNode<T>
    get() {
        return SimpleMutableNode(this)
    }

val <T> T.node: Node<T>
    get() {
        return SimpleNode(this)
    }

