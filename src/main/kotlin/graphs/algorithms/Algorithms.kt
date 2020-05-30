package graphs.algorithms

import graphs.Edge
import graphs.Graph
import graphs.Node
import graphs.hasCycle
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

fun <T> Graph<T>.traverseBFS(startingNode: Node<T>, function: (Node<T>) -> Unit) {
    traverseBFSIndexed(startingNode) { node, _ ->
        function(node)
    }
}

//szintenként bejárjuk a fát csinálva a nodeokkal valamit
fun <T> Graph<T>.traverseBFSIndexed(startingNode: Node<T>, function: (Node<T>, Int) -> Unit) {
    val helper = BFS(this, startingNode)

    val levels = helper.visited.values.max() ?: return

    //elmentettük minden bejárt csúcsról hogy hanyadik szint a fában, és az alapján járjuk be
    for (i in 0..levels) {
        //"minden elemre ami az i. szinten van"
        helper.visited.filterValues { it == i }.keys.forEach {
            function(it, i)
        }
    }
}

fun <T> buildTreeChecked(graph: Graph<T>, rootNode: Node<T>, usedEdges: MutableSet<Edge<T>>): Graph<T> {
    require(graph.edges.values.flatten().containsAll(usedEdges)) //részgráf
    require(graph.nodes.contains(rootNode))

    val ret = buildTree(rootNode, usedEdges)
    require(!ret.hasCycle)
    return ret
}

fun <T> buildTree(rootNode: Node<T>, usedEdges: MutableSet<Edge<T>>): Graph<T> {
    val nodes: MutableSet<Node<T>> = HashSet()
    //root + minden felhasznált él vége lesz a csúcshalmaz
    nodes.add(rootNode)
    for (edge in usedEdges) {
        nodes.add(edge.end)
    }

    //felépítjük az adjacency listet
    val mapping: MutableMap<Node<T>, MutableList<Edge<T>>> =
        HashMap()
    //minden csúcshoz
    for (node in nodes) {
        //rendelünk egy élek listáját
        mapping[node] = LinkedList()
        //a használt élek közül
        for (edge in usedEdges) {
            //kiválasztjuk azokat amik a csúcsból indulnak
            if (edge.start == node) {
                //itt nem kéne a !! de nem elég okos a smartcast/túl okos és gondol a concurrencyre
                mapping[node]!!.add(edge)
            }
        }
    }

    return Graph(mapping.toMap())
}

fun <T> Graph<T>.traverseDFSDepth(startingNode: Node<T>, function: (Node<T>) -> Unit) {
    traverseDFSDepthIndexed(startingNode) { node, _ ->
        function(node)
    }
}

//szintenként bejárjuk a fát csinálva a nodeokkal valamit
fun <T> Graph<T>.traverseDFSDepthIndexed(startingNode: Node<T>, function: (Node<T>, Int) -> Unit) {
    val helper = DFS(this, startingNode)

    helper.depthNum.toList().sortedBy { it.second }.forEach {
        function(it.first, it.second)
    }
}

fun <T> Graph<T>.traverseDFSFinish(startingNode: Node<T>, function: (Node<T>) -> Unit) {
    traverseDFSFinishIndexed(startingNode) { node, _ ->
        function(node)
    }
}

fun <T> Graph<T>.traverseDFSFinishIndexed(startingNode: Node<T>, function: (Node<T>, Int) -> Unit) {
    val helper = DFS(this, startingNode)

    helper.finishNum.toList().sortedBy { it.second }.forEach {
        function(it.first, it.second)
    }
}

