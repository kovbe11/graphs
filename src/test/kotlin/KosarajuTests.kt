import graphs.algorithms.kosaraju
import graphs.utils.doubleEdge
import graphs.utils.edge
import graphs.utils.graph
import graphs.utils.node
import org.junit.Assert.assertEquals
import org.junit.Test

class KosarajuTests {


    @Test
    fun test1() {

        val test1 = graph<Int> {
            for (i in 0..8) {
                node(i)
            }
            edge(0 to 1)
            edge(1 to 2)
            edge(2 to 3)
            edge(3 to 0)
            edge(2 to 4)
            edge(4 to 5)
            edge(5 to 6)
            edge(6 to 4)
            edge(7 to 6)
            edge(7 to 8)
        }

        val kosaraju = kosaraju(test1)
        val expected = graph<Set<Int>> {
            node(setOf(0, 1, 2, 3))
            node(setOf(4, 5, 6))
            node(setOf(7))
            node(setOf(8))
            edge(setOf(0, 1, 2, 3) to setOf(4, 5, 6))
            edge(setOf(7) to setOf(4, 5, 6))
            edge(setOf(7) to setOf(8))
        }
        assertEquals(expected, kosaraju)
    }

    @Test
    fun test2() {
        val test2 = graph<Char> {
            for (c in 'a'..'k') {
                node(c)
            }
            doubleEdge('e' to 'h')
            doubleEdge('a' to 'd')
            edge('e' to 'a')
            edge('a' to 'f')
            edge('k' to 'a')
            edge('f' to 'k')
            edge('f' to 'g')
            edge('h' to 'i')
            edge('k' to 'j')
            edge('i' to 'k')
            edge('g' to 'c')
            edge('j' to 'g')
            edge('c' to 'j')
            edge('b' to 'i')
        }

        val kosaraju = kosaraju(test2)

        val expected = graph<Set<Char>> {
            val eh = setOf('e', 'h')
            val adfk = setOf('a', 'd', 'f', 'k')
            val cgj = setOf('c', 'g', 'j')
            val i = setOf('i')
            val b = setOf('b')
            node(eh)
            node(adfk)
            node(cgj)
            node(i)
            node(b)
            edge(eh to adfk)
            edge(eh to i)
            edge(b to i)
            edge(i to adfk)
            edge(adfk to cgj)
        }

        assertEquals(expected, kosaraju)
    }
}