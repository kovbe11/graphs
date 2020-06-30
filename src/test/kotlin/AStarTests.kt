import graphs.Node
import graphs.algorithms.shortestpath.DoubleAdapter
import graphs.algorithms.shortestpath.IntAdapter
import graphs.algorithms.shortestpath.tree.aStar
import graphs.algorithms.shortestpath.tree.aStarShortestPathLength
import graphs.utils.node
import org.junit.Assert.assertEquals
import org.junit.Test

class AStarTests {
    @Test
    fun test1() {
        val heuristicFn: (Node<Int>) -> Double = { 0.0 }
        assertEquals(
            6,
            aStarShortestPathLength(
                test5,
                1.node,
                3.node,
                heuristicFn,
                IntAdapter
            )
        )
    }

    @Test
    fun test2() {
        val finish = (99 to 99).node

        //this 100% depends on the heuristic though.

        val heuristicFn: (Node<Pair<Int, Int>>) -> Double = {
            val a = finish.value
            val b = it.value
            kotlin.math.sqrt(((a.first - b.first) * (a.first - b.first) + (a.second - b.second) * (a.second - b.second)).toDouble())
        }
        val checked = aStar(
            grid100x100,
            (0 to 0).node,
            finish,
            heuristicFn,
            DoubleAdapter
        ).size
        assert(checked < 1000)

        val checked2 = aStar(
            walledGrid100x100,
            (0 to 50).node,
            (99 to 50).node,
            heuristicFn,
            DoubleAdapter
        ).size
        assert(checked2 < 5000)

    }

}