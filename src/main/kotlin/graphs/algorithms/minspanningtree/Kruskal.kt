package graphs.algorithms.minspanningtree

import graphs.MutableWeightedGraph
import graphs.SimpleMutableWeightedGraph
import graphs.WeightedEdge
import graphs.WeightedGraph
import graphs.utils.*


private fun <T, N : Number> tryEdge(
    graph: MutableWeightedGraph<T, N>,
    edges: Pair<WeightedEdge<T, N>, WeightedEdge<T, N>>
) {
    graph.addNode(edges.first.start.mutable)
    graph.addNode(edges.first.end.mutable)
    graph.addEdge(edges.first.mutable)
    graph.addEdge(edges.second.mutable)
}

private fun <T, N : Number> cancelTry(
    graph: MutableWeightedGraph<T, N>,
    edges: Pair<WeightedEdge<T, N>, WeightedEdge<T, N>>
) {
    graph.removeEdge(edges.first.mutable)
    graph.removeEdge(edges.second.mutable)
    if (graph[edges.first.start].isNullOrEmpty()) {
        graph.removeNode(edges.first.start.mutable)
    }
    if (graph[edges.first.end].isNullOrEmpty()) {
        graph.removeNode(edges.first.end.mutable)
    }
}


fun <T, N : Number> kruskal(graph: WeightedGraph<T, N>, comparator: Comparator<N>): WeightedGraph<T, N> {
    check(graph.isUndirected)

    val mutableGraph = SimpleMutableWeightedGraph<T, N>()

    val allEdges = graph.edges
    val edgePairs: MutableSet<Pair<WeightedEdge<T, N>, WeightedEdge<T, N>>> = HashSet()

    allEdges.forEach {
        if (edgePairs.none { pair -> pair.second == it }) {
            val edgePair = it to (allEdges.find { edge -> edge.start == it.end && edge.end == it.start }
                ?: error("invalid graph"))
            edgePairs.add(edgePair)
        }
    }

    val sortedEdgePairs =
        edgePairs.sortedWith(Comparator { o1, o2 ->
            comparator.compare(o1.first.weight, o2.first.weight)
        })

    sortedEdgePairs.forEach {
        tryEdge(mutableGraph, it)
        if (mutableGraph.hasUndirectedCycle) {
            cancelTry(mutableGraph, it)
        }

        val edgeCount = mutableGraph.edges.size
        if (edgeCount == graph.nodes.size - 1) {
            return@forEach
        }
    }

    return mutableGraph
}