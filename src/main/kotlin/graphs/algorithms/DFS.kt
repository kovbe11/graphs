package graphs.algorithms

import graphs.Edge
import graphs.Graph
import graphs.Node

class DFS<T>(private val graph: Graph<T>, val rootNode: Node<T>) {

    //elkérhetjük a dfs fát
    val dfsTree: Graph<T>

    //mélységi számozás
    private val mutableDepthNum: MutableMap<Node<T>, Int> = HashMap()
    val depthNum: Map<Node<T>, Int>

    //visszatérési számozás
    private val mutableFinishNum: MutableMap<Node<T>, Int> = HashMap()
    val finishNum: Map<Node<T>, Int>

    //felhasznált élek
    private val mutableUsedEdges: MutableSet<Edge<T>> = HashSet()
    val usedEdges: Set<Edge<T>>

    //találtunk-e kört
    private var mutableHasCycle = false
    val hasCycle: Boolean

    //így keressük hogy van e kör a gráfban
    private val onStack: MutableSet<Node<T>> = HashSet()


    private var maxDepth: Int = 0
    private var maxFin: Int = 0

    init {
        dfs(rootNode)
        dfsTree = buildTree(rootNode, mutableUsedEdges)
        //immutable dolgok beállítása
        depthNum = mutableDepthNum
        finishNum = mutableFinishNum
        usedEdges = mutableUsedEdges
        hasCycle = mutableHasCycle
    }


    private fun dfs(current: Node<T>) {
        //beállítjuk az aktuális node számát
        mutableDepthNum[current] = maxDepth
        ++maxDepth

        //betesszük a stackre
        onStack.add(current)

        //összegyűjtjük
        val childrenConnections =
            (graph.edges[current] ?: error("invalid graph"))

        for (edge in childrenConnections) {

            if (mutableDepthNum.containsKey(edge.end)) {
                //ha olyan nodeal találkoztunk ami a stacken van akkor
                if (onStack.contains(edge.end)) {
                    mutableHasCycle = true
                }
                continue
            }

            //kiválasztjuk
            mutableUsedEdges.add(edge)
            //majd a szomszédra is meghívjuk ezt
            dfs(edge.end)
        }
        //visszatéréskor pedig megjelöljük hogy hanyadikként térünk vissza
        mutableFinishNum[current] = maxFin++

        //kivesszük a stackről
        onStack.remove(current)

    }

}
