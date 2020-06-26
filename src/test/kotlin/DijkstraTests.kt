import graphs.algorithms.shortestpath.IntAdapter
import graphs.algorithms.shortestpath.tree.dijkstraShortestDistances
import graphs.algorithms.shortestpath.tree.dijkstraShortestPath
import graphs.algorithms.shortestpath.tree.dijkstraShortestPathLength
import graphs.algorithms.shortestpath.tree.dijkstraShortestTree
import graphs.utils.edge
import graphs.utils.node
import graphs.utils.weightedGraph
import org.junit.Assert.assertEquals
import org.junit.Test

class DijkstraTests {

    @Test
    fun test1() {
        val expectedShortestPaths = weightedGraph<Char, Int> {
            for (c in 'A'..'E') {
                node(c)
            }
            edge('A' to 'D', 1)
            edge('D' to 'B', 2)
            edge('D' to 'E', 1)
            edge('E' to 'C', 5)
        }

        assertEquals(expectedShortestPaths, dijkstraShortestTree(test1, 'A'.node, IntAdapter))
        val expectedDistancesMapping = mapOf(
            'A'.node to 0,
            'B'.node to 3,
            'C'.node to 7,
            'D'.node to 1,
            'E'.node to 2
        )
        assertEquals(expectedDistancesMapping, dijkstraShortestDistances(test1, 'A'.node, IntAdapter))
    }

    @Test
    fun test2() {
//        val dijkstra = Dijkstra(test2, 'a'.node, IntAdapter)
        val expectedShortestPaths = weightedGraph<Char, Int> {
            for (c in 'a'..'h') {
                node(c)
            }
            edge('a' to 'c', 2)
            edge('c' to 'd', 2)
            edge('d' to 'b', 2)
            edge('d' to 'e', 1)
            edge('e' to 'g', 1)
            edge('g' to 'f', 2)
            edge('f' to 'h', 3)
        }
        assertEquals(expectedShortestPaths, dijkstraShortestTree(test2, 'a'.node, IntAdapter))

        val expectedDistances = mapOf(
            'a'.node to 0,
            'b'.node to 6,
            'c'.node to 2,
            'd'.node to 4,
            'e'.node to 5,
            'f'.node to 8,
            'g'.node to 6,
            'h'.node to 11
        )
        assertEquals(expectedDistances, dijkstraShortestDistances(test2, 'a'.node, IntAdapter))
    }


    @Test
    fun test3() {
        assertEquals(6, dijkstraShortestPathLength(test3, 'a'.node, 'b'.node, IntAdapter))
    }


    @Test
    fun test4() {
        assertEquals(5, dijkstraShortestPathLength(test4, 1.node, 12.node, IntAdapter))
    }


    @Test
    fun test5() {
        assertEquals(6, dijkstraShortestPathLength(test5, 1.node, 3.node, IntAdapter))
    }

    @Test(expected = IllegalStateException::class)
    fun test6() {
        val test6 = weightedGraph<Int, Int> {
            node(1)
            node(2)
            edge(1 to 2, -1)
        }
        dijkstraShortestPath(test6, 1.node, 2.node, IntAdapter)
    }
}