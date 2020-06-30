package graphs.algorithms.traversal

import graphs.Edge
import graphs.Graph
import graphs.Node
import graphs.utils.buildPathFromPreviousNodeMapping
import graphs.utils.buildTreeFromPreviousNodeMapping
import graphs.utils.get


private fun <T> bfsHelper(
    graph: Graph<T>,
    current: Node<T>,
    endNode: Node<T>?,
    visited: MutableMap<Node<T>, Int>,
    previousNodeMapping: MutableMap<Node<T>, Edge<T>>,
    d: Int
) {
    visited[current] = d

    val neighbourConnections = (graph[current] ?: error("invalid graph"))
        .filter { !visited.containsKey(it.end) }

    if (neighbourConnections.isEmpty()) return

    for (edge in neighbourConnections) {
        previousNodeMapping[edge.end] = edge
        visited[edge.end] = d + 1
    }
    for (edge in neighbourConnections) {
        bfsHelper(graph, edge.end, endNode, visited, previousNodeMapping, d + 1)
    }
}


fun <T> bfs(
    graph: Graph<T>,
    startNode: Node<T>,
    endNode: Node<T>? = null
): Pair<Map<Node<T>, Edge<T>>, Map<Node<T>, Int>> {
    val visited: MutableMap<Node<T>, Int> = HashMap()
    val previousNodeMapping: MutableMap<Node<T>, Edge<T>> = HashMap()
    bfsHelper(graph, startNode, endNode, visited, previousNodeMapping, 0)

    return previousNodeMapping to visited
}

fun <T> Graph<T>.bfsTreeFrom(startNode: Node<T>): Graph<T> {
    return buildTreeFromPreviousNodeMapping(startNode, bfs(this, startNode, null).first)
}

fun <T> Graph<T>.bfsDistance(startNode: Node<T>, endNode: Node<T>): Int {
    return bfs(this, startNode, endNode).second[endNode] ?: -1
}

fun <T> bfsShortestPath(graph: Graph<T>, startNode: Node<T>, endNode: Node<T>): Graph<T> {
    return bfsShortestPathOrNull(graph, startNode, endNode) ?: error("$endNode is unreachable, or not part of graph")
}

fun <T> bfsShortestPathOrNull(graph: Graph<T>, startNode: Node<T>, endNode: Node<T>): Graph<T>? {
    val bfs = bfs(graph, startNode, endNode)
    if (!bfs.second.containsKey(endNode)) {
        return null
    }
    return buildPathFromPreviousNodeMapping(endNode, bfs.first)
}

inline fun <T> Graph<T>.traverseBFS(startingNode: Node<T>, crossinline function: (Node<T>) -> Unit) {
    traverseBFSIndexed(startingNode) { node, _ ->
        function(node)
    }
}

inline fun <T> Graph<T>.traverseBFSIndexed(startNode: Node<T>, crossinline function: (Node<T>, Int) -> Unit) {

    val bfs = bfs(this, startNode, null)
    val levels = bfs.second.values.max() ?: return

    for (i in 0..levels) {
        bfs.second.filterValues { it == i }.keys.forEach {
            function(it, i)
        }
    }
}