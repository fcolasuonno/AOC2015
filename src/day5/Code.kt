package day5

import java.io.File

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    println("Part 1 = ${part1(input)}")
    println("Part 2 = ${part2(input)}")
}

fun part1(input: List<String>) =
    input.count {
        it.count { it == 'a' || it == 'e' || it == 'i' || it == 'o' || it == 'u' } >= 3
                && it.zipWithNext().any { it.first == it.second }
                && !it.contains("ab")
                && !it.contains("cd")
                && !it.contains("pq")
                && !it.contains("xy")
    }

fun part2(input: List<String>) = input.count {
    it.contains("""(..).*\1""".toRegex()) &&
            it.contains("""(.).\1""".toRegex())
}
