import graphs.algorithms.shortestpath.IntAdapter
import graphs.algorithms.shortestpath.tree.BellmanFord
import graphs.utils.doubleEdge
import graphs.utils.edge
import graphs.utils.node
import graphs.utils.weightedGraph
import org.junit.Assert.assertEquals
import org.junit.Test

class BellmanFordTests {

    @Test
    fun test1() {
        val bellmanFord =
            BellmanFord(test1, 'A'.node, IntAdapter)

        val expectedDistancesMapping = mapOf(
            'A'.node to 0,
            'B'.node to 3,
            'C'.node to 7,
            'D'.node to 1,
            'E'.node to 2
        )
        assertEquals(expectedDistancesMapping, bellmanFord.distances)

        val expectedShortestPaths = weightedGraph<Char, Int> {
            for (c in 'A'..'E') {
                node(c)
            }
            edge('A' to 'D', 1)
            edge('D' to 'B', 2)
            edge('D' to 'E', 1)
            edge('E' to 'C', 5)
        }

        assertEquals(expectedShortestPaths, bellmanFord.shortestPathTree)
//        assertEquals(7, test1.bellmanFordShortestPath('A'.node, 'C'.node))
    }


    @Test
    fun test2() {
        val bellmanFord =
            BellmanFord(test2, 'a'.node, IntAdapter)

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
        assertEquals(expectedDistances, bellmanFord.distances)

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
        assertEquals(expectedShortestPaths, bellmanFord.shortestPathTree)

    }


    @Test
    fun test3() {
        val bellmanFord =
            BellmanFord(test3, 'a'.node, IntAdapter)
        assertEquals(6, bellmanFord.distances['b'.node])
    }



    @Test
    fun test4() {
        val bellmanFord =
            BellmanFord(test4, 1.node, IntAdapter)
        assertEquals(5, bellmanFord.distances[12.node])
    }


    @Test
    fun test5() {
        val bellmanFord =
            BellmanFord(test5, 1.node, IntAdapter)
        assertEquals(6, bellmanFord.distances[3.node])
    }


    @Test
    fun test6() {
//        println(test6)
//        println(test6.bellmanFordShortestPath(5.node, 2.node))
    }

    @Test(expected = IllegalStateException::class)
    fun test7() {
        val test6 = weightedGraph<Int, Int> {
            node(1)
            node(2)
            doubleEdge(1 to 2, -1)
        }
        BellmanFord(test6, 1.node, IntAdapter)
    }
}