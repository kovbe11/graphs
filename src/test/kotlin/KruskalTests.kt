import graphs.algorithms.minspanningtree.kruskal
import graphs.utils.doubleEdge
import graphs.utils.edge
import graphs.utils.node
import graphs.utils.weightedGraph
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class KruskalTests {
    //from https://www.geeksforgeeks.org/kruskals-minimum-spanning-tree-using-stl-in-c/
    private val mst1 = weightedGraph<Int, Int> {
        node(0)
        node(1)
        node(2)
        node(3)
        node(4)
        node(5)
        node(6)
        node(7)
        node(8)
        doubleEdge(0 to 1, 4)
        doubleEdge(0 to 7, 8)
        doubleEdge(1 to 7, 11)
        doubleEdge(1 to 2, 8)
        doubleEdge(7 to 8, 7)
        doubleEdge(7 to 6, 1)
        doubleEdge(6 to 8, 6)
        doubleEdge(6 to 5, 2)
        doubleEdge(8 to 2, 2)
        doubleEdge(2 to 5, 4)
        doubleEdge(2 to 3, 7)
        doubleEdge(3 to 4, 9)
        doubleEdge(3 to 5, 14)
    }

    @Test
    fun goodMstFound() {
        val minWeight = kruskal(mst1, Comparator.naturalOrder()).adjacencyList.values.flatten().sumBy { it.weight } / 2
        assertEquals(37, minWeight)
    }

    @Test(expected = IllegalStateException::class)
    fun directedFound1() {
        val graph = weightedGraph<Int, Int> {
            node(1)
            node(2)
            edge(1 to 2, 2)
            edge(2 to 1, 1)
        }
        kruskal(graph, Comparator.naturalOrder())
    }

    @Test(expected = IllegalStateException::class)
    fun directedFound2() {
        val graph = weightedGraph<Int, Int> {
            node(1)
            node(2)
            edge(1 to 2, 2)
        }
        kruskal(graph, Comparator.naturalOrder())
    }

}