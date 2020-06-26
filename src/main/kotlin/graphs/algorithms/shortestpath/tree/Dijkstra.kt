package graphs.algorithms.shortestpath.tree

import graphs.Node
import graphs.WeightedEdge
import graphs.WeightedGraph
import graphs.algorithms.shortestpath.NumberAdapter
import graphs.utils.edge
import graphs.utils.get
import graphs.utils.node
import graphs.utils.weightedGraph
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@Suppress("UNCHECKED_CAST")
private fun <T, N : Number> dijkstra(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    endNode: Node<T>?,
    numberAdapter: NumberAdapter<N>
): Map<Node<T>, WeightedEdge<T, N>> {
    check(graph.adjacencyList.none { it.value.any { edge -> numberAdapter.toDouble(edge.weight) < 0 } })
    check(graph[startNode] != null && (endNode == null || graph[endNode] != null))

    val distances: MutableMap<Node<T>, N> = HashMap()
    val previousNodeMapping: MutableMap<Node<T>, WeightedEdge<T, N>> = HashMap()

    //dijkstra's initialization process
    for (node in graph.nodes) {
        if (node == startNode) {
            distances[node] = numberAdapter.toN(0.0)
            continue
        }
        distances[node] = numberAdapter.toN(Double.POSITIVE_INFINITY)
    }

    //priority queue for nodes
    val pqNodes = PriorityQueue<Node<T>>(graph.nodes.size,
        Comparator.comparingDouble { numberAdapter.toDouble(distances[it]!!) })
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
            val neighbourDist: N = distances[edge.end] ?: error("hashcode-equals problems")


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

fun <T, N : Number> dijkstraShortestTree(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    numberAdapter: NumberAdapter<N>
): WeightedGraph<T, N> {
    val previousNodeMapping = dijkstra(graph, startNode, null, numberAdapter)

    return weightedGraph {
        node(startNode)
        for (n in previousNodeMapping.keys) {
            node(n)
        }
        for (e in previousNodeMapping.values) {
            edge(e)
        }
    }

}


fun <T, N : Number> dijkstraShortestDistances(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    numberAdapter: NumberAdapter<N>
): Map<Node<T>, N> {
    val previousNodeMapping = dijkstra(graph, startNode, null, numberAdapter)

    val ret: MutableMap<Node<T>, N> = HashMap()

    for (node in graph.nodes) {
        val list: MutableList<WeightedEdge<T, N>> = ArrayList()

        var prev: WeightedEdge<T, N>? = previousNodeMapping[node]
        while (prev != null) {
            list.add(prev)
            prev = previousNodeMapping[prev.start]
        }

        if (list.isEmpty()) {
            ret[node] = numberAdapter.toN(Double.POSITIVE_INFINITY)
        } else {
            ret[node] = numberAdapter.toN(list.sumByDouble { numberAdapter.toDouble(it.weight) })
        }
    }


    ret[startNode] = numberAdapter.toN(0.0)
    return ret

}


fun <T, N : Number> dijkstraShortestPath(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    endNode: Node<T>,
    numberAdapter: NumberAdapter<N>
): WeightedGraph<T, N> {

    val previousNodeMapping = dijkstra(graph, startNode, endNode, numberAdapter)

    return weightedGraph {
        node(endNode)

        var prev: WeightedEdge<T, N>? = previousNodeMapping[endNode]
        while (prev != null) {
            node(prev.start)
            edge(prev)
            prev = previousNodeMapping[prev.start]
        }
    }

}

@Suppress("UNCHECKED_CAST")
fun <T, N : Number> dijkstraShortestPathLength(
    graph: WeightedGraph<T, N>,
    startNode: Node<T>,
    endNode: Node<T>,
    numberAdapter: NumberAdapter<N>
): N {
    return dijkstraShortestPath(graph, startNode, endNode, numberAdapter).adjacencyList.values.flatten()
        .fold(numberAdapter.toN(0.0)) { acc, edge ->
            numberAdapter.toN(numberAdapter.toDouble(edge.weight) + numberAdapter.toDouble(acc))
        }
}