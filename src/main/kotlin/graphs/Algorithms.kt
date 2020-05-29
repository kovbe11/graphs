package graphs

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

class BFS<T>(private val graph: Graph<T>, val rootNode: Node<T>) {

    val bfsTree: Graph<T> //-> elkérhetjük a bfs fát

    //bejárt nodeok és azok szintje
    private val mutableVisited: MutableMap<Node<T>, Int> = HashMap()

    //kifele csak immutable!
    val visited: Map<Node<T>, Int>

    //használt élek (hogy fel tudjam építeni a bfs fát ha később kellene)
    private val mutableUsedEdges: MutableSet<Edge<T>> = HashSet()

    //kifele csak immutable!
    val usedEdges: Set<Edge<T>>

    init {
        //a számolás
        bfs(rootNode, 0)
        val adjacencyList = createAdjacencyList(rootNode)
        bfsTree = Graph(adjacencyList)
        visited = mutableVisited.toMap()
        usedEdges = mutableUsedEdges.toSet()
    }


    //getterszerűség arra hogy az adott node hanyadik szinten van
    fun visitLevel(node: Node<T>): Int? {
        if (mutableVisited.containsKey(node)) {
            return mutableVisited[node]
        }
        return null
    }

    //a bfs fa felépítéséhez
    private fun createAdjacencyList(root: Node<T>): Map<Node<T>, List<Edge<T>>> {
        val nodes: MutableSet<Node<T>> = HashSet()
        //root + minden felhasznált él vége lesz a csúcshalmaz
        nodes.add(root)
        for (edge in mutableUsedEdges) {
            nodes.add(edge.end)
        }

        //felépítjük az adjacency listet
        val mapping: MutableMap<Node<T>, MutableList<Edge<T>>> = HashMap()
        //minden csúcshoz
        for (node in nodes) {
            //rendelünk egy élek listáját
            mapping[node] = LinkedList()
            //a használt élek közül
            for (edge in mutableUsedEdges) {
                //kiválasztjuk azokat amik a csúcsból indulnak
                if (edge.start == node) {
                    //itt nem kéne a !! de nem elég okos a smartcast/túl okos és gondol a concurrencyre
                    mapping[node]!!.add(edge)
                }
            }
        }

        return mapping.toMap()
    }

    private fun bfs(current: Node<T>, d: Int) {
        //az aktuális csúcs distance-e legyen d
        mutableVisited[current] = d
        //gyűjtsük ki egy listába az aktuális csúcs olyan éleit amik még nem meglátogatott csúcsokra mutatnak
        val unvisitedNeighbourConnections =
            (graph.edges[current]
                ?: error("invalid graph")).filter { !mutableVisited.containsKey(it.end) }//itt mondom hogy "A nem meglátogatott szomszédok"
        if (unvisitedNeighbourConnections.isEmpty()) return

        //minden ilyen élre
        for (edge in unvisitedNeighbourConnections) {
            //kiválasztjuk őket
            mutableUsedEdges.add(edge)
            //bejegyezzük hogy ők d+1 távolságra vannak az aktuális csúcstól
            mutableVisited[edge.end] = d + 1
        }
        for (edge in unvisitedNeighbourConnections) {
            //majd rekurzívan megkérjük minden szomszédot hogy az ő szomszédjaikat is jelöljék meg
            bfs(edge.end, d + 1)
        }
    }

}

