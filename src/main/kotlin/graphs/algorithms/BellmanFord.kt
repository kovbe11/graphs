package graphs.algorithms

import graphs.Node
import graphs.WeightedEdge
import graphs.WeightedGraph
import graphs.utils.edge
import graphs.utils.node
import graphs.utils.weightedGraph
import kotlin.collections.set

@Suppress("UNCHECKED_CAST")
class BellmanFord<T, N : Number>(
    val graph: WeightedGraph<T, N>,
    val startNode: Node<T>,
    private val toN: (Double) -> N,
    private val toDouble: (N) -> Double = Number::toDouble
) {
//if you want bigger numbers, you could use a mapping like [doublemin, doublemax] -> [custommin, custommax]
//so it really works with any number

    val distances: Map<Node<T>, N>
    val shortestPathTree: WeightedGraph<T, N>

    private val mutableDistances: MutableMap<Node<T>, N> = HashMap()
    private val previousNodeMapping: MutableMap<Node<T>, WeightedEdge<T, N>> = HashMap()

    init {
        //bellmanford's initialization process
        for (node in graph.nodes) {
            if (node == startNode) {
                mutableDistances[node] = 0.0.n
                continue
            }
            mutableDistances[node] = Double.POSITIVE_INFINITY.n
        }

        bellmanFord()

        //we can build the "shortestPathTree" from the used previous edges
        shortestPathTree = weightedGraph {
            node(startNode)
            for (n in previousNodeMapping.keys) {
                node(n)
            }
            for (e in previousNodeMapping.values) {
                edge(e)
            }
        }

        distances = mutableDistances
    }

    //conversions for generality
    private val N.double: Double
        get() {
            return toDouble(this)
        }
    private val Double.n: N
        get() {
            return toN(this)
        }

    private fun bellmanFord() {

        val edges = graph.adjacencyList.values.flatten()

        for (i in 1 until graph.nodes.size) { //|V| - 1 times

            for (edge in edges) {
                val startDist = mutableDistances[edge.start] ?: error("hashcode, equals")
                val endDist = mutableDistances[edge.end] ?: error("hashcode, equals")
                val edgeWeight = edge.weight

                //minOf compares doubles properly, no need to reimplement
                mutableDistances[edge.end] = minOf(startDist.double + edgeWeight.double, endDist.double).n
                if (endDist != mutableDistances[edge.end]) {
                    previousNodeMapping[edge.end] = edge
                }
            }

        }
        for (edge in edges) {
            val startDist = mutableDistances[edge.start] ?: error("hashcode, equals")
            val endDist = mutableDistances[edge.end] ?: error("hashcode, equals")
            val edgeWeight = edge.weight

            if (startDist.double + edgeWeight.double < endDist.double) {
                error("negative cycle was found!")
            }
        }
    }

}

fun <T, N : Number> bellmanFordShortestPath(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    endNode: Node<T>,
    toN: (Double) -> N,
    toDouble: ((N) -> Double)?
): N {
    val bellmanFord = toDouble?.let { BellmanFord(graph, startNode, toN, it) } ?: BellmanFord(graph, startNode, toN)

    return bellmanFord.distances[endNode] ?: error("endNode is not in the graph!")
}

fun <T> WeightedGraph<T, Int>.bellmanFordShortestPath(startNode: Node<T>, endNode: Node<T>): Int {
    return bellmanFordShortestPath(this, startNode, endNode, Number::toInt, null)
}

fun <T> WeightedGraph<T, Double>.bellmanFordShortestPath(startNode: Node<T>, endNode: Node<T>): Double {
    return bellmanFordShortestPath(this, startNode, endNode, toN = { it }, toDouble = { it })
}