package graphs.utils

import graphs.*
import graphs.algorithms.traversal.DFS

val <T> MutableGraph<T>.immutable: Graph<T>
    get() {
        return SimpleGraph(adjacencyList
            .mapKeys { it.key.immutable }
            .mapValues { entry -> entry.value.map { it.immutable }.toSet() })
    }

val <T> Graph<T>.mutable: MutableGraph<T>
    get() {
        return SimpleMutableGraph(adjacencyList
            .mapKeys { it.key.mutable }
            .mapValues { entry -> entry.value.map { it.mutable }.toMutableSet() }
            .toMutableMap())
    }

val <T, N : Number> MutableWeightedGraph<T, N>.immutable: SimpleWeightedGraph<T, N>
    get() {
        return SimpleWeightedGraph(adjacencyList
            .mapKeys { it.key.immutable }
            .mapValues { entry -> entry.value.map { it.immutable }.toSet() })
    }

val <T, N : Number> WeightedGraph<T, N>.mutable: SimpleMutableWeightedGraph<T, N>
    get() {

        return SimpleMutableWeightedGraph(adjacencyList
            .mapKeys { it.key.mutable }
            .mapValues { entry -> entry.value.map { it.mutable }.toMutableSet() }
            .toMutableMap())
    }


fun <T> hasDirectedCycle(graph: Graph<T>): Boolean {
    for (node in graph.nodes) {
        if (DFS(graph, node).hasDirectedCycle) {
            return true
        }
    }
    return false
}

fun <T> hasUndirectedCycle(graph: Graph<T>): Boolean {
    for (node in graph.nodes) {
        if (DFS(graph, node).hasUndirectedCycle) {
            return true
        }
    }
    return false
}

operator fun <T> MutableGraph<T>.get(node: MutableNode<T>): MutableSet<MutableEdge<T>>? {
    return adjacencyList[node]
}

operator fun <T> Graph<T>.get(node: Node<T>): Set<Edge<T>>? {
    return adjacencyList[node]
}

fun <T> isUndirected(graph: Graph<T>): Boolean {
    val edges = graph.adjacencyList.values.flatten()
    for (edge in edges) {
        //each edge has a pair
        if (graph[edge.end]!!.find { it.end == edge.start } == null) {
            return false
        }
    }
    return true
}

@Suppress("UNCHECKED_CAST")
fun <T, N : Number> isUndirectedWeighted(graph: WeightedGraph<T, N>): Boolean {
    val edges = graph.adjacencyList.values.flatten()
    for (edge in edges) {
        val pair = graph[edge.end]!!.find { it.end == edge.start } as? WeightedEdge<T, N>
        if (pair == null || pair.weight != edge.weight) {
            return false
        }
    }
    return true
}

fun <T> Graph<T>.transposed(): Graph<T> {
    return graph {
        for (node in this@transposed.nodes) {
            node(node.value)
        }
        for (edge in this@transposed.adjacencyList.values.flatten()) {
            edge(edge.end.value to edge.start.value)
        }
    }
}


operator fun <T> Graph<T>.minus(other: Set<Node<T>>): Graph<T> {
    val retAdjList = adjacencyList.filterKeys { !other.contains(it) }
        .mapValues { it.value.filter { edge -> !other.contains(edge.start) && !other.contains(edge.end) }.toSet() }
    return SimpleGraph(retAdjList)
}

fun <T> findInDegreesOfNodes(graph: Graph<T>): Map<Node<T>, Int> {
    val ret = mutableMapOf<Node<T>, Int>()
    for (node in graph.nodes) {
        ret[node] = 0
    }

    for (edge in graph.adjacencyList.values.flatten()) {
        ret[edge.end] = (ret[edge.end] as Int) + 1
    }

    return ret
}

fun <T> buildTreeChecked(graph: Graph<T>, rootNode: Node<T>, usedEdges: MutableSet<Edge<T>>): Graph<T> {
    require(graph.adjacencyList.values.flatten().containsAll(usedEdges)) //részgráf
    require(graph.nodes.contains(rootNode))

    val ret = buildTree(rootNode, usedEdges)
    require(!ret.hasDirectedCycle)
    return ret
}

fun <T> buildTree(rootNode: Node<T>, usedEdges: MutableSet<Edge<T>>): Graph<T> {
    val nodes: MutableSet<Node<T>> = HashSet()

    nodes.add(rootNode)
    for (edge in usedEdges) {
        nodes.add(edge.end)
    }

    val mapping: MutableMap<Node<T>, MutableSet<Edge<T>>> =
        HashMap()
    for (node in nodes) {
        mapping[node] = HashSet()
        for (edge in usedEdges) {
            if (edge.start == node) {
                mapping[node]!!.add(edge)
            }
        }
    }

    return SimpleGraph(mapping.toMap())
}