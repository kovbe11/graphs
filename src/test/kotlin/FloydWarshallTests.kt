import graphs.algorithms.shortestpath.DoubleAdapter
import graphs.algorithms.shortestpath.IntAdapter
import graphs.algorithms.shortestpath.allpairs.floydWarshallMapping
import graphs.utils.node
import org.junit.Assert.assertEquals
import org.junit.Test

class FloydWarshallTests {


    @Test
    fun test1() {
        assertEquals(6, test5.floydWarshallMapping(IntAdapter)[1.node to 3.node])
    }

    @Test
    fun test2() {
        //TODO
        println(gridForFloyd20x20.floydWarshallMapping(DoubleAdapter)[(0 to 0).node to (19 to 19).node])
    }

}