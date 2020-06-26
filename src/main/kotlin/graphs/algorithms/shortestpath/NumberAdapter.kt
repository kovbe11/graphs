package graphs.algorithms.shortestpath

interface NumberAdapter<N : Number> {
    val toN: (Double) -> N
    val toDouble: (N) -> Double
}

object IntAdapter : NumberAdapter<Int> {
    override val toN = Number::toInt
    override val toDouble = Number::toDouble
}

object DoubleAdapter : NumberAdapter<Double> {
    override val toN: (Double) -> Double = { it }
    override val toDouble: (Double) -> Double = { it }
}

