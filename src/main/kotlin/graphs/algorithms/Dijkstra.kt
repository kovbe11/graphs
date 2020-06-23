package graphs.algorithms

import graphs.Node
import graphs.WeightedEdge
import graphs.WeightedGraph
import graphs.utils.edge
import graphs.utils.get
import graphs.utils.node
import graphs.utils.weightedGraph
import java.util.*
import kotlin.collections.HashMap


//for weighted minimal distances to each node from starting node. Dijkstra doesn't work with negative weights.

@Suppress("UNCHECKED_CAST")
class Dijkstra<T, N : Number>(
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
        check(graph.adjacencyList.none { it.value.any { edge -> edge.weight.double < 0 } })
        //dijkstra's initialization process
        for (node in graph.nodes) {
            if (node == startNode) {
                mutableDistances[node] = 0.0.n
                continue
            }
            mutableDistances[node] = Double.POSITIVE_INFINITY.n
        }

        dijkstra()

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


    private fun dijkstra() {

        val nodes = PriorityQueue<Node<T>>(graph.nodes.size,
            Comparator.comparingDouble { mutableDistances[it]!!.double })
        nodes.addAll(mutableDistances.keys)

        while (nodes.isNotEmpty()) {
            //get the closest node
            val current = nodes.remove()
            val nodeDist: N = mutableDistances[current] ?: error("hashcode-equals problems")

            //check all neighbours
            for (edge in graph[current]!!) {
                //if(!nodes.contains(edge.end)) continue -> this would be O(|unvisited V|), while we only need that IF we found better path
                val edgeWeight: N = (edge as WeightedEdge<T, N>).weight
                val neighbourDist: N = mutableDistances[edge.end] ?: error("hashcode-equals problems")


                //main logic, if it's better to go to that node through the current node, change it.
                mutableDistances[edge.end] = minOf(neighbourDist.double, nodeDist.double + edgeWeight.double).n

                //if minOf didn't return neighbourdist -> better path was found
                if (neighbourDist != mutableDistances[edge.end]) {
                    previousNodeMapping[edge.end] = edge
                    //we would need to re-sort for it to work without removing it and putting it back
                    nodes.remove(edge.end) //O(|unvisited V|)
                    nodes.add(edge.end)
                }
            }
        }

    }

}

fun <T, N : Number> dijkstraShortestPath(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    endNode: Node<T>,
    toN: (Double) -> N,
    toDouble: ((N) -> Double)?
): N {
    val dijkstra = toDouble?.let { Dijkstra(graph, startNode, toN, it) } ?: Dijkstra(graph, startNode, toN)

    return dijkstra.distances[endNode] ?: error("endNode is not in the graph!")
}

fun <T> WeightedGraph<T, Int>.dijkstraShortestPath(startNode: Node<T>, endNode: Node<T>): Int {
    return dijkstraShortestPath(this, startNode, endNode, Number::toInt, null)
}

fun <T> WeightedGraph<T, Double>.dijkstraShortestPath(startNode: Node<T>, endNode: Node<T>): Double {
    return dijkstraShortestPath(this, startNode, endNode, toN = { it }, toDouble = { it })
}