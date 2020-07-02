package graphs.algorithms.shortestpath.tree

import graphs.Node
import graphs.WeightedEdge
import graphs.WeightedGraph
import graphs.algorithms.shortestpath.NumberAdapter
import graphs.utils.*
import kotlin.collections.set


fun <T, N : Number> bellmanFord(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    numberAdapter: NumberAdapter<N>
): Map<Node<T>, WeightedEdge<T, N>> {
    check(graph[startNode] != null)

    val distances: MutableMap<Node<T>, N> = HashMap()
    val previousNodeMapping: MutableMap<Node<T>, WeightedEdge<T, N>> = HashMap()

    //bellmanford's initialization process
    for (node in graph.nodes) {
        if (node == startNode) {
            distances[node] = numberAdapter.toN(0.0)
            continue
        }
        distances[node] = numberAdapter.toN(Double.POSITIVE_INFINITY)
    }

    val edges: List<WeightedEdge<T, N>> = graph.edges

    for (i in 1 until graph.nodes.size) { //|V| - 1 times
        edges.forEachEdgeIfBetterPathFound(distances, numberAdapter) {
            previousNodeMapping[it.end] = it
        }
    }
    edges.forEachEdgeIfBetterPathFound(distances, numberAdapter) {
        error("negative cycle detected!")
    }

    return previousNodeMapping
}

fun <T, N : Number> bellmanFordShortestTree(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    numberAdapter: NumberAdapter<N>
): WeightedGraph<T, N> {
    val previousNodeMapping = bellmanFord(graph, startNode, numberAdapter)
    return buildTreeFromPreviousNodeMapping(startNode, previousNodeMapping)
}


fun <T, N : Number> bellmanFordShortestDistances(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    numberAdapter: NumberAdapter<N>
): Map<Node<T>, N> {
    val previousNodeMapping = bellmanFord(graph, startNode, numberAdapter)
    return buildDistanceMappingFromPreviousNodeMapping(graph, startNode, previousNodeMapping, numberAdapter)
}

private fun <T, N : Number> List<WeightedEdge<T, N>>.forEachEdgeIfBetterPathFound(
    distances: MutableMap<Node<T>, N>,
    numberAdapter: NumberAdapter<N>,
    block: (edge: WeightedEdge<T, N>) -> Unit
) {
    for (edge in this) {
        val startDist = distances[edge.start] ?: error("hashcode, equals")
        val endDist = distances[edge.end] ?: error("hashcode, equals")
        val edgeWeight = edge.weight

        //minOf compares doubles properly, no need to reimplement
        distances[edge.end] = numberAdapter.toN(
            minOf(
                numberAdapter.toDouble(startDist) + numberAdapter.toDouble(edgeWeight),
                numberAdapter.toDouble(endDist)
            )
        )
        if (endDist != distances[edge.end]) {
            block(edge)
        }
    }
}
