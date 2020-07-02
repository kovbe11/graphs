import graphs.algorithms.traversal.*
import graphs.utils.*
import org.junit.Assert.assertEquals
import org.junit.Test

class DFSTests {


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

    @Test
    fun depthTest1() {
        val depthNums = basicGraph.dfsDepthNumMappingFrom('s'.node)
        assertEquals(depthNums['s'.node], 0)
        assertEquals(depthNums['c'.node], 1)
        assertEquals(depthNums['d'.node], 2)
        assertEquals(depthNums['e'.node], 3)
        assertEquals(depthNums['f'.node], 4)
        assertEquals(depthNums['b'.node], 5)
        assertEquals(depthNums['a'.node], 6)
        assertEquals(depthNums['g'.node], 7)
    }

    @Test
    fun finishTest1() {
        val finishNums = basicGraph.dfsFinishNumMappingFrom('s'.node)
        assertEquals(finishNums['f'.node], 0)
        assertEquals(finishNums['b'.node], 1)
        assertEquals(finishNums['e'.node], 2)
        assertEquals(finishNums['d'.node], 3)
        assertEquals(finishNums['a'.node], 4)
        assertEquals(finishNums['c'.node], 5)
        assertEquals(finishNums['g'.node], 6)
        assertEquals(finishNums['s'.node], 7)
    }

    @Test
    fun depthTest2() {
        val depthNums1 = threeComponentGraph.dfsDepthNumMappingFrom(1.node)
        val depthNums2 = threeComponentGraph.dfsDepthNumMappingFrom(5.node)
        assertEquals(1, depthNums1[3.node])
        assertEquals(2, depthNums1.size)
        val expected1 = graph<Int> {
            node(1)
            node(3)
            edge(1 to 3)
        }
        assertEquals(expected1, threeComponentGraph.dfsTreeFrom(1.node))
        assertEquals(3, depthNums2.size)
        assertEquals(1, depthNums2[10.node])
        assertEquals(2, depthNums2[2.node])
        val expected2 = graph<Int> {
            node(10)
            node(5)
            node(2)
            edge(5 to 10)
            edge(5 to 2)
        }
        assertEquals(expected2, threeComponentGraph.dfsTreeFrom(5.node))
    }

    @Test
    fun finishTest2() {
        val finishNums1 = threeComponentGraph.dfsFinishNumMappingFrom(1.node)
        val finishNums2 = threeComponentGraph.dfsFinishNumMappingFrom(5.node)
        assertEquals(0, finishNums1[3.node])
        assertEquals(2, finishNums1.size)
        assertEquals(3, finishNums2.size)
        assertEquals(0, finishNums2[10.node])
        assertEquals(1, finishNums2[2.node])
    }


    @Test
    fun cycleDetection1() {
        assert(!dfsDirectedDetectCycleFrom(basicGraph, 's'.node))
        assert(dfsDirectedDetectCycleFrom(threeComponentGraph, 1.node))
        assert(dfsDirectedDetectCycleFrom(threeComponentGraph, 5.node))
        assert(dfsDirectedDetectCycleFrom(cyclicGraph, 1.node))
        assert(!dfsDirectedDetectCycleFrom(acyclicGraph, 1.node))
        assert(!dfsDirectedDetectCycleFrom(cyclicGraph, 4.node)) //can't see from node 4
    }

    @Test
    fun cycleDetection2() {
        assert(cyclicGraph.hasDirectedCycle)
        assert(!acyclicGraph.hasDirectedCycle)
        assert(!basicGraph.hasDirectedCycle)
    }

    @Test
    fun dfsTree() {
        assert(!basicGraph.dfsTreeFrom('s'.node).hasDirectedCycle)
        assert(!threeComponentGraph.dfsTreeFrom(1.node).hasDirectedCycle)
        assert(!threeComponentGraph.dfsTreeFrom(5.node).hasDirectedCycle)
        assert(!cyclicGraph.dfsTreeFrom(1.node).hasDirectedCycle)
        assert(!acyclicGraph.dfsTreeFrom(1.node).hasDirectedCycle)
        assert(!cyclicGraph.dfsTreeFrom(4.node).hasDirectedCycle)
    }

    @Test
    fun directedUndirectedCycles() {
        val undirected1 = graph<Int> {
            node(1)
            node(2)
            doubleEdge(1 to 2)
        }

        val undirected2 = graph<Int> {
            node(1)
            node(2)
            node(3)
            doubleEdge(1 to 2)
            doubleEdge(2 to 3)
            doubleEdge(3 to 1)
        }

        assert(dfsDirectedDetectCycleFrom(undirected1, 1.node))
        assert(!dfsUndirectedDetectCycleFrom(undirected1, 1.node))
        assert(dfsDirectedDetectCycleFrom(undirected2, 1.node))
        assert(dfsUndirectedDetectCycleFrom(undirected2, 1.node))
    }

}