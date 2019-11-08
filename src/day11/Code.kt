package day11

import java.io.File

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed)}")
    println("Part 2 = ${part2(parsed)}")
}

fun parse(input: List<String>) = input.map { initial ->
    generateSequence(initial.toMutableList()) { pass ->
        pass.inc()
    }.filter { possiblePass ->
        possiblePass.windowed(3).any { (a, b, c) -> c == (b + 1) && b == (a + 1) }
                && possiblePass.windowed(2).filter { (c1, c2) -> c1 == c2 }.groupBy { it.first() }.size >= 2
    }
}

fun part1(input: List<Sequence<List<Char>>>): Any? = input.map {
    it.first().joinToString(separator = "")
}

fun part2(input: List<Sequence<List<Char>>>): Any? = input.map {
    it.drop(1).first().joinToString(separator = "")
}

private fun MutableList<Char>.inc() = this.apply {
    for (index in indices.reversed()) {
        if (this[index] == 'z') {
            this[index] = 'a'
        } else {
            this[index]++
            break
        }
    }
    var clean = false
    for (index in indices) {
        if (clean) this[index] = 'a'
        when (this[index]) {
            'i', 'o', 'l' -> {
                this[index]++; clean = true
            }
        }
    }
}