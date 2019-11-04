package day8

import java.io.File

private val String.encodedLength: Int
    get() = 2 + length + count { it == '\\' || it == '"' }

private val String.decodedCount: Int
    get() = indices.fold(0 to 0) { (count, skip), index ->
        when {
            skip != 0 -> count to skip - 1
            this[index] == '"' -> count to 0
            this.substring(index).startsWith("""\x""") -> count + 1 to 3
            this[index] == '\\' -> count + 1 to 1
            else -> count + 1 to 0
        }
    }.first


fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed)}")
    println("Part 2 = ${part2(parsed)}")
}


fun parse(input: List<String>) = input.requireNoNulls()

fun part1(input: List<String>): Any? = input.sumBy { it.length } - input.sumBy { it.decodedCount }

fun part2(input: List<String>): Any? = input.sumBy { it.encodedLength } - input.sumBy { it.length }
