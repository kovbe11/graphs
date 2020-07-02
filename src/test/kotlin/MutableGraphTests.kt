import graphs.SimpleMutableGraph
import graphs.utils.*
import org.junit.Assert.assertEquals
import org.junit.Test

class MutableGraphTests {

    @Test
    fun addEdgeWorksWhenNodesExist() {
        val graph = SimpleMutableGraph<Int>()
        graph.addNode(3.mutableNode)
        graph.addNode(4.mutableNode)
        graph.addEdge((3 to 4).mutableEdge)
        assertEquals(1, graph[3.mutableNode]!!.size)
    }

    @Test(expected = IllegalStateException::class)
    fun addEdgeOnlyWorksWhenNodeExists() {
        val graph = SimpleMutableGraph<Int>()
        graph.addEdge((1 to 2).mutableEdge)
    }

    @Test
    fun removeNodeRemovesRelatedEdgesToo() {
        val graph = mutableGraph<Int> {
            for (i in 1..4) {
                node(i)
            }
            doubleEdge(1 to 2)
            doubleEdge(1 to 3)
            doubleEdge(2 to 4)
            doubleEdge(3 to 4)
        }

        graph.removeNode(1.mutableNode)
        assertEquals(1, graph[2.mutableNode]!!.size)
        assertEquals(1, graph[3.mutableNode]!!.size)

        val graph1 = mutableGraph<Int> {
            for (i in 2..4) {
                node(i)
            }
            doubleEdge(2 to 4)
            doubleEdge(3 to 4)
        }
        assertEquals(graph1, graph)

        graph.removeNode(4.mutableNode)
        assertEquals(0, graph[2.mutableNode]!!.size)
        assertEquals(0, graph[3.mutableNode]!!.size)

        val graph2 = mutableGraph<Int> {
            node(2)
            node(3)
        }
        assertEquals(graph2, graph)
    }

}