package graphs

import graphs.algorithms.DFS

val <T> MutableGraph<T>.immutable: Graph<T>
    get() {
        return Graph(this)
    }

val <T> Graph<T>.mutable: MutableGraph<T>
    get() {
        return MutableGraph(this)
    }

val <T> MutableNode<T>.immutable: Node<T>
    get() {
        return Node(this)
    }

val <T> MutableEdge<T>.immutable: Edge<T>
    get() {
        return Edge(this)
    }

val <T, N : Number> MutableWeightedEdge<T, N>.immutable: WeightedEdge<T, N>
    get() {
        return WeightedEdge(this)
    }

val <T> Node<T>.mutable: MutableNode<T>
    get() {
        return MutableNode(this)
    }

val <T> Edge<T>.mutable: MutableEdge<T>
    get() {
        return MutableEdge(this.start.mutable, this.end.mutable)
    }

val <T> T.mutableNode: MutableNode<T>
    get() {
        return MutableNode(this)
    }

val <T> T.node: Node<T>
    get() {
        return Node(this)
    }

val <T> Pair<T, T>.mutableEdge: MutableEdge<T>
    get() {
        return MutableEdge(this.first.mutableNode, this.second.mutableNode)
    }

val <T> Pair<T, T>.edge: Edge<T>
    get() {
        return Edge(this.first.node, this.second.node)
    }

fun <T> GraphBuilder<T>.edge(edge: Pair<T, T>) {
    graph.addEdge(edge.mutableEdge)
}

fun <T> GraphBuilder<T>.doubleEdge(edge: Pair<T, T>) {
    graph.addEdge(edge.mutableEdge)
    graph.addEdge((edge.second to edge.first).mutableEdge)
}

fun <T> GraphBuilder<T>.edge(start: T, end: T) {
    edge(start to end)
}

fun <T> GraphBuilder<T>.remove(edge: Pair<T, T>) {
    graph.removeEdge(edge.mutableEdge)
}

fun <T> GraphBuilder<T>.remove(value: T) {
    graph.removeNode(value.mutableNode)
}

fun <T> GraphBuilder<T>.remove(start: T, end: T) {
    remove(start to end)
}

fun <T> GraphBuilder<T>.node(value: T) {
    graph.addNode(value.mutableNode)
}

val <T> Graph<T>.hasCycle: Boolean
    get() {
        return hasCycleGetter(this)
    }

private fun <T> hasCycleGetter(graph: Graph<T>): Boolean {
    if (hasCycleCache.containsKey(graph)) {
        return hasCycleCache[graph] ?: error("concurrency issue")
    }

    if (hasCycleCache.size > 100) { //nem tudom hogy cacheléskor mekkora mennyiséget érdemes tárolni
        hasCycleCache.remove(hasCycleCache.keys.first())
    }

    var hasCycle = false
    for (node in graph.nodes) {
        if (DFS(graph, node).hasCycle) {
            hasCycle = true
            break
        }
    }

    hasCycleCache[graph] = hasCycle
    return hasCycle
}

private val hasCycleCache: MutableMap<Graph<*>, Boolean> = HashMap()