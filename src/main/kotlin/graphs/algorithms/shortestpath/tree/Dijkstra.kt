package graphs.algorithms.shortestpath.tree

import graphs.Node
import graphs.WeightedEdge
import graphs.WeightedGraph
import graphs.algorithms.shortestpath.NumberAdapter
import graphs.utils.buildDistanceMappingFromPreviousNodeMapping
import graphs.utils.buildPathFromPreviousNodeMapping
import graphs.utils.buildTreeFromPreviousNodeMapping
import graphs.utils.sumEdgeWeights


@Suppress("UNCHECKED_CAST")
fun <T, N : Number> dijkstra(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    endNode: Node<T>?,
    numberAdapter: NumberAdapter<N>
): Map<Node<T>, WeightedEdge<T, N>> {
    //as dijkstra is practically a special a*, i'll reuse that code
    //dijkstra's heuristic is always 0
    return aStar(
        graph,
        startNode,
        endNode,
        { 0.0 },
        numberAdapter
    )
}

fun <T, N : Number> dijkstraShortestTree(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    numberAdapter: NumberAdapter<N>
): WeightedGraph<T, N> {
    val previousNodeMapping = dijkstra(graph, startNode, null, numberAdapter)
    return buildTreeFromPreviousNodeMapping(startNode, previousNodeMapping)
}


fun <T, N : Number> dijkstraShortestDistances(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    numberAdapter: NumberAdapter<N>
): Map<Node<T>, N> {
    val previousNodeMapping = dijkstra(graph, startNode, null, numberAdapter)
    return buildDistanceMappingFromPreviousNodeMapping(graph, startNode, previousNodeMapping, numberAdapter)
}


fun <T, N : Number> dijkstraShortestPath(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    endNode: Node<T>,
    numberAdapter: NumberAdapter<N>
): WeightedGraph<T, N> {
    val previousNodeMapping = dijkstra(graph, startNode, endNode, numberAdapter)
    return buildPathFromPreviousNodeMapping(endNode, previousNodeMapping)
}

@Suppress("UNCHECKED_CAST")
fun <T, N : Number> dijkstraShortestPathLength(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    endNode: Node<T>,
    numberAdapter: NumberAdapter<N>
): N {
    return dijkstraShortestPath(graph, startNode, endNode, numberAdapter)
        .sumEdgeWeights(numberAdapter)
}