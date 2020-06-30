import graphs.Node
import graphs.WeightedGraph
import graphs.utils.*

val test1 = weightedGraph<Char, Int> {
    for (c in 'A'..'E') {
        node(c)
    }
    doubleEdge('A' to 'B', 6)
    doubleEdge('A' to 'D', 1)
    doubleEdge('B' to 'D', 2)
    doubleEdge('B' to 'E', 2)
    doubleEdge('D' to 'E', 1)
    doubleEdge('B' to 'C', 5)
    doubleEdge('E' to 'C', 5)
}

val test2 = weightedGraph<Char, Int> {
    for (c in 'a'..'h') {
        node(c)
    }
    doubleEdge('a' to 'b', 8)
    doubleEdge('a' to 'c', 2)
    doubleEdge('a' to 'd', 5)
    doubleEdge('b' to 'f', 13)
    doubleEdge('b' to 'd', 2)
    doubleEdge('c' to 'd', 2)
    doubleEdge('c' to 'e', 5)
    doubleEdge('d' to 'e', 1)
    doubleEdge('d' to 'f', 6)
    doubleEdge('d' to 'g', 3)
    doubleEdge('e' to 'g', 1)
    doubleEdge('g' to 'f', 2)
    doubleEdge('g' to 'h', 6)
    doubleEdge('f' to 'h', 3)
}

val test3 = weightedGraph<Char, Int> {
    for (c in 'a'..'k') {
        node(c)
    }
    //10
    doubleEdge('a' to 'b', 10)

    //9
    doubleEdge('a' to 'f', 5)
    doubleEdge('f' to 'b', 4)

    //8
    doubleEdge('a' to 'd', 3)
    doubleEdge('d' to 'f', 1)
    //edge('f' to 'b', 4)

    //7
    doubleEdge('a' to 'g', 1)
    doubleEdge('g' to 'h', 1)
    doubleEdge('h' to 'j', 1)
    doubleEdge('j' to 'b', 4)

    //6
    doubleEdge('j' to 'k', 1)
    doubleEdge('k' to 'b', 2)

    doubleEdge('k' to 'c', 10)
    doubleEdge('g' to 'k', 8)
    doubleEdge('f' to 'g', 11)
    doubleEdge('e' to 'c', 9)
    doubleEdge('a' to 'i', 15)
}

val test4 = weightedGraph<Int, Int> {
    for (i in 1..12) {
        node(i)
    }
    doubleEdge(1 to 12, 9)

    doubleEdge(1 to 2, 1)
    doubleEdge(2 to 12, 7)

    doubleEdge(1 to 3, 1)
    doubleEdge(3 to 4, 1)
    doubleEdge(4 to 12, 5)

    doubleEdge(1 to 5, 1)
    doubleEdge(5 to 6, 1)
    doubleEdge(6 to 7, 1)
    doubleEdge(7 to 12, 3)

    doubleEdge(1 to 8, 1)
    doubleEdge(8 to 9, 1)
    doubleEdge(9 to 10, 1)
    doubleEdge(10 to 11, 1)
    doubleEdge(11 to 12, 1)
}


val test5 = weightedGraph<Int, Int> {
    for (i in 1..7) {
        node(i)
    }
    doubleEdge(1 to 2, 10)
    doubleEdge(2 to 3, 1)

    doubleEdge(1 to 4, 1)
    doubleEdge(4 to 5, 1)
    doubleEdge(5 to 6, 1)
    doubleEdge(6 to 7, 1)
    doubleEdge(7 to 2, 1)
    doubleEdge(7 to 3, 6)
}

val grid100x100: WeightedGraph<Pair<Int, Int>, Double> = weightedGraph {
    for (i in 0 until 100) {
        for (j in 0 until 100) {
            node(i to j)
        }
    }

    for (i in 0 until 100) {
        for (j in 0 until 100) {
            val weight = if (i % 2 == 0) 0.3 else 0.2
            if (i + 1 < 100) edge((i to j) to (i + 1 to j), weight)
            if (i - 1 >= 0) edge((i to j) to (i - 1 to j), weight)
            if (j + 1 < 100) edge((i to j) to (i to j + 1), weight)
            if (j - 1 >= 0) edge((i to j) to (i to j - 1), weight)
        }
    }
}

private fun wall(i: Int, range: IntRange): Set<Node<Pair<Int, Int>>> {
    val wall = mutableSetOf<Node<Pair<Int, Int>>>()

    for (j in range) {
        wall.add((i to j).node)
    }

    return wall
}

@Suppress("UNCHECKED_CAST")
val walledGrid100x100: WeightedGraph<Pair<Int, Int>, Double> = weightedGraph<Pair<Int, Int>, Double> {
    for (i in 0 until 100) {
        for (j in 0 until 100) {
            node(i to j)
        }
    }

    for (i in 0 until 100) {
        for (j in 0 until 100) {
            val weight = if (i % 2 == 0) 0.3 else 0.2
            if (i + 1 < 100) edge((i to j) to (i + 1 to j), weight)
            if (i - 1 >= 0) edge((i to j) to (i - 1 to j), weight)
            if (j + 1 < 100) edge((i to j) to (i to j + 1), weight)
            if (j - 1 >= 0) edge((i to j) to (i to j - 1), weight)
        }
    }
} - wall(50, 30..70)

val gridForFloyd20x20: WeightedGraph<Pair<Int, Int>, Double> = weightedGraph {
    for (i in 0 until 20) {
        for (j in 0 until 20) {
            node(i to j)
        }
    }

    for (i in 0 until 20) {
        for (j in 0 until 20) {
            if (i + 1 < 20) edge((i to j) to (i + 1 to j), 1.0)
            if (i - 1 >= 0) edge((i to j) to (i - 1 to j), 1.0)
            if (j + 1 < 20) edge((i to j) to (i to j + 1), 1.0)
            if (j - 1 >= 0) edge((i to j) to (i to j - 1), 1.0)
        }
    }
}

