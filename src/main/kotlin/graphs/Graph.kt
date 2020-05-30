package graphs

import java.util.*
import kotlin.collections.HashMap


//ezt amúgy hogy érdemes csinálni? örököljön a MutableGraph a Graph-ból? interface?

data class MutableGraph<T>(
    val edges: MutableMap<MutableNode<T>, MutableList<MutableEdge<T>>> = HashMap()
) {

    val nodes: MutableSet<MutableNode<T>>
        get() {
            return edges.keys
        }

    constructor(graph: Graph<T>)
            : this(graph.edges.mapKeys { it.key.mutable }
        .mapValues { entry -> entry.value.map { it.makeMutable() }.toMutableList() }
        .toMutableMap())

    fun addNode(node: MutableNode<T>) {
        edges[node] = LinkedList()
    }

    fun removeNode(node: MutableNode<T>) {
        edges.remove(node)
    }

    fun addEdge(edge: MutableEdge<T>) {
        check(edges.containsKey(edge.start) && edges.containsKey(edge.end))
        edges[edge.start]!!.add(edge)
    }

    fun removeEdge(edge: MutableEdge<T>) {
        check(edges.containsKey(edge.start) && edges.containsKey(edge.end))
        edges[edge.start]!!.remove(edge)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MutableGraph<*>

        if (nodes != other.nodes) return false
        //order doesn't matter!
        for (entry in edges.entries) {
            if (!(other.edges[entry.key] ?: return false).containsAll(entry.value)) {
                return false
            }
        }

        return true
    }

    override fun hashCode(): Int {
        var result = nodes.hashCode()
        for (entry in edges.entries) {
            result = 31 * result + (entry.value.sumBy { it.hashCode() })  //so that order doesn't matter!
        }
        return result
    }

    override fun toString(): String {
        return "MutableGraph(nodes=$nodes, edges=$edges)"
    }
}

class Graph<T>(val edges: Map<Node<T>, List<Edge<T>>>) {

    val nodes: Set<Node<T>>
        get() {
            return edges.keys
        }

    constructor(mutableGraph: MutableGraph<T>)
            : this(mutableGraph.edges.mapKeys { it.key.immutable }
        .mapValues { entry -> entry.value.map { it.makeImmutable() } })

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Graph<*>

        if (nodes != other.nodes) return false
        //order doesn't matter!
        for (entry in edges.entries) {
            if (!(other.edges[entry.key] ?: return false).containsAll(entry.value)) {
                return false
            }
        }

        return true
    }

    override fun hashCode(): Int {
        var result = nodes.hashCode()
        for (entry in edges.entries) {
            result = 31 * result + (entry.value.sumBy { it.hashCode() })  //so that order doesn't matter!
        }
        return result
    }

    override fun toString(): String {
        return "Graph(nodes=$nodes, edges=$edges)"
    }
}

class GraphBuilder<T> {

    val graph = MutableGraph<T>()

    fun build(): Graph<T> {
        return graph.immutable
    }
}


fun <T> graph(op: GraphBuilder<T>.() -> Unit): Graph<T> {
    return GraphBuilder<T>().apply(op).build()
}

fun <T> mutableGraph(op: GraphBuilder<T>.() -> Unit): MutableGraph<T> {
    return GraphBuilder<T>().apply(op).graph
}