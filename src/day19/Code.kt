package day19

import java.io.File

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val (parsed, test) = parse(input)
    println("Part 1 = ${part1(parsed, test)}")
    println("Part 2 = ${part2(parsed, test)}")
}

private val lineStructure = """(\w+) => (\w+)""".toRegex()

fun parse(input: List<String>) = input.map {
    lineStructure.matchEntire(it)?.destructured?.let {
        val (find, replace) = it.toList()
        (find to replace)
    }
}.filterNotNull() to input.last()

fun part1(input: List<Pair<String, String>>, test: String): Any? =
    input.map { it.first.toRegex() to it.second }.flatMap { (find, replace) ->
        find.findAll(test).toList().map {
            test.replaceRange(it.range, replace)
        }
    }.distinct().count()

fun part2(input: List<Pair<String, String>>, test: String): Any? = input.map { it.second to it.first }.let { patterns ->
    generateSequence { patterns.shuffled() }.map { p ->
        generateSequence(test) { reduction ->
            p.firstOrNull { it.first in reduction }?.let { (find, replace) ->
                reduction.replaceFirst(find, replace)
            }
        }.indexOfFirst { it == "e" }
    }.first { it > 0 }
}