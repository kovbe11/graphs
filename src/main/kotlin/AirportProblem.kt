import graphs.Edge
import graphs.Graph
import graphs.Node
import graphs.algorithms.kosaraju
import graphs.utils.edge
import graphs.utils.findInDegreesOfNodes
import graphs.utils.graph
import graphs.utils.node

//problem: https://youtu.be/qz9tKlF431k
//task is: we want to use the least amount of new edges
//to be able to reach all the nodes from the starting airport.

private val airports = setOf(
    "BGI", "CDG", "DEL", "DOH", "DSM", "EWR", "EYW",
    "HND", "ICN", "JFK", "LGA", "LHR", "ORD", "SAN",
    "SFO", "SIN", "TLV", "BUD"
)

private val routes = setOf(
    "DSM" to "ORD",
    "ORD" to "BGI",
    "BGI" to "LGA",
    "SIN" to "CDG",
    "CDG" to "SIN",
    "CDG" to "BUD",
    "DEL" to "DOH",
    "DEL" to "CDG",
    "TLV" to "DEL",
    "EWR" to "HND",
    "HND" to "ICN",
    "HND" to "JFK",
    "ICN" to "JFK",
    "JFK" to "LGA",
    "EYW" to "LHR",
    "LHR" to "SFO",
    "SFO" to "SAN",
    "SFO" to "DSM",
    "SAN" to "EYW"
)

private val baseAirport = "LGA"


fun <T> findMinimalNewEdgesToReachAllNodes(graph: Graph<T>, start: Node<T>): Set<Edge<T>> {
    val sccGraph: Graph<Set<T>> = kosaraju(graph)
    println(sccGraph)
    val zeroInDegNodes: Map<Node<Set<T>>, Int> =
        findInDegreesOfNodes(sccGraph).filter { it.value == 0 && it.key != start }

    val ret = mutableSetOf<Edge<T>>()
    for (scc in zeroInDegNodes) {
        ret.add((start.value to scc.key.value.first()).edge)
    }

    return ret
}

fun main() {
    val parsedGraph = graph<String> {
        for (airport in airports) {
            node(airport)
        }
        for (route in routes) {
            edge(route)
        }
    }

    println("expected answer: lga to tlv, lga to sfo or lga to san, lga to ewr")
    println(findMinimalNewEdgesToReachAllNodes(parsedGraph, baseAirport.node))
}