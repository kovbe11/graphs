import graphs.*


fun main() {

    val graph1 = graph<Int> {
        node(3)
        node(2)
        edge(2 to 3)
        node(11)
        node(10)
        node(1)
        node(13)
        node(5)
        node(6)
        node(15)
        node(20)
        node(30)
        node(40)
        edge(13 to 15)
        edge(15 to 20)
        edge(20 to 30)
        edge(30 to 13)
        edge(30 to 40)
        edge(11 to 13)
        edge(13 to 3)
        doubleEdge(1 to 2)
        edge(1 to 11)
        edge(5 to 11)
        doubleEdge(11 to 2)
        edge(3 to 2)
        doubleEdge(10 to 11)
        edge(6 to 3)
        edge(2 to 5)
        edge(5 to 6)
        edge(3 to 2)
    }

    graph1.traverseBFSIndexed(2.node) { node, i ->
        println("$node is $i away from starting node")
    }

}



