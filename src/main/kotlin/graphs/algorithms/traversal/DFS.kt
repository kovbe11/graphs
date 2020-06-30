package graphs.algorithms.traversal

import graphs.Edge
import graphs.Graph
import graphs.Node
import graphs.utils.buildTreeFromPreviousNodeMapping
import graphs.utils.get


class DFSHelper<T>(
    private val graph: Graph<T>,
    startNode: Node<T>,
    val onEnter: (Node<T>) -> Unit = {},
    val onVisitedEdge: (Edge<T>) -> Unit = {},
    val onNonVisitedEdge: (Edge<T>) -> Unit = {},
    val onFinishing: (Node<T>) -> Unit = {}
) {
    private val visited: MutableSet<Node<T>> = HashSet()
    val nodesReached: Int


    init {
        dfs(startNode)
        nodesReached = visited.size
    }

    private fun dfs(current: Node<T>) {

        visited.add(current)
        onEnter(current)

        val childrenConnections = (graph[current] ?: error("invalid graph"))

        for (edge in childrenConnections) {

            //check if we already have it visited
            if (visited.contains(edge.end)) {
                onVisitedEdge(edge)
                continue
            }
            onNonVisitedEdge(edge)
            dfs(edge.end)
        }
        onFinishing(current)
    }
}


fun <T> Graph<T>.traverseDFSDepth(startNode: Node<T>, function: (Node<T>) -> Unit) {
    DFSHelper(this, startNode, function)
}

inline fun <T> Graph<T>.traverseDFSDepthIndexed(startNode: Node<T>, crossinline function: (Node<T>, Int) -> Unit) {
    var i = 0
    DFSHelper(this, startNode, { function(it, i++) })
}

fun <T> Graph<T>.traverseDFSFinish(startNode: Node<T>, function: (Node<T>) -> Unit) {
    DFSHelper(this, startNode, onFinishing = function)
}

inline fun <T> Graph<T>.traverseDFSFinishIndexed(startNode: Node<T>, crossinline function: (Node<T>, Int) -> Unit) {
    var i = 0
    DFSHelper(this, startNode, onFinishing = { function(it, i++) })
}

fun <T> Graph<T>.dfsDepthOrderFrom(startNode: Node<T>): List<Node<T>> {
    val list: MutableList<Node<T>> = mutableListOf()
    DFSHelper(this, startNode, { list.add(it) })
    return list
}

fun <T> Graph<T>.dfsFinishOrderFrom(startNode: Node<T>): List<Node<T>> {
    val list: MutableList<Node<T>> = mutableListOf()
    DFSHelper(this, startNode, onFinishing = { list.add(it) })
    return list
}

fun <T> Graph<T>.dfsDepthNumMappingFrom(startNode: Node<T>): Map<Node<T>, Int> {
    var i = 0
    val mapping: MutableMap<Node<T>, Int> = HashMap()
    DFSHelper(this, startNode, { mapping[it] = i++ })
    return mapping
}

fun <T> Graph<T>.dfsFinishNumMappingFrom(startNode: Node<T>): Map<Node<T>, Int> {
    var i = 0
    val mapping: MutableMap<Node<T>, Int> = HashMap()
    DFSHelper(this, startNode, onFinishing = { mapping[it] = i++ })
    return mapping
}

fun <T> Graph<T>.dfsDepthNumOf(of: Node<T>, from: Node<T>): Int {
    var i = 0
    val checked = DFSHelper(this, from, {
        if (it == of) {
            return@DFSHelper
        }
        ++i
    }).nodesReached

    return if (checked == i) {
        -1
    } else {
        i
    }
}

fun <T> Graph<T>.dfsFinishNumOf(of: Node<T>, from: Node<T>): Int {
    var i = 0
    val checked = DFSHelper(this, from, onFinishing = {
        if (it == of) {
            return@DFSHelper
        }
        ++i
    }).nodesReached

    return if (checked == i) {
        -1
    } else {
        i
    }
}

fun <T> Graph<T>.dfsTreeFrom(startNode: Node<T>): Graph<T> {
    val previousNodeMapping: MutableMap<Node<T>, Edge<T>> = HashMap()
    DFSHelper(this, startNode, onNonVisitedEdge = { previousNodeMapping[it.end] = it })
    return buildTreeFromPreviousNodeMapping(startNode, previousNodeMapping)
}

fun <T> dfsDirectedDetectCycleFrom(graph: Graph<T>, startNode: Node<T>): Boolean {
    val onStack: MutableSet<Node<T>> = HashSet()
    var directedCycleFound = false

    DFSHelper(graph,
        startNode,
        onEnter = { onStack.add(it) },
        onVisitedEdge = { if (onStack.contains(it.end)) directedCycleFound = true; return@DFSHelper },
        onFinishing = { onStack.remove(it) })

    return directedCycleFound
}

fun <T> dfsUnDirectedDetectCycleFrom(graph: Graph<T>, startNode: Node<T>): Boolean {
    if (!graph.isUndirected) {
        return false
    }
    val previousNodeMapping: MutableMap<Node<T>, Edge<T>> = HashMap()
    val onStack: MutableSet<Node<T>> = HashSet()
    var unDirectedCycleFound = false

    DFSHelper(graph,
        startNode,
        onEnter = { onStack.add(it) },
        onVisitedEdge = {
            if (onStack.contains(it.end) && previousNodeMapping.values.none { edge ->
                    it.start == edge.end && it.end == edge.start
                }) {
                unDirectedCycleFound = true
                return@DFSHelper
            }
        },
        onNonVisitedEdge = { previousNodeMapping[it.end] = it },
        onFinishing = { onStack.remove(it) })

    return unDirectedCycleFound
}
