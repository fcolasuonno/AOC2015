package day1

import java.io.File

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed)}")
    println("Part 2 = ${part2(parsed)}")
}

fun parse(input: List<String>) = input.first()

fun part1(input: String): Any? = input.count { it == '(' } - input.count { it == ')' }

fun part2(input: String): Any? {
    var floor = 0
    var index = 0
    while (floor >= 0) {
        floor += if (input[index++] == '(') 1 else -1
    }
    return index
}
