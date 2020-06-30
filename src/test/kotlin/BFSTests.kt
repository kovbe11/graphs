import graphs.algorithms.traversal.bfs
import graphs.algorithms.traversal.bfsTreeFrom
import graphs.algorithms.traversal.traverseBFS
import graphs.utils.doubleEdge
import graphs.utils.edge
import graphs.utils.graph
import graphs.utils.node
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BFSTests {

    //TODO: több teszt beszédes névvel

    private val basicGraph = graph<Int> {
        node(0)
        node(1)
        node(2)
        node(3)
        node(4)
        node(5)
        doubleEdge(0 to 1)
        doubleEdge(0 to 2)
        doubleEdge(1 to 3)
        doubleEdge(2 to 3)
        doubleEdge(2 to 4)
        doubleEdge(3 to 5)
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
        assertEquals("012345", builder.toString())
    }

    @Test
    fun randomTest2() {
        val bfsTree = graph<Int> {
            node(3)
            node(1)
            node(2)
            node(5)
            node(0)
            node(4)
            edge(3 to 1)
            edge(3 to 2)
            edge(3 to 5)
            edge(1 to 0)
            edge(2 to 4)
        }
        assertEquals(bfsTree, basicGraph.bfsTreeFrom(3.node))
    }

    @Test
    fun randomTest3() {

        val bfsTree = graph<Int> {
            node(4)
            node(2)
            node(0)
            node(3)
            node(1)
            node(5)
            edge(4 to 2)
            edge(2 to 0)
            edge(2 to 3)
            edge(3 to 5)
            edge(3 to 1)
        }
        assertEquals(bfsTree, basicGraph.bfsTreeFrom(4.node))
    }

    @Test
    fun randomTest4() {
        val bfs = bfs(fourComponentGraph, 1.node, null)


        val bfsTree = graph<Int> {
            node(1)
            node(2)
            node(3)
            edge(1 to 2)
            edge(1 to 3)
        }
        assertEquals(bfsTree, fourComponentGraph.bfsTreeFrom(1.node))
        assertNull(bfs.second[5.node])
        assertNull(bfs.second[4.node])
        assertEquals(1, bfs.second[2.node])
        assertEquals(1, bfs.second[3.node])
    }

    @Test
    fun randomTest5() {
        assert(!basicGraph.bfsTreeFrom(0.node).hasDirectedCycle)
        assert(!fourComponentGraph.bfsTreeFrom(1.node).hasDirectedCycle)
    }

}
