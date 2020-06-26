package graphs.algorithms.traversal

import graphs.Edge
import graphs.Graph
import graphs.Node
import graphs.utils.buildTree
import graphs.utils.get

class DFS<T>(private val graph: Graph<T>, val rootNode: Node<T>) {

    val dfsTree: Graph<T>

    private val mutableVisited: MutableSet<Node<T>> = HashSet()
    val visited: Set<Node<T>>
        get() = mutableVisited


    private val mutableDepthOrder: MutableList<Node<T>> = ArrayList(graph.nodes.size)
    val depthNum: Map<Node<T>, Int> //if depthNum is needed O(1)
    val depthOrder: List<Node<T>> //if order is needed O(1)
        get() = mutableDepthOrder

    private val mutableFinishOrder: MutableList<Node<T>> = ArrayList(graph.nodes.size)
    val finishNum: Map<Node<T>, Int> //if finishNum is needed O(1)
    val finishOrder: List<Node<T>> //if order is needed O(1)
        get() = mutableFinishOrder


    private val mutableUsedEdges: MutableSet<Edge<T>> = HashSet()
    val usedEdges: Set<Edge<T>>

    private var mutableHasDirectedCycle = false
    val hasDirectedCycle: Boolean

    private var mutableHasUndirectedCycle = false
    val hasUndirectedCycle: Boolean

    private val onStack: MutableSet<Node<T>> = HashSet() //not used as a stack!

    private val isUndirected = graph.isUndirected

    init {
        dfs(rootNode)

        dfsTree = buildTree(rootNode, mutableUsedEdges)

        depthNum = mutableDepthOrder.mapIndexed { idx, value -> value to idx }.toMap()
        finishNum = mutableFinishOrder.mapIndexed { idx, value -> value to idx }.toMap()

        usedEdges = mutableUsedEdges
        hasDirectedCycle = mutableHasDirectedCycle
        hasUndirectedCycle = mutableHasUndirectedCycle
    }


    private fun dfs(current: Node<T>) {
        mutableDepthOrder.add(current)
        mutableVisited.add(current)
        onStack.add(current)

        val childrenConnections = (graph[current] ?: error("invalid graph"))

        for (edge in childrenConnections) {

            //check if we already have it visited
            if (mutableVisited.contains(edge.end)) {
                //check if it's on stack
                if (onStack.contains(edge.end)) {
                    mutableHasDirectedCycle = true
                    //check if it's only bc of undirected graph
                    if (isUndirected && mutableUsedEdges.none { it.start == edge.end && it.end == edge.start }) {
                        mutableHasUndirectedCycle = true
                    }
                }
                continue
            }

            mutableUsedEdges.add(edge)
            dfs(edge.end)
        }
        mutableFinishOrder.add(current)
        onStack.remove(current)
    }

}

inline fun <T> Graph<T>.traverseDFSDepth(startingNode: Node<T>, crossinline function: (Node<T>) -> Unit) {
    traverseDFSDepthIndexed(startingNode) { node, _ ->
        function(node)
    }
}

inline fun <T> Graph<T>.traverseDFSDepthIndexed(startingNode: Node<T>, function: (Node<T>, Int) -> Unit) {
    val helper = DFS(this, startingNode)

    helper.depthOrder.forEachIndexed { idx, node ->
        function(node, idx)
    }
}


inline fun <T> Graph<T>.traverseDFSFinish(startingNode: Node<T>, crossinline function: (Node<T>) -> Unit) {
    traverseDFSFinishIndexed(startingNode) { node, _ ->
        function(node)
    }
}

inline fun <T> Graph<T>.traverseDFSFinishIndexed(startingNode: Node<T>, function: (Node<T>, Int) -> Unit) {
    val helper = DFS(this, startingNode)

    helper.finishOrder.forEachIndexed { idx, node ->
        function(node, idx)
    }
}