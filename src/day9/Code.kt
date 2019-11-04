package day9

import java.io.File

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed)}")
    println("Part 2 = ${part2(parsed)}")
}

data class Distance(val city1: String, val city2: String, val dist: Int)

private val lineStructure = """(\w+) to (\w+) = (\d+)""".toRegex()

fun parse(input: List<String>) = input.map {
    lineStructure.matchEntire(it)?.destructured?.let {
        val (city1, city2, dist) = it.toList()
        Distance(city1, city2, dist.toInt())
    }
}.requireNoNulls().let { distances ->
    MultiMap<String, String, Int>().apply {
        distances.forEach {
            this[it.city1][it.city2] = it.dist
            this[it.city2][it.city1] = it.dist
        }
    }
}

fun part1(input: MultiMap<String, String, Int>): Any? = input.keys.permutations.map {
    it.windowed(size = 2).sumBy { (from, to) -> input[from][to] }
}.min()

fun part2(input: MultiMap<String, String, Int>): Any? = input.keys.permutations.map {
    it.windowed(size = 2).sumBy { (from, to) -> input[from][to] }
}.max()

private val <E> Set<E>.permutations: List<List<E>>
    get() = if (size == 1) listOf(listOf(first())) else flatMap { element ->
        minus(element).permutations.map { it + element }
    }

class MultiMap<K1, K2, V> : HashMap<K1, MultiMap.ValueMap<K2, V>>(), MutableMap<K1, MultiMap.ValueMap<K2, V>> {
    class ValueMap<K2, V> : HashMap<K2, V>(), MutableMap<K2, V> {
        override fun get(key: K2) = super.get(key) ?: throw IllegalAccessError()
    }

    override fun get(key: K1) = super.get(key) ?: ValueMap<K2, V>().also { put(key, it) }
}