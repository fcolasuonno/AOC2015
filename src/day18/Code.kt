package day18

import java.io.File

private val Coord.neighbours: List<Coord>
    get() = listOf(
        (first - 1) to (second - 1),
        (first - 1) to (second),
        (first - 1) to (second + 1),
        (first) to (second - 1),
        (first) to (second + 1),
        (first + 1) to (second - 1),
        (first + 1) to (second),
        (first + 1) to (second + 1)
    )

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed, 100)}")
    println("Part 2 = ${part2(parsed, 100)}")
}

typealias Coord = Pair<Int, Int>

fun parse(input: List<String>) = input.mapIndexed { y, s ->
    s.withIndex().filter { it.value == '#' }.map { Coord(it.index, y) }
}.requireNoNulls().flatten().toSet() to input.size

fun part1(input: Pair<Set<Coord>, Int>, steps: Int): Any? =
    generateSequence(input.first) { current: Set<Coord> ->
        val keepOn = current.filter {
            val n = it.neighbours
            val on = current.count { it in n }
            on == 2 || on == 3
        }
        val turnOn = current.map { it.neighbours }.flatten().distinct()
            .filter { it.first in 0 until input.second && it.second in 0 until input.second && it.neighbours.count { it in current } == 3 }
        (keepOn + turnOn).toSet()
    }.drop(steps).first().size

fun part2(input: Pair<Set<Coord>, Int>, steps: Int): Any? = generateSequence(input.first) { current: Set<Coord> ->
    val keepOn = current.filter {
        val n = it.neighbours
        val on = current.count { it in n }
        on == 2 || on == 3
    }
    val turnOn = current.map { it.neighbours }.flatten().distinct()
        .filter { it.first in 0 until input.second && it.second in 0 until input.second && it.neighbours.count { it in current } == 3 }
    val next = keepOn + turnOn + listOf(
        Coord(0, 0),
        Coord(0, input.second - 1),
        Coord(input.second - 1, 0),
        Coord(input.second - 1, input.second - 1)
    )
    next.toSet()
}.drop(steps).first().size
