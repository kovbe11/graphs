package graphs

import graphs.utils.nodes
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.collections.Map
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.forEach
import kotlin.collections.set

interface Graph<T> {
    val adjacencyList: Map<out Node<T>, Set<Edge<T>>>
}

data class SimpleGraph<T>(override val adjacencyList: Map<out Node<T>, Set<Edge<T>>>) : Graph<T> {
    override fun toString(): String {
        return "Graph(nodes=$nodes, edges=$adjacencyList)"
    }
}

interface MutableGraph<T> : Graph<T> {
    override val adjacencyList: MutableMap<out MutableNode<T>, MutableSet<MutableEdge<T>>>

    fun addNode(node: MutableNode<T>)
    fun removeNode(node: MutableNode<T>)
    fun addEdge(edge: MutableEdge<T>)
    fun removeEdge(edge: MutableEdge<T>)
}

data class SimpleMutableGraph<T>(
    override val adjacencyList: MutableMap<MutableNode<T>, MutableSet<MutableEdge<T>>> = HashMap()
) : MutableGraph<T> {


    override fun addNode(node: MutableNode<T>) {
        if (adjacencyList[node] == null) {
            adjacencyList[node] = HashSet()
        }
    }

    override fun removeNode(node: MutableNode<T>) {
        adjacencyList.remove(node)
        adjacencyList.values.forEach { it.removeIf { edge -> edge.end == node } }
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

    override fun toString(): String {
        return "WeightedGraph(nodes=$nodes, edges=$adjacencyList)"
    }
}

interface MutableWeightedGraph<T, N : Number> : WeightedGraph<T, N> {
    override val adjacencyList: MutableMap<MutableNode<T>, MutableSet<MutableWeightedEdge<T, N>>>

    fun addNode(node: MutableNode<T>)
    fun removeNode(node: MutableNode<T>)
    fun addEdge(edge: MutableWeightedEdge<T, N>)
    fun removeEdge(edge: MutableWeightedEdge<T, N>)
}

data class SimpleMutableWeightedGraph<T, N : Number>
    (override val adjacencyList: MutableMap<MutableNode<T>, MutableSet<MutableWeightedEdge<T, N>>> = HashMap()) :
    MutableWeightedGraph<T, N> {

    override fun addNode(node: MutableNode<T>) {
        if (adjacencyList[node] == null) {
            adjacencyList[node] = HashSet()
        }
    }

    override fun removeNode(node: MutableNode<T>) {
        adjacencyList.remove(node)
        adjacencyList.values.forEach { it.removeIf { edge -> edge.end == node } }
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
