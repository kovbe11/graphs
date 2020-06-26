package graphs.algorithms.shortestpath.allpairs

import graphs.Node
import graphs.WeightedEdge
import graphs.WeightedGraph
import graphs.algorithms.shortestpath.DoubleAdapter
import graphs.algorithms.shortestpath.IntAdapter
import graphs.algorithms.shortestpath.NumberAdapter
import graphs.utils.get


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

fun <N> createDistanceMatrix(rows: Int, cols: Int, init: (i: Int, j: Int) -> N): DistanceMatrix<N> {
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
class FloydWarshall<T, N : Number>(
    val graph: WeightedGraph<T, N>,
    private val numberAdapter: NumberAdapter<N>
) {
    //if you want bigger numbers, you could use a mapping like [doublemin, doublemax] -> [custommin, custommax]
    //so it really works with any number

    val distances: Map<Pair<Node<T>, Node<T>>, N>
    val distanceMatrix: DistanceMatrix<N>

    private val _distanceMatrix: Array<DoubleArray> =
        Array(graph.nodes.size) { DoubleArray(graph.nodes.size) }

    //conversions for generality
    private val N.double: Double
        get() {
            return numberAdapter.toDouble(this)
        }
    private val Double.n: N
        get() {
            return numberAdapter.toN(this)
        }


    init {
        val nodeOrder = graph.nodes.toList()

        _distanceMatrix.forEach2DIndex { i, j ->
            _distanceMatrix[i][j] =
                if (i == j) {
                    0.0
                } else {
                    val edge =
                        graph[nodeOrder[i]]!!.find { it.end == nodeOrder[j] } as? WeightedEdge<T, N>
                    edge?.weight?.double ?: Double.POSITIVE_INFINITY
                }
        }

        for (k in nodeOrder.indices) {
            _distanceMatrix.forEach2DIndex { i, j ->
                _distanceMatrix[i][j] = minOf(_distanceMatrix[i][j], _distanceMatrix[i][k] + _distanceMatrix[k][j])
            }
        }

        val mutableDistances: MutableMap<Pair<Node<T>, Node<T>>, N> = HashMap()

        _distanceMatrix.forEach2DIndex { i, j ->
            mutableDistances[nodeOrder[i] to nodeOrder[j]] = _distanceMatrix[i][j].n
        }

        distances = mutableDistances

        distanceMatrix = createDistanceMatrix(nodeOrder.size, nodeOrder.size) { i, j ->
            _distanceMatrix[i][j].n
        }

    }


}


fun <T, N : Number> floydWarshallMapping(
    graph: WeightedGraph<T, N>,
    numberAdapter: NumberAdapter<N>
): Map<Pair<Node<T>, Node<T>>, N> {
    return FloydWarshall(graph, numberAdapter).distances
}

fun <T, N : Number> floydWarshallDistanceMatrix(
    graph: WeightedGraph<T, N>,
    numberAdapter: NumberAdapter<N>
): DistanceMatrix<N> {
    return FloydWarshall(graph, numberAdapter).distanceMatrix
}

fun <T> WeightedGraph<T, Int>.floydWarshallMappingInt(): Map<Pair<Node<T>, Node<T>>, Int> {
    return floydWarshallMapping(this, IntAdapter)
}

fun <T> WeightedGraph<T, Int>.floydWarshallDistanceMatrixInt(): DistanceMatrix<Int> {
    return floydWarshallDistanceMatrix(this, IntAdapter)
}

fun <T> WeightedGraph<T, Double>.floydWarshallMappingDouble(): Map<Pair<Node<T>, Node<T>>, Double> {
    return floydWarshallMapping(this, DoubleAdapter)
}

fun <T> WeightedGraph<T, Double>.floydWarshallDistanceMatrixDouble(): DistanceMatrix<Double> {
    return floydWarshallDistanceMatrix(this, DoubleAdapter)
}