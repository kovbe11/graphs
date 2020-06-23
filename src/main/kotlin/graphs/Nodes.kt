package graphs

/**
 * A wrapper that acts as a Node of a graph
 * @param T The type parameter of the wrapped value
 * @property value The wrapped value
 */
interface Node<T> {
    val value: T
}


/**
 * Equals method for Node implementations.
 * Two nodes are expected to be equal when their values are the same.
 * Same goes for the hashcode.
 * @param T The type parameter of the wrapped value
 * @param N The Node's type to make calling this function typesafe
 * @param n The node with "this"
 * @param other The other node
 * @return Returns true if both n and other implements the Node interface, and n.value equals other.value
 */
private fun <T, N : Node<T>> nodeEquals(n: N, other: Any?): Boolean {
    if (n === other) return true
    if (other !is Node<*>) return false
    return n.value == other.value
}

/**
 * A simple implementation of the Node interface
 * @param T The type parameter of the wrapped value
 * @property value The wrapped value
 */
data class SimpleNode<T>(override val value: T) : Node<T> {

    /**
     * @see nodeEquals
     * @param other The other object to compare
     * @return Returns true if other implements the Node interface, and this.value equals other.value
     */
    override fun equals(other: Any?): Boolean {
        return nodeEquals(this, other)
    }

    /**
     * @return Returns the hashcode of the wrapped value
     */
    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Node($value)"
    }

}

/**
 * A mutable version of the Node interface
 * @see Node
 * @param T The type parameter of the wrapped value
 * @property value The wrapped value overriden as var
 */
interface MutableNode<T> : Node<T> {
    /**
     * The wrapped value
     */
    override var value: T
}

/**
 * A simple implementation of the MutableNode interface
 * @see MutableNode
 * @param T The type parameter of the wrapped value
 * @property value The wrapped value overriden as var
 */
data class SimpleMutableNode<T>(override var value: T) : MutableNode<T> {

    /**
     * @see nodeEquals
     * @param other The other object to compare
     * @return Returns true if other implements the Node interface, and this.value equals other.value
     */
    override fun equals(other: Any?): Boolean {
        return nodeEquals(this, other)
    }

    /**
     * @return Returns the hashcode of the wrapped value
     */
    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }


    override fun toString(): String {
        return "MutableNode($value)"
    }
}