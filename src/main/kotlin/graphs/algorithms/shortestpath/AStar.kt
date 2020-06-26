package graphs.algorithms.shortestpath

import graphs.Node
import graphs.WeightedEdge
import graphs.WeightedGraph

inline fun <T, N : Number> aStarShortestPath(
    graph: WeightedGraph<T, N>,
    heuristic: Map<Node<T>, N>,
    startNode: Node<T>,
    endNode: Node<T>,
    toN: (Double) -> N,
    toDouble: (N) -> Double
): List<WeightedEdge<T, N>> {
    return emptyList()
}


fun <T> aStarShortestPathLength(
    graph: WeightedGraph<T, Int>,
    heuristic: Map<Node<T>, Int>,
    startNode: Node<T>,
    endNode: Node<T>
): Int {
    return aStarShortestPath(
        graph,
        heuristic,
        startNode,
        endNode,
        Number::toInt,
        Number::toDouble
    ).fold(0) { acc, edge ->
        acc + edge.weight
    }
}

fun <T> aStarShortestPathLength(
    graph: WeightedGraph<T, Double>,
    heuristic: Map<Node<T>, Double>,
    startNode: Node<T>,
    endNode: Node<T>
): Double {
    return aStarShortestPath(graph, heuristic, startNode, endNode, { it }, { it }).fold(0.0) { acc, edge ->
        acc + edge.weight
    }
}