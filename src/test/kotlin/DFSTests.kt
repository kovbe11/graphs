import graphs.algorithms.traversal.DFS
import graphs.utils.doubleEdge
import graphs.utils.edge
import graphs.utils.graph
import graphs.utils.node
import org.junit.Assert.assertEquals
import org.junit.Test

class DFSTests {

    //TODO: több teszt

    private val basicGraph = graph<Char>
    {
        node('a')
        node('b')
        node('c')
        node('d')
        node('e')
        node('f')
        node('g')
        node('s')
        edge('a' to 'd')
        edge('a' to 'e')
        edge('a' to 'b')
        edge('b' to 'f')
        edge('c' to 'a')
        edge('c' to 'd')
        edge('d' to 'e')
        edge('e' to 'f')
        edge('e' to 'b')
        edge('g' to 'd')
        edge('g' to 'c')
        edge('s' to 'g')
        edge('s' to 'c')
    }

    private val threeComponentGraph = graph<Int> {
        node(1)
        node(2)
        node(5)
        node(10)
        node(3)
        doubleEdge(1 to 3)
        doubleEdge(10 to 5)
        doubleEdge(5 to 2)
    }

    private val cyclicGraph = graph<Int> {
        node(1)
        node(2)
        node(3)
        node(4)
        node(5)

        edge(1 to 2)
        edge(2 to 3)
        edge(3 to 1)
        edge(4 to 5)
        edge(3 to 4)
    }

    private val acyclicGraph = graph<Int> {
        node(1)
        node(2)
        node(3)
        node(4)

        edge(1 to 2)
        edge(1 to 3)
        edge(2 to 4)
        edge(3 to 4)
    }

    private val dfs1 = DFS(basicGraph, 's'.node)
    private val dfs2 = DFS(threeComponentGraph, 1.node)
    private val dfs3 = DFS(threeComponentGraph, 5.node)
    private val dfs4 = DFS(cyclicGraph, 1.node)
    private val dfs5 = DFS(acyclicGraph, 1.node)
    private val dfs6 = DFS(cyclicGraph, 4.node)

    @Test
    fun depthTest1() {
        assert(dfs1.depthNum['s'.node] == 0)
        assert(dfs1.depthNum['c'.node] == 1)
        assert(dfs1.depthNum['d'.node] == 2)
        assert(dfs1.depthNum['e'.node] == 3)
        assert(dfs1.depthNum['f'.node] == 4)
        assert(dfs1.depthNum['b'.node] == 5)
        assert(dfs1.depthNum['a'.node] == 6)
        assert(dfs1.depthNum['g'.node] == 7)
    }

    @Test
    fun finishTest1() {
        assert(dfs1.finishNum['f'.node] == 0)
        assert(dfs1.finishNum['b'.node] == 1)
        assert(dfs1.finishNum['e'.node] == 2)
        assert(dfs1.finishNum['d'.node] == 3)
        assert(dfs1.finishNum['a'.node] == 4)
        assert(dfs1.finishNum['c'.node] == 5)
        assert(dfs1.finishNum['g'.node] == 6)
        assert(dfs1.finishNum['s'.node] == 7)
    }

    @Test
    fun depthTest2() {
        assert(dfs2.depthNum[3.node] == 1)
        assert(dfs2.depthNum.size == 2)
        val expected1 = graph<Int> {
            node(1)
            node(3)
            edge(1 to 3)
        }
        assertEquals(expected1, dfs2.dfsTree)
        assert(dfs3.depthNum.size == 3)
        assert(dfs3.depthNum[10.node] == 1)
        assert(dfs3.depthNum[2.node] == 2)
        val expected2 = graph<Int> {
            node(10)
            node(5)
            node(2)
            edge(5 to 10)
            edge(5 to 2)
        }
        assertEquals(expected2, dfs3.dfsTree)
    }

    @Test
    fun finishTest2() {
        assert(dfs2.finishNum[3.node] == 0)
        assert(dfs2.finishNum.size == 2)
        assert(dfs3.finishNum.size == 3)
        assert(dfs3.finishNum[10.node] == 0)
        assert(dfs3.finishNum[2.node] == 1)
    }

    @Test
    fun cycleDetection1() {
        assert(!dfs1.hasDirectedCycle)
        assert(dfs2.hasDirectedCycle)
        assert(dfs3.hasDirectedCycle)
        assert(dfs4.hasDirectedCycle)
        assert(!dfs5.hasDirectedCycle)
        assert(!dfs6.hasDirectedCycle) //mivel a 4 esből nem láthatjuk hogy van kör
    }

    @Test
    fun cycleDetection2() {
        assert(cyclicGraph.hasDirectedCycle)
        assert(!acyclicGraph.hasDirectedCycle)
        assert(!basicGraph.hasDirectedCycle)
    }

    @Test
    fun dfsTree() {
        assert(!dfs1.dfsTree.hasDirectedCycle)
        assert(!dfs2.dfsTree.hasDirectedCycle)
        assert(!dfs3.dfsTree.hasDirectedCycle)
        assert(!dfs4.dfsTree.hasDirectedCycle)
        assert(!dfs5.dfsTree.hasDirectedCycle)
        assert(!dfs6.dfsTree.hasDirectedCycle)
    }

    @Test
    fun directedUndirectedCycles() {
        val undirected1 = graph<Int> {
            node(1)
            node(2)
            doubleEdge(1 to 2)
        }
        val dfs7 = DFS(undirected1, 1.node)

        val undirected2 = graph<Int> {
            node(1)
            node(2)
            node(3)
            doubleEdge(1 to 2)
            doubleEdge(2 to 3)
            doubleEdge(3 to 1)
        }

        val dfs8 = DFS(undirected2, 1.node)

        assert(dfs7.hasDirectedCycle)
        assert(!dfs7.hasUndirectedCycle)
        assert(dfs8.hasDirectedCycle)
        assert(dfs8.hasUndirectedCycle)
    }

}