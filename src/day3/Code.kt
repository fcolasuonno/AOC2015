package day3

import java.io.File

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed)}")
    println("Part 2 = ${part2(parsed)}")
}

fun parse(input: List<String>) = input.requireNoNulls()

data class Location(val x: Int, val y: Int)

fun part1(input: List<String>): Any? =
    input.map {
        var current = Location(0, 0)
        val houses = mutableSetOf(current)
        it.forEach {
            current = move(current, it, houses)
        }
        houses.count()
    }

private fun move(current: Location, where: Char, houses: MutableSet<Location>) = when (where) {
    '^' -> current.copy(y = current.y + 1)
    'v' -> current.copy(y = current.y - 1)
    '>' -> current.copy(x = current.x + 1)
    '<' -> current.copy(x = current.x - 1)
    else -> throw IllegalStateException()
}.also {
    houses.add(it)
}

fun part2(input: List<String>): Any? = input.map {
    var current1 = Location(0, 0)
    var current2 = Location(0, 0)
    val houses = mutableSetOf(current2)
    it.toList().chunked(2).forEach {
        current1 = move(current1, it[0], houses)
        if (it.size > 1) {
            current2 = move(current2, it[1], houses)
        }
    }
    houses.count()
}
