package graphs.algorithms

import graphs.SimpleMutableWeightedGraph
import graphs.WeightedEdge
import graphs.WeightedGraph
import graphs.utils.get
import graphs.utils.immutable
import graphs.utils.mutable

class Kruskal<T, N : Number>(private val graph: WeightedGraph<T, N>) {

    val minimumSpanningForest: WeightedGraph<T, N>

    private var mutableGraph = SimpleMutableWeightedGraph<T, N>()

    init {
        check(graph.isUndirected)
        kruskal()
        minimumSpanningForest = mutableGraph.immutable
    }

    private fun tryEdge(edge1: WeightedEdge<T, N>, edge2: WeightedEdge<T, N>) {
        mutableGraph.addNode(edge1.start.mutable)
        mutableGraph.addNode(edge1.end.mutable)
        mutableGraph.addEdge(edge1.mutable)
        mutableGraph.addEdge(edge2.mutable)
    }

    private fun cancelTry(edge1: WeightedEdge<T, N>, edge2: WeightedEdge<T, N>) {
        mutableGraph.removeEdge(edge1.mutable)
        mutableGraph.removeEdge(edge2.mutable)
        if (mutableGraph[edge1.start].isNullOrEmpty()) {
            mutableGraph.removeNode(edge1.start.mutable)
        }
        if (mutableGraph[edge1.end].isNullOrEmpty()) {
            mutableGraph.removeNode(edge1.end.mutable)
        }
    }

    private fun kruskal() {
        val allEdges = graph.adjacencyList.values.flatten()
        val edgePairs: MutableSet<Pair<WeightedEdge<T, N>, WeightedEdge<T, N>>> = HashSet()

        allEdges.forEach {
            if (edgePairs.none { pair -> pair.second == it }) {
                val edgePair = it to (allEdges.find { edge -> edge.start == it.end && edge.end == it.start }
                    ?: error("invalid graph"))
                edgePairs.add(edgePair)
            }
        }

        val sortedEdgePairs = edgePairs.sortedBy { it.first.weight.toDouble() }

        sortedEdgePairs.forEach {
            tryEdge(it.first, it.second)
            if (mutableGraph.hasUndirectedCycle) {
                cancelTry(it.first, it.second)
            }

            if (mutableGraph.adjacencyList.values.flatten().size == graph.nodes.size - 1) {
                return@forEach
            }
        }

    }
}