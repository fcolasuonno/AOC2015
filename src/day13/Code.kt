package day13

import MultiMap
import permutations
import java.io.File

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed)}")
    println("Part 2 = ${part2(parsed)}")
}

data class Score(val source: String, val dest: String, val score: Int)

private val lineStructure = """(\w+) would (\w+) (\d+) happiness units by sitting next to (\w+)\.""".toRegex()

fun parse(input: List<String>) = input.map {
    lineStructure.matchEntire(it)?.destructured?.let {
        val (who, sign, points, whom) = it.toList()
        Score(who, whom, (if (sign == "gain") 1 else -1) * points.toInt())
    }
}.requireNoNulls().let { scores ->
    MultiMap<String, String, Int>().apply {
        scores.forEach {
            this[it.source][it.dest] = it.score
        }
    }
}

fun part1(input: MultiMap<String, String, Int>): Any? = input.keys.permutations.map {
    it.windowed(2).sumBy { (source, dest) -> input[source][dest] + input[dest][source] } + input[it.last()][it.first()] + input[it.first()][it.last()]
}.max()

fun part2(input: MultiMap<String, String, Int>): Any? = input.apply {
    keys.toList().forEach { key ->
        this[key]["me"] = 0
        this["me"][key] = 0
    }
}.let { part1(it) }
