package graphs

class MutableNode<T>(var value: T) {
    constructor(node: Node<T>) : this(node.value)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MutableNode<*>

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "MutableNode($value)"
    }
}

class Node<T>(val value: T) {
    constructor(mutableNode: MutableNode<T>) : this(mutableNode.value)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node<*>

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Node($value)"
    }
}
