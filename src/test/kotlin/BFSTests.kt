import graphs.algorithms.traversal.bfs
import graphs.algorithms.traversal.bfsTreeFrom
import graphs.algorithms.traversal.traverseBFS
import graphs.utils.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BFSTests {


    private val basicGraph = graph<Int> {
        for (i in 0..5) {
            node(i)
        }
        doubleEdge(0 to 1)
        doubleEdge(0 to 2)
        doubleEdge(1 to 3)
        doubleEdge(3 to 4)
        doubleEdge(2 to 5)
        doubleEdge(2 to 3)

    }

    private val fourComponentGraph = graph<Int> {
        node(1)
        node(2)
        node(3)
        node(4)
        node(5)
        doubleEdge(1 to 2)
        doubleEdge(2 to 3)
        doubleEdge(3 to 1)
    }


    @Test
    fun randomTest1() {
        val builder = StringBuilder()
        basicGraph.traverseBFS(0.node) {
            builder.append(it.value)
        }
        assertEquals("012354", builder.toString())
    }

    @Test
    fun randomTest2() {
        val bfsTree = graph<Int> {
            for (i in 0..5) {
                node(i)
            }
            edge(3 to 1)
            edge(3 to 2)
            edge(3 to 4)
            edge(1 to 0)
            edge(2 to 5)
        }
        assertEquals(bfsTree, basicGraph.bfsTreeFrom(3.node))
    }

    @Test
    fun randomTest3() {

        val bfsTree = graph<Int> {
            for (i in 0..5) {
                node(i)
            }
            edge(4 to 3)
            edge(1 to 0)
            edge(3 to 2)
            edge(3 to 1)
            edge(2 to 5)
        }
        assertEquals(bfsTree, basicGraph.bfsTreeFrom(4.node))
    }

    @Test
    fun randomTest4() {
        val bfs = bfs(fourComponentGraph, 1.node)


        val bfsTree = graph<Int> {
            node(1)
            node(2)
            node(3)
            edge(1 to 2)
            edge(1 to 3)
        }
        assertEquals(bfsTree, fourComponentGraph.bfsTreeFrom(1.node))
        assertNull(bfs[5.node])
        assertNull(bfs[4.node])
        assertEquals(1, bfs[2.node])
        assertEquals(1, bfs[3.node])
    }

    @Test
    fun randomTest5() {
        assert(!basicGraph.bfsTreeFrom(0.node).hasDirectedCycle)
        assert(!fourComponentGraph.bfsTreeFrom(1.node).hasDirectedCycle)
    }

}
