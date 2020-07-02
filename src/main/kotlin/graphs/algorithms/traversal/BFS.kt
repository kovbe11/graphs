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
    visited: MutableMap<Node<T>, Int>,
    onVisit: (Node<T>) -> Unit = {},
    onUnvisitedEdge: (Edge<T>) -> Unit = {},
    d: Int = 0
) {
    visited[current] = d
    onVisit(current)

    val neighbourConnections = (graph[current] ?: error("invalid graph"))
        .filter { !visited.containsKey(it.end) }

    if (neighbourConnections.isEmpty()) return

    for (edge in neighbourConnections) {
        visited[edge.end] = d + 1
        onUnvisitedEdge(edge)
    }
    for (edge in neighbourConnections) {
        bfsHelper(graph, edge.end, visited, onVisit, onUnvisitedEdge, d + 1)
    }
}


fun <T> bfs(
    graph: Graph<T>,
    startNode: Node<T>,
    onVisit: (Node<T>) -> Unit = {},
    onUnvisitedEdge: (Edge<T>) -> Unit = {}
): Map<Node<T>, Int> {
    val visited: MutableMap<Node<T>, Int> = HashMap()
    bfsHelper(graph, startNode, visited, onVisit, onUnvisitedEdge)
    return visited
}

fun <T> Graph<T>.bfsTreeFrom(startNode: Node<T>): Graph<T> {
    val previousNodeMapping: MutableMap<Node<T>, Edge<T>> = HashMap()
    bfs(this, startNode, onUnvisitedEdge = { previousNodeMapping[it.end] = it })

    return buildTreeFromPreviousNodeMapping(startNode, previousNodeMapping)
}

fun <T> Graph<T>.bfsDistance(startNode: Node<T>, endNode: Node<T>): Int {
    val visited: MutableMap<Node<T>, Int> = HashMap()
    bfsHelper(this, startNode, visited, { if (it == endNode) return@bfsHelper })
    return visited[endNode] ?: -1
}

fun <T> bfsShortestPath(graph: Graph<T>, startNode: Node<T>, endNode: Node<T>): Graph<T> {
    return bfsShortestPathOrNull(graph, startNode, endNode) ?: error("$endNode unreachable")
}

fun <T> bfsShortestPathOrNull(graph: Graph<T>, startNode: Node<T>, endNode: Node<T>): Graph<T>? {
    val visited: MutableMap<Node<T>, Int> = HashMap()
    val previousNodeMapping: MutableMap<Node<T>, Edge<T>> = HashMap()
    bfsHelper(graph, startNode, visited, { if (it == endNode) return@bfsHelper }, { previousNodeMapping[it.end] = it })

    return if (visited.containsKey(endNode))
        buildPathFromPreviousNodeMapping(endNode, previousNodeMapping)
    else null
}

fun <T> Graph<T>.traverseBFS(startNode: Node<T>, function: (Node<T>) -> Unit) {
    this.traverseBFSIndexed(startNode) { node, _ -> function(node) }
}

fun <T> Graph<T>.traverseBFSIndexed(startNode: Node<T>, function: (Node<T>, Int) -> Unit) {
    val visited: MutableMap<Node<T>, Int> = HashMap()
    bfsHelper(this, startNode, visited)
    for (i in 0..visited.values.max()!!) {
        for (entry in visited.filterValues { it == i }) {
            function(entry.key, i)
        }
    }

}