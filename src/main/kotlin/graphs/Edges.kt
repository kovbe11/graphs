package graphs

/**
 * An interface that defines an edge
 *
 * @param T Type parameter of the nodes
 * @see Node
 */
interface Edge<T> {
    /**
     * Starting node
     */
    val start: Node<T>

    /**
     * Ending node
     */
    val end: Node<T>

}

/**
 * A simple implementation of the Edge interface
 * @param T Type parameter of the nodes
 * @property start Starting node
 * @property end Ending node
 * @see Edge
 */
data class SimpleEdge<T>(override val start: Node<T>, override val end: Node<T>) : Edge<T> {

    override fun toString(): String {
        return "Edge($start to $end)"
    }

}

/**
 * @param T Type parameter of the nodes
 * @property start Starting node as MutableNode and as var
 * @property end Ending node as MutableNode and as var
 * @see MutableNode
 */
interface MutableEdge<T> : Edge<T> {
    /**
     * Starting node
     */
    override var start: MutableNode<T>

    /**
     * Ending node
     */
    override var end: MutableNode<T>
}

/**
 * A simple implementation of MutableEdge interface
 * @param T Type parameter of the nodes
 * @property start Starting node
 * @property end Ending node
 * @see MutableEdge
 */
data class SimpleMutableEdge<T>(override var start: MutableNode<T>, override var end: MutableNode<T>) : MutableEdge<T> {

    override fun toString(): String {
        return "MutableEdge($start to $end)"
    }
}

/**
 * Weighted Edge interface
 * @param T Type parameter of the nodes
 * @param N The type of number to represent the weight
 * @property weight The value of weight on this edge
 */
interface WeightedEdge<T, N : Number> : Edge<T> {
    /**
     * Value of weight
     */
    val weight: N
}

/**
 * A simple implementation of the WeightedEdge interface
 * @param T Type parameter of T
 * @param N The type of number to represent the weight
 * @property start The starting node
 * @property end The ending node
 * @property weight The value of weight on this edge
 * @see SimpleEdge
 * @see WeightedEdge
 */
data class SimpleWeightedEdge<T, N : Number>(
    override val start: Node<T>,
    override val end: Node<T>,
    override val weight: N
) : WeightedEdge<T, N> {

    override fun toString(): String {
        return "WeightedEdge($start to $end, $weight)"
    }
}

/**
 * The mutable version of the WeightedEdge interface
 * @param T The type parameter of the nodes
 * @param N The type of number to represent the weight
 * @property weight The value of weight on this edge overriden as var
 * @see MutableEdge
 * @see WeightedEdge
 */
interface MutableWeightedEdge<T, N : Number> : MutableEdge<T>, WeightedEdge<T, N> {
    override var weight: N
}

data class SimpleMutableWeightedEdge<T, N : Number>(
    override var start: MutableNode<T>,
    override var end: MutableNode<T>,
    override var weight: N
) : MutableWeightedEdge<T, N> {
    override fun toString(): String {
        return "MutableWeightedEdge($start to $end, $weight)"
    }
}
