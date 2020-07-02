package graphs.utils

import graphs.*

class GraphBuilder<T> {

    val graph = SimpleMutableGraph<T>()

    fun build(): Graph<T> {
        return graph.immutable
    }
}

fun <T> graph(op: GraphBuilder<T>.() -> Unit = {}): Graph<T> {
    return GraphBuilder<T>().apply(op).build()
}

fun <T> mutableGraph(op: GraphBuilder<T>.() -> Unit = {}): MutableGraph<T> {
    return GraphBuilder<T>().apply(op).graph
}


fun <T> GraphBuilder<T>.edge(edge: Pair<T, T>) {
    graph.addEdge(edge.mutableEdge)
}

fun <T> GraphBuilder<T>.edge(edge: Edge<T>) {
    graph.addEdge(edge.mutable)
}

fun <T> GraphBuilder<T>.doubleEdge(edge: Pair<T, T>) {
    edge(edge)
    edge(edge.second to edge.first)
}

fun <T> GraphBuilder<T>.node(value: T) {
    graph.addNode(value.mutableNode)
}

fun <T> GraphBuilder<T>.node(node: Node<T>) {
    graph.addNode(node.value.mutableNode)
}

class WeightedGraphBuilder<T, N : Number> {
    val graph = SimpleMutableWeightedGraph<T, N>()

    fun build(): SimpleWeightedGraph<T, N> {
        return graph.immutable
    }
}

fun <T, N : Number> weightedGraph(op: WeightedGraphBuilder<T, N>.() -> Unit): WeightedGraph<T, N> {
    return WeightedGraphBuilder<T, N>().apply(op).build()
}

fun <T, N : Number> mutableWeightedGraph(op: WeightedGraphBuilder<T, N>.() -> Unit): SimpleMutableWeightedGraph<T, N> {
    return WeightedGraphBuilder<T, N>().apply(op).graph
}

fun <T, N : Number> WeightedGraphBuilder<T, N>.node(value: T) {
    graph.addNode(value.mutableNode)
}

fun <T, N : Number> WeightedGraphBuilder<T, N>.node(node: Node<T>) {
    graph.addNode(node.mutable)
}

fun <T, N : Number> WeightedGraphBuilder<T, N>.edge(edge: Pair<T, T>, weight: N) {
    graph.addEdge(edge.mutableWeighted(weight))
}

fun <T, N : Number> WeightedGraphBuilder<T, N>.edge(edge: WeightedEdge<T, N>) {
    graph.addEdge(edge.mutable)
}

fun <T, N : Number> WeightedGraphBuilder<T, N>.doubleEdge(edge: Pair<T, T>, weight: N) {
    edge(edge, weight)
    edge(edge.second to edge.first, weight)
}

fun <T, N : Number> WeightedGraphBuilder<T, N>.doubleEdge(edge: WeightedEdge<T, N>) {
    graph.addEdge(edge.mutable)
    graph.addEdge(edge.reversed.mutable)
}

