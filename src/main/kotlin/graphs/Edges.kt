package graphs

//ugyanaz a kérdés: öröklés/interface/hogy?


open class MutableEdge<T>(var start: MutableNode<T>, var end: MutableNode<T>) {
    open fun makeImmutable(): Edge<T> {
        return this.immutable
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MutableEdge<*>

        if (start != other.start) return false
        if (end != other.end) return false

        return true
    }

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + end.hashCode()
        return result
    }

    override fun toString(): String {
        return "MutableEdge($start to $end)"
    }
}

open class Edge<T>(val start: Node<T>, val end: Node<T>) {
    constructor(mutableEdge: MutableEdge<T>) : this(mutableEdge.start.immutable, mutableEdge.end.immutable)

    open fun makeMutable(): MutableEdge<T> {
        return MutableEdge(start.mutable, end.mutable)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Edge<*>

        if (start != other.start) return false
        if (end != other.end) return false

        return true
    }

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + end.hashCode()
        return result
    }

    override fun toString(): String {
        return "Edge($start to $end)"
    }


}

class MutableWeightedEdge<T, N : Number>(start: MutableNode<T>, end: MutableNode<T>, var weight: N) :
    MutableEdge<T>(start, end) {
    override fun makeImmutable(): Edge<T> {
        return this.immutable
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as MutableWeightedEdge<*, *>

        if (start != other.start) return false
        if (end != other.end) return false
        if (weight != other.weight) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + weight.hashCode()
        return result
    }

    override fun toString(): String {
        return "MutableWeightedEdge($start to $end weight = $weight)"
    }
}

class WeightedEdge<T, N : Number>(start: Node<T>, end: Node<T>, val weight: N) : Edge<T>(start, end) {
    constructor(mutableWeightedEdge: MutableWeightedEdge<T, N>)
            : this(mutableWeightedEdge.start.immutable, mutableWeightedEdge.end.immutable, mutableWeightedEdge.weight)

    override fun makeMutable(): MutableEdge<T> {
        return MutableWeightedEdge(start.mutable, end.mutable, weight)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as WeightedEdge<*, *>

        if (start != other.start) return false
        if (end != other.end) return false
        if (weight != other.weight) return false


        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + weight.hashCode()
        return result
    }

    override fun toString(): String {
        return "WeightedEdge($start to $end weight = $weight)"
    }
}
