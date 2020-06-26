package graphs.algorithms.scc

import graphs.Graph
import graphs.Node
import graphs.algorithms.traversal.DFS
import graphs.algorithms.traversal.traverseDFSDepth
import graphs.utils.*
import java.util.*
import kotlin.collections.HashSet


private fun <T> sortNodes(graph: Graph<T>): List<Node<T>> {
    var currGraph = graph
    val finishingNumbers: MutableList<Node<T>> = LinkedList()

    //O(|V|+|E|), as we need to go through each node once, and DFS is checking all the edges while doing so
    while (currGraph.nodes.isNotEmpty()) {
        val dfs = DFS(currGraph, currGraph.nodes.first())
        finishingNumbers.addAll(dfs.finishOrder)
        currGraph -= dfs.dfsTree.nodes
    }

    return finishingNumbers
}

private fun <T> findEdges(graph: Graph<T>, sccs: Set<Set<T>>): Set<Pair<Set<T>, Set<T>>> {
    val ret = mutableSetOf<Pair<Set<T>, Set<T>>>()

    //O(|E|)
    for (edge in graph.adjacencyList.values.flatten()) {
        //we find the sccs of the edges
        val start = sccs.find { it.contains(edge.start.value) } //O(1)
        val end = sccs.find { it.contains(edge.end.value) } //O(1)
        if (start == null || end == null) {
            error("Something went wrong with hashcodes and equals!")
        }
        //if it's a useful edge we add it to ret
        if (start != end) {
            ret.add((start to end)) //O(1)
        }
    }

    return ret
}


//O(|V|+|E|) -> complexity-wise optimal implementation
fun <T> kosaraju(graph: Graph<T>): Graph<Set<T>> {
    val topologicalOrder: List<Node<T>> = sortNodes(graph) //O(|V|+|E|)
    var transposedGraph = graph.transposed()//O(|V|+|E|)
    val visited = mutableSetOf<Node<T>>()
    val sccs = mutableSetOf<Set<T>>()

    //O(|V|+|E|)
    topologicalOrder.reverseForEach { node ->
        if (!visited.contains(node)) {

            val nextComponent: MutableSet<T> = HashSet()

            //traverse the graph from current node with dfs
            transposedGraph.traverseDFSDepth(node) {
                if (!visited.contains(it)) {
                    //when we find an unvisited node, we visit it, and add it to the current component
                    visited.add(it)
                    nextComponent.add(it.value)
                }
            }

            sccs.add(nextComponent)
        }
    }

    //O(|E|)
    val edges = findEdges(graph, sccs)

    //O(|V|+|E|)
    return graph {
        for (n in sccs) {
            node(n)
        }
        for (e in edges) {
            edge(e)
        }
    }
}

private inline fun <T> List<Node<T>>.reverseForEach(f: (Node<T>) -> Unit) {
    val iter = listIterator(size)
    while (iter.hasPrevious()) {
        f(iter.previous())
    }
}