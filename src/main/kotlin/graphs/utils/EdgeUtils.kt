package graphs.utils

import graphs.*

val <T> Pair<T, T>.mutableEdge: MutableEdge<T>
    get() {
        return SimpleMutableEdge(first.mutableNode, second.mutableNode)
    }

val <T> Pair<T, T>.edge: Edge<T>
    get() {
        return SimpleEdge(first.node, second.node)
    }

val <T> Edge<T>.mutable: MutableEdge<T>
    get() {
        return SimpleMutableEdge(start.mutable, end.mutable)
    }

val <T> Edge<T>.reversed: Edge<T>
    get() {
        return SimpleEdge(end, start)
    }

val <T, N : Number> WeightedEdge<T, N>.reversed: WeightedEdge<T, N>
    get() {
        return SimpleWeightedEdge(end, start, weight)
    }


val <T> Edge<T>.immutable: Edge<T>
    get() {
        return SimpleEdge(start, end)
    }

val <T> MutableEdge<T>.mutable: MutableEdge<T>
    get() {
        return SimpleMutableEdge(start, end)
    }

val <T> MutableEdge<T>.immutable: Edge<T>
    get() {
        return SimpleEdge(start.immutable, end.immutable)
    }

val <T, N : Number> WeightedEdge<T, N>.mutable: MutableWeightedEdge<T, N>
    get() {
        return SimpleMutableWeightedEdge(start.mutable, end.mutable, weight)
    }

val <T, N : Number> WeightedEdge<T, N>.immutable: WeightedEdge<T, N>
    get() {
        return SimpleWeightedEdge(start, end, weight)
    }

val <T, N : Number> MutableWeightedEdge<T, N>.mutable: MutableWeightedEdge<T, N>
    get() {
        return SimpleMutableWeightedEdge(start, end, weight)
    }

val <T, N : Number> MutableWeightedEdge<T, N>.immutable: WeightedEdge<T, N>
    get() {
        return SimpleWeightedEdge(start.immutable, end.immutable, weight)
    }

fun <T, N : Number> Pair<T, T>.weighted(weight: N): WeightedEdge<T, N> {
    return SimpleWeightedEdge(first.node, second.node, weight)
}

fun <T, N : Number> Pair<T, T>.mutableWeighted(weight: N): MutableWeightedEdge<T, N> {
    return SimpleMutableWeightedEdge(first.mutableNode, second.mutableNode, weight)
}

