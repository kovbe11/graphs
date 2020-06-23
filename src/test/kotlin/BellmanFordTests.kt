import graphs.algorithms.BellmanFord
import graphs.utils.doubleEdge
import graphs.utils.edge
import graphs.utils.node
import graphs.utils.weightedGraph
import org.junit.Assert.assertEquals
import org.junit.Test

class BellmanFordTests {
    private val test1 = weightedGraph<Char, Int> {
        for (c in 'A'..'E') {
            node(c)
        }
        doubleEdge('A' to 'B', 6)
        doubleEdge('A' to 'D', 1)
        doubleEdge('B' to 'D', 2)
        doubleEdge('B' to 'E', 2)
        doubleEdge('D' to 'E', 1)
        doubleEdge('B' to 'C', 5)
        doubleEdge('E' to 'C', 5)
    }

    @Test
    fun test1() {
        val bellmanFord = BellmanFord(test1, 'A'.node, Number::toInt)

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

    }

    private val test2 = weightedGraph<Char, Int> {
        for (c in 'a'..'h') {
            node(c)
        }
        doubleEdge('a' to 'b', 8)
        doubleEdge('a' to 'c', 2)
        doubleEdge('a' to 'd', 5)
        doubleEdge('b' to 'f', 13)
        doubleEdge('b' to 'd', 2)
        doubleEdge('c' to 'd', 2)
        doubleEdge('c' to 'e', 5)
        doubleEdge('d' to 'e', 1)
        doubleEdge('d' to 'f', 6)
        doubleEdge('d' to 'g', 3)
        doubleEdge('e' to 'g', 1)
        doubleEdge('g' to 'f', 2)
        doubleEdge('g' to 'h', 6)
        doubleEdge('f' to 'h', 3)
    }

    @Test
    fun test2() {
        val bellmanFord = BellmanFord(test2, 'a'.node, Number::toInt)

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

    private val test3 = weightedGraph<Char, Int> {
        for (c in 'a'..'k') {
            node(c)
        }
        //10
        doubleEdge('a' to 'b', 10)

        //9
        doubleEdge('a' to 'f', 5)
        doubleEdge('f' to 'b', 4)

        //8
        doubleEdge('a' to 'd', 3)
        doubleEdge('d' to 'f', 1)
        //edge('f' to 'b', 4)

        //7
        doubleEdge('a' to 'g', 1)
        doubleEdge('g' to 'h', 1)
        doubleEdge('h' to 'j', 1)
        doubleEdge('j' to 'b', 4)

        //6
        doubleEdge('j' to 'k', 1)
        doubleEdge('k' to 'b', 2)

        doubleEdge('k' to 'c', 10)
        doubleEdge('g' to 'k', 8)
        doubleEdge('f' to 'g', 11)
        doubleEdge('e' to 'c', 9)
        doubleEdge('a' to 'i', 15)
    }

    @Test
    fun test3() {
        val bellmanFord = BellmanFord(test3, 'a'.node, Number::toInt)
        assertEquals(6, bellmanFord.distances['b'.node])
    }

    private val test4 = weightedGraph<Int, Int> {
        for (i in 1..12) {
            node(i)
        }
        doubleEdge(1 to 12, 9)

        doubleEdge(1 to 2, 1)
        doubleEdge(2 to 12, 7)

        doubleEdge(1 to 3, 1)
        doubleEdge(3 to 4, 1)
        doubleEdge(4 to 12, 5)

        doubleEdge(1 to 5, 1)
        doubleEdge(5 to 6, 1)
        doubleEdge(6 to 7, 1)
        doubleEdge(7 to 12, 3)

        doubleEdge(1 to 8, 1)
        doubleEdge(8 to 9, 1)
        doubleEdge(9 to 10, 1)
        doubleEdge(10 to 11, 1)
        doubleEdge(11 to 12, 1)
    }


    @Test
    fun test4() {
        val bellmanFord = BellmanFord(test4, 1.node, Number::toInt)
        assertEquals(5, bellmanFord.distances[12.node])
    }

    private val test5 = weightedGraph<Int, Int> {
        for (i in 1..7) {
            node(i)
        }
        doubleEdge(1 to 2, 10)
        doubleEdge(2 to 3, 1)

        doubleEdge(1 to 4, 1)
        doubleEdge(4 to 5, 1)
        doubleEdge(5 to 6, 1)
        doubleEdge(6 to 7, 1)
        doubleEdge(7 to 2, 1)
        doubleEdge(7 to 3, 6)

    }

    @Test
    fun test5() {
        val bellmanFord = BellmanFord(test5, 1.node, Number::toInt)
        assertEquals(6, bellmanFord.distances[3.node])
    }

    @Test(expected = IllegalStateException::class)
    fun test6() {
        val test6 = weightedGraph<Int, Int> {
            node(1)
            node(2)
            doubleEdge(1 to 2, -1)
        }
        BellmanFord(test6, 1.node, Number::toInt)
    }
}