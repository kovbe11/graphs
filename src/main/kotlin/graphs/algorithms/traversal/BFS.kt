package graphs.algorithms.traversal

import graphs.Edge
import graphs.Graph
import graphs.Node
import graphs.utils.buildTree
import graphs.utils.get

class BFS<T>(private val graph: Graph<T>, val rootNode: Node<T>) {

    val bfsTree: Graph<T>

    private val mutableVisited: MutableMap<Node<T>, Int> =
        HashMap()

    val visited: Map<Node<T>, Int>

    private val mutableUsedEdges: MutableSet<Edge<T>> = HashSet()

    val usedEdges: Set<Edge<T>>

    init {
        bfs(rootNode, 0)
        bfsTree = buildTree(rootNode, mutableUsedEdges)
        visited = mutableVisited.toMap()
        usedEdges = mutableUsedEdges.toSet()
    }

    private fun bfs(current: Node<T>, d: Int) {

        mutableVisited[current] = d

        //nem meglátogatott szomszédokba mutató élek
        val neighbourConnections = (graph[current] ?: error("invalid graph"))
            .filter { !mutableVisited.containsKey(it.end) }

        if (neighbourConnections.isEmpty()) return

        for (edge in neighbourConnections) {
            mutableUsedEdges.add(edge)
            mutableVisited[edge.end] = d + 1
        }
        for (edge in neighbourConnections) {
            bfs(edge.end, d + 1)
        }
    }

}

fun <T> BFS<T>.visitLevel(node: Node<T>): Int? {
    return visited[node]
}

fun <T> Graph<T>.bfsShortestPathLength(startNode: Node<T>, endNode: Node<T>): Int {
    val bfs = BFS(this, startNode)
    return bfs.visitLevel(endNode) ?: error("$endNode is unreachable, or not in graph!")
}

inline fun <T> Graph<T>.traverseBFS(startingNode: Node<T>, crossinline function: (Node<T>) -> Unit) {
    traverseBFSIndexed(startingNode) { node, _ ->
        function(node)
    }
}

inline fun <T> Graph<T>.traverseBFSIndexed(startingNode: Node<T>, crossinline function: (Node<T>, Int) -> Unit) {
    val helper = BFS(this, startingNode)

    val levels = helper.visited.values.max() ?: return

    for (i in 0..levels) {
        helper.visited.filterValues { it == i }.keys.forEach {
            function(it, i)
        }
    }
}