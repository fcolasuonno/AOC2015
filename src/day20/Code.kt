package day20

import java.io.File

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed)}")
    println("Part 2 = ${part2(parsed)}")
}

fun parse(input: List<String>) = input.single().toInt()

fun part1(input: Int): Any? = IntArray(2_000_000) { 10 }.apply {
    (2 until size).forEach { elf ->
        (1 until size / elf).forEach {
            this[it * elf] += (elf * 10)
        }
    }
}.indexOfFirst { it >= input }

fun part2(input: Int): Any? = IntArray(2_000_000) { 0 }.apply {
    (1 until size).forEach { elf ->
        (1..50).forEach {
            val house = it * elf
            if (house < size) {
                this[house] += (elf * 11)
            }
        }
    }
}.indexOfFirst { it >= input }
