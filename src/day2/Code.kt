package day2

import java.io.File

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed)}")
    println("Part 2 = ${part2(parsed)}")
}

data class Box(val l: Int, val w: Int, val h: Int) {
    private val area = 2 * l * w + 2 * w * h + 2 * h * l
    private val volume = h * l * w
    private val smallestDim = listOf(l, w, h).sorted().take(2)
    private val slack = smallestDim.reduce { a, b -> a * b }
    val wrappingArea = area + slack
    val ribbonLength = volume + smallestDim.sum() * 2
}

private val lineStructure = """(\d+)x(\d+)x(\d+)""".toRegex()

fun parse(input: List<String>) = input.map {
    lineStructure.matchEntire(it)?.destructured?.let {
        val (l, w, h) = it.toList().map { it.toInt() }
        Box(l, w, h)
    }
}.requireNoNulls()

fun part1(input: List<Box>): Any? = input.sumBy { it.wrappingArea }

fun part2(input: List<Box>): Any? = input.sumBy { it.ribbonLength }
