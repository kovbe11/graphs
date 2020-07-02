package graphs.algorithms.shortestpath.allpairs

import graphs.Node
import graphs.WeightedEdge
import graphs.WeightedGraph
import graphs.algorithms.shortestpath.NumberAdapter
import graphs.utils.get
import graphs.utils.nodes


typealias DistanceMatrix<N> = List<List<N>>

fun Array<DoubleArray>.forEach2DIndex(block: (i: Int, j: Int) -> Unit = { _, _ -> }) {
    for (i in this.indices) {
        for (j in this[i].indices) {
            block(i, j)
        }
    }
}

fun <N> DistanceMatrix<N>.forEach2DIndex(block: (i: Int, j: Int) -> Unit = { _, _ -> }) {
    for (i in this.indices) {
        for (j in this[i].indices) {
            block(i, j)
        }
    }
}

private fun <N> createDistanceMatrix(rows: Int, cols: Int, init: (i: Int, j: Int) -> N): DistanceMatrix<N> {
    val temp: MutableList<MutableList<N>> = ArrayList()

    for (i in 0 until rows) {
        temp.add(ArrayList())
        for (j in 0 until cols) {
            temp[i].add(init(i, j))
        }
    }
    return temp
}

@Suppress("UNCHECKED_CAST")
fun <T, N : Number> floydWarshall(
    graph: WeightedGraph<T, N>,
    numberAdapter: NumberAdapter<N>
): Pair<Map<Pair<Node<T>, Node<T>>, N>, DistanceMatrix<N>> {
    val nodeOrder = graph.nodes.toList()

    val distanceMatrix: Array<DoubleArray> =
        Array(graph.nodes.size) { DoubleArray(graph.nodes.size) }

    distanceMatrix.forEach2DIndex { i, j ->
        distanceMatrix[i][j] =
            if (i == j) {
                0.0
            } else {
                val edge =
                    graph[nodeOrder[i]]!!.find { it.end == nodeOrder[j] } as? WeightedEdge<T, N>
                if (edge == null) {
                    Double.POSITIVE_INFINITY
                } else {
                    numberAdapter.toDouble(edge.weight)
                }
            }
    }

    for (k in nodeOrder.indices) {
        distanceMatrix.forEach2DIndex { i, j ->
            distanceMatrix[i][j] = minOf(distanceMatrix[i][j], distanceMatrix[i][k] + distanceMatrix[k][j])
        }
    }

    val distances: MutableMap<Pair<Node<T>, Node<T>>, N> = HashMap()

    distanceMatrix.forEach2DIndex { i, j ->
        distances[nodeOrder[i] to nodeOrder[j]] = numberAdapter.toN(distanceMatrix[i][j])
    }

    return distances to createDistanceMatrix(nodeOrder.size, nodeOrder.size) { i, j ->
        numberAdapter.toN(distanceMatrix[i][j])
    }

}

fun <T, N : Number> WeightedGraph<T, N>.floydWarshallMapping(numberAdapter: NumberAdapter<N>): Map<Pair<Node<T>, Node<T>>, N> {
    return floydWarshall(this, numberAdapter).first
}

fun <T, N : Number> WeightedGraph<T, N>.floydWarshallDistanceMatrix(numberAdapter: NumberAdapter<N>): DistanceMatrix<N> {
    return floydWarshall(this, numberAdapter).second
}
