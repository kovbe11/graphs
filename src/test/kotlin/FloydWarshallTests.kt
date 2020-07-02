import graphs.algorithms.shortestpath.DoubleAdapter
import graphs.algorithms.shortestpath.IntAdapter
import graphs.algorithms.shortestpath.allpairs.floydWarshallMapping
import graphs.utils.node
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.abs

class FloydWarshallTests {


    @Test
    fun test1() {
        assertEquals(6, test5.floydWarshallMapping(IntAdapter)[1.node to 3.node])
    }

    @Test
    fun test2() {
        val distances = gridForFloyd20x20.floydWarshallMapping(DoubleAdapter)

        for (i in 0 until 20)
            for (j in 0 until 20)
                for (k in 0 until 20)
                    for (l in 0 until 20) {
                        val expect = abs((i - k).toDouble()) + abs((j - l).toDouble())
                        val actual = distances[(k to l).node to (i to j).node]
//                            println("i:$i j:$j k:$k l:$l expect: $expect, actual: $actual")
                        assertEquals(expect, actual) //on a grid with 1 weights it's easy to calculate min distance
                    }

    }

}