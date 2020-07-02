package graphs.algorithms.shortestpath.tree

import graphs.Node
import graphs.WeightedEdge
import graphs.WeightedGraph
import graphs.algorithms.shortestpath.NumberAdapter
import graphs.utils.buildPathFromPreviousNodeMapping
import graphs.utils.get
import graphs.utils.nodes
import graphs.utils.sumEdgeWeights
import java.util.*
import kotlin.collections.HashMap

@Suppress("UNCHECKED_CAST")
inline fun <T, N : Number> aStar(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    endNode: Node<T>?,
    crossinline heuristicFn: (Node<T>) -> Double,
    numberAdapter: NumberAdapter<N>
): Map<Node<T>, WeightedEdge<T, N>> {
    check(graph.adjacencyList.none { it.value.any { edge -> numberAdapter.toDouble(edge.weight) < 0 } })
    check(graph[startNode] != null && (endNode == null || graph[endNode] != null))

    val distances: MutableMap<Node<T>, N> = HashMap()
    val previousNodeMapping: MutableMap<Node<T>, WeightedEdge<T, N>> = HashMap()

    //a*'s initialization process
    for (node in graph.nodes) {
        if (node == startNode) {
            distances[node] = numberAdapter.toN(0.0)
            continue
        }
        distances[node] = numberAdapter.toN(Double.POSITIVE_INFINITY)
    }

    //priority queue for nodes
    val pqNodes = PriorityQueue<Node<T>>(graph.nodes.size,
        Comparator.comparingDouble {
            numberAdapter.toDouble(distances[it]!!) + heuristicFn(it)
        })
    pqNodes.addAll(distances.keys)

    //maximum of |V| times we do this
    while (pqNodes.isNotEmpty()) {
        val current = pqNodes.remove()

        if (endNode != null && current == endNode) {
            break
        }


        val nodeDist: N = distances[current]!!

        //check all neighbours
        for (edge in graph[current]!!) {
            //if(!nodes.contains(edge.end)) continue -> useless, less costly to just do the counting instead
            val edgeWeight: N = (edge as WeightedEdge<T, N>).weight
            val neighbourDist: N = distances[edge.end] ?: error("hashcode-equals problems???")


            //main logic, if it's better to go to that node through the current node, change it.
            distances[edge.end] = numberAdapter.toN(
                minOf(
                    numberAdapter.toDouble(neighbourDist),
                    numberAdapter.toDouble(nodeDist) + numberAdapter.toDouble(edgeWeight)
                )
            )

            //this is where the logic "already visited?" happens instead, this will always fail if it was visited

            //if minOf didn't return neighbourdist -> better path was found
            if (neighbourDist != distances[edge.end]) {
                previousNodeMapping[edge.end] = edge
                //we would need to re-sort for it to work without removing it and putting it back
                pqNodes.remove(edge.end) //O(|unvisited V|)
                pqNodes.add(edge.end)
            }

        }
    }

    return previousNodeMapping
}


inline fun <T, N : Number> aStarShortestPathOrNull(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    endNode: Node<T>,
    crossinline heuristicFn: (Node<T>) -> Double,
    numberAdapter: NumberAdapter<N>
): WeightedGraph<T, N>? {
    val previousNodeMapping = aStar(
        graph,
        startNode,
        endNode,
        heuristicFn,
        numberAdapter
    )
    return if (previousNodeMapping.containsKey(endNode))
        buildPathFromPreviousNodeMapping(endNode, previousNodeMapping)
    else null
}

inline fun <T, N : Number> aStarShortestPath(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    endNode: Node<T>,
    crossinline heuristicFn: (Node<T>) -> Double,
    numberAdapter: NumberAdapter<N>
): WeightedGraph<T, N> {
    return aStarShortestPathOrNull(graph, startNode, endNode, heuristicFn, numberAdapter)
        ?: error("$endNode unreachable")
}

fun <T, N : Number> aStarShortestPathLength(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    endNode: Node<T>,
    heuristicFn: (Node<T>) -> Double,
    numberAdapter: NumberAdapter<N>
): N {
    return aStarShortestPath(
        graph,
        startNode,
        endNode,
        heuristicFn,
        numberAdapter
    ).sumEdgeWeights(numberAdapter)
}