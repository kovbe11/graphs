import graphs.algorithms.shortestpath.allpairs.floydWarshallMappingInt
import graphs.utils.node
import org.junit.Assert.assertEquals
import org.junit.Test

class FloydWarshallTests {


    @Test
    fun test1() {
        assertEquals(6, test5.floydWarshallMappingInt()[1.node to 3.node])
    }

}