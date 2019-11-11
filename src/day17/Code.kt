package day17

import java.io.File

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed, 150)}")
    println("Part 2 = ${part2(parsed, 150)}")
}

data class SomeObject(val i1: String)

fun parse(input: List<String>) = input.map {
    it.toInt()
}.requireNoNulls()

fun part1(input: List<Int>, total: Int): Any? = (0 until 1.shl(input.size)).map { num ->
    input.withIndex().filter { (num.shr(it.index).and(1) != 0) }.map { it.value }
}.count { it.sum() == total }

fun part2(input: List<Int>, total: Int): Any? = (0 until 1.shl(input.size)).map { num ->
    input.withIndex().filter { (num.shr(it.index).and(1) != 0) }.map { it.value }
}.filter { it.sum() == total }.groupBy { it.size }.minBy { it.key }?.value?.size