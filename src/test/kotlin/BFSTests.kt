import graphs.*
import graphs.algorithms.BFS
import graphs.algorithms.traverseBFS
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
        val bfs = BFS(basicGraph, 3.node)

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
        assertEquals(bfsTree, bfs.bfsTree)
    }

    @Test
    fun randomTest3() {
        val bfs = BFS(basicGraph, 4.node)

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
            edge(0 to 1)
            edge(3 to 5)
        }
        assertEquals(bfsTree, bfs.bfsTree)
    }

    @Test
    fun randomTest4() {
        val bfs = BFS(fourComponentGraph, 1.node)

        val bfsTree = graph<Int> {
            node(1)
            node(2)
            node(3)
            edge(1 to 2)
            edge(1 to 3)
        }
        assertEquals(bfsTree, bfs.bfsTree)
        assertNull(bfs.visitLevel(5.node))
        assertNull(bfs.visitLevel(4.node))
        assertEquals(1, bfs.visitLevel(2.node))
        assertEquals(1, bfs.visitLevel(3.node))
    }

    @Test
    fun randomTest5() {
        val bfs1 = BFS(basicGraph, 0.node)
        assert(!bfs1.bfsTree.hasCycle)
        val bfs2 = BFS(fourComponentGraph, 1.node)
        assert(!bfs2.bfsTree.hasCycle)
    }

}
