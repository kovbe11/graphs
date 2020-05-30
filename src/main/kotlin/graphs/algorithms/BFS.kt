package graphs.algorithms

import graphs.Edge
import graphs.Graph
import graphs.Node

class BFS<T>(private val graph: Graph<T>, val rootNode: Node<T>) {

    val bfsTree: Graph<T> //-> elkérhetjük a bfs fát

    //bejárt nodeok és azok szintje
    private val mutableVisited: MutableMap<Node<T>, Int> =
        HashMap()

    //kifele csak immutable!
    val visited: Map<Node<T>, Int>

    //használt élek (hogy fel tudjam építeni a bfs fát ha később kellene)
    private val mutableUsedEdges: MutableSet<Edge<T>> =
        HashSet()

    //kifele csak immutable!
    val usedEdges: Set<Edge<T>>

    init {
        //a számolás
        bfs(rootNode, 0)
        bfsTree = buildTree(rootNode, mutableUsedEdges)
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

    private fun bfs(current: Node<T>, d: Int) {
        //az aktuális csúcs distance-e legyen d
        mutableVisited[current] = d

        //gyűjtsük ki egy listába az aktuális csúcs olyan éleit amik még nem meglátogatott csúcsokra mutatnak
        val neighbourConnections = (graph.edges[current] ?: error("invalid graph"))
            .filter { !mutableVisited.containsKey(it.end) }//itt mondom hogy "A nem meglátogatott szomszédok"
        if (neighbourConnections.isEmpty()) return

        //minden ilyen élre
        for (edge in neighbourConnections) {
            //kiválasztjuk őket
            mutableUsedEdges.add(edge)
            //bejegyezzük hogy ők d+1 távolságra vannak az aktuális csúcstól
            mutableVisited[edge.end] = d + 1
        }
        for (edge in neighbourConnections) {
            //majd rekurzívan megkérjük minden szomszédot hogy az ő szomszédjaikat is jelöljék meg
            bfs(edge.end, d + 1)
        }
    }

}