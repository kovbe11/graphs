package graphs

import graphs.utils.hasDirectedCycle
import graphs.utils.hasUndirectedCycle
import graphs.utils.isUndirected
import graphs.utils.isUndirectedWeighted

interface Graph<T> {
    val nodes: Set<Node<T>>
    val adjacencyList: Map<out Node<T>, Set<Edge<T>>>
    val hasDirectedCycle: Boolean
    val hasUndirectedCycle: Boolean
    val isUndirected: Boolean
}

data class SimpleGraph<T>(override val adjacencyList: Map<Node<T>, Set<Edge<T>>> = HashMap()) : Graph<T> {

    override val nodes: Set<Node<T>> = adjacencyList.keys

    override val hasDirectedCycle: Boolean by lazy { hasDirectedCycle(this) }
    override val hasUndirectedCycle: Boolean by lazy { hasUndirectedCycle(this) }
    override val isUndirected: Boolean by lazy { isUndirected(this) }

    override fun toString(): String {
        return "Graph(nodes=$nodes, edges=$adjacencyList)"
    }
}

interface MutableGraph<T> : Graph<T> {
    override val nodes: Set<MutableNode<T>>
    override val adjacencyList: MutableMap<out MutableNode<T>, MutableSet<MutableEdge<T>>>

    fun addNode(node: MutableNode<T>)
    fun removeNode(node: MutableNode<T>)
    fun addEdge(edge: MutableEdge<T>)
    fun removeEdge(edge: MutableEdge<T>)
}

data class SimpleMutableGraph<T>
    (override val adjacencyList: MutableMap<MutableNode<T>, MutableSet<MutableEdge<T>>> = HashMap()) : MutableGraph<T> {

    override val nodes: Set<MutableNode<T>> = adjacencyList.keys

    override val hasDirectedCycle: Boolean
        get() {
            return hasDirectedCycle(this)
        }

    override val hasUndirectedCycle: Boolean
        get() {
            return hasUndirectedCycle(this)
        }

    override val isUndirected: Boolean
        get() {
            return isUndirected(this)
        }

    override fun addNode(node: MutableNode<T>) {
        if (adjacencyList[node] == null) {
            adjacencyList[node] = HashSet()
        }
    }

    override fun removeNode(node: MutableNode<T>) {
        adjacencyList.remove(node)
    }

    override fun addEdge(edge: MutableEdge<T>) {
        check(adjacencyList.containsKey(edge.start) && adjacencyList.containsKey(edge.end))
        adjacencyList[edge.start]!!.add(edge)
    }

    override fun removeEdge(edge: MutableEdge<T>) {
        check(adjacencyList.containsKey(edge.start) && adjacencyList.containsKey(edge.end))
        adjacencyList[edge.start]!!.remove(edge)
    }

    override fun toString(): String {
        return "MutableGraph(nodes=$nodes, edges=$adjacencyList)"
    }
}

interface WeightedGraph<T, N : Number> : Graph<T> {
    override val adjacencyList: Map<out Node<T>, Set<WeightedEdge<T, N>>>
}

data class SimpleWeightedGraph<T, N : Number>
    (override val adjacencyList: Map<Node<T>, Set<WeightedEdge<T, N>>> = HashMap()) : WeightedGraph<T, N> {
    override val nodes: Set<Node<T>> = adjacencyList.keys
    override val hasDirectedCycle: Boolean by lazy { hasDirectedCycle(this) }
    override val hasUndirectedCycle: Boolean by lazy { hasUndirectedCycle(this) }
    override val isUndirected: Boolean by lazy { isUndirectedWeighted(this) }

    override fun toString(): String {
        return "WeightedGraph(nodes=$nodes, edges=$adjacencyList)"
    }
}

interface MutableWeightedGraph<T, N : Number> : WeightedGraph<T, N> {
    override val adjacencyList: MutableMap<MutableNode<T>, MutableSet<MutableWeightedEdge<T, N>>>
    override val nodes: Set<MutableNode<T>>

    fun addNode(node: MutableNode<T>)
    fun removeNode(node: MutableNode<T>)
    fun addEdge(edge: MutableWeightedEdge<T, N>)
    fun removeEdge(edge: MutableWeightedEdge<T, N>)
}

data class SimpleMutableWeightedGraph<T, N : Number>
    (override val adjacencyList: MutableMap<MutableNode<T>, MutableSet<MutableWeightedEdge<T, N>>> = HashMap()) :
    MutableWeightedGraph<T, N> {

    override val nodes: Set<MutableNode<T>> = adjacencyList.keys

    override val hasDirectedCycle: Boolean
        get() {
            return hasDirectedCycle(this)
        }
    override val hasUndirectedCycle: Boolean
        get() {
            return hasUndirectedCycle(this)
        }
    override val isUndirected: Boolean
        get() {
            return isUndirectedWeighted(this)
        }

    override fun addNode(node: MutableNode<T>) {
        if (adjacencyList[node] == null) {
            adjacencyList[node] = HashSet()
        }
    }

    override fun removeNode(node: MutableNode<T>) {
        adjacencyList.remove(node)
    }

    override fun addEdge(edge: MutableWeightedEdge<T, N>) {
        check(adjacencyList.containsKey(edge.start) && adjacencyList.containsKey(edge.end))
        adjacencyList[edge.start]!!.add(edge)
    }

    override fun removeEdge(edge: MutableWeightedEdge<T, N>) {
        check(adjacencyList.containsKey(edge.start) && adjacencyList.containsKey(edge.end))
        adjacencyList[edge.start]!!.remove(edge)
    }

}
