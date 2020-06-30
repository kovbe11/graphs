package graphs.utils

import graphs.*
import graphs.algorithms.shortestpath.NumberAdapter
import graphs.algorithms.traversal.dfsDirectedDetectCycleFrom
import graphs.algorithms.traversal.dfsUnDirectedDetectCycleFrom

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
    return graph.nodes.any { dfsDirectedDetectCycleFrom(graph, it) }
}

fun <T> hasUndirectedCycle(graph: Graph<T>): Boolean {
    return graph.nodes.any { dfsUnDirectedDetectCycleFrom(graph, it) }
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

operator fun <T, N : Number> WeightedGraph<T, N>.minus(other: Set<Node<T>>): WeightedGraph<T, N> {
    val retAdjList = adjacencyList.filterKeys { !other.contains(it) }
        .mapValues {
            it.value.filter { weightedEdge ->
                !other.contains(weightedEdge.start) && !other.contains(
                    weightedEdge.end
                )
            }.toSet()
        }
    return SimpleWeightedGraph(retAdjList)
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


fun <T, N : Number> buildPathFromPreviousNodeMapping(
    endNode: Node<T>,
    previousNodeMapping: Map<Node<T>, WeightedEdge<T, N>>
): WeightedGraph<T, N> {
    return weightedGraph {
        node(endNode)

        var prev: WeightedEdge<T, N>? = previousNodeMapping[endNode]
        while (prev != null) {
            node(prev.start)
            edge(prev)
            prev = previousNodeMapping[prev.start]
        }
    }
}

fun <T> buildPathFromPreviousNodeMapping(
    endNode: Node<T>,
    previousNodeMapping: Map<Node<T>, Edge<T>>
): Graph<T> {
    return graph {
        node(endNode)

        var prev: Edge<T>? = previousNodeMapping[endNode]
        while (prev != null) {
            node(prev.start)
            edge(prev)
            prev = previousNodeMapping[prev.start]
        }
    }
}

fun <T, N : Number> buildTreeFromPreviousNodeMapping(
    startNode: Node<T>,
    previousNodeMapping: Map<Node<T>, WeightedEdge<T, N>>
): WeightedGraph<T, N> {
    return weightedGraph {
        node(startNode)
        for (n in previousNodeMapping.keys) {
            node(n)
        }
        for (e in previousNodeMapping.values) {
            edge(e)
        }
    }
}

fun <T> buildTreeFromPreviousNodeMapping(
    startNode: Node<T>,
    previousNodeMapping: Map<Node<T>, Edge<T>>
): Graph<T> {
    return graph {
        node(startNode)
        for (n in previousNodeMapping.keys) {
            node(n)
        }
        for (e in previousNodeMapping.values) {
            edge(e)
        }
    }
}

fun <T, N : Number> buildDistanceMappingFromPreviousNodeMapping(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    previousNodeMapping: Map<Node<T>, WeightedEdge<T, N>>,
    numberAdapter: NumberAdapter<N>
): Map<Node<T>, N> {
    val ret: MutableMap<Node<T>, N> = HashMap()

    for (node in graph.nodes) {
        val list: MutableList<WeightedEdge<T, N>> = ArrayList()

        var prev: WeightedEdge<T, N>? = previousNodeMapping[node]
        while (prev != null) {
            list.add(prev)
            prev = previousNodeMapping[prev.start]
        }

        if (list.isEmpty()) {
            ret[node] = numberAdapter.toN(Double.POSITIVE_INFINITY)
        } else {
            ret[node] = numberAdapter.toN(list.sumByDouble { numberAdapter.toDouble(it.weight) })
        }
    }

    ret[startNode] = numberAdapter.toN(0.0)
    return ret
}

fun <T, N : Number> WeightedGraph<T, N>.sumEdgeWeights(numberAdapter: NumberAdapter<N>): N {
    return adjacencyList.values.flatten()
        .fold(numberAdapter.toN(0.0)) { acc, edge ->
            numberAdapter.toN(numberAdapter.toDouble(edge.weight) + numberAdapter.toDouble(acc))
        }
}
