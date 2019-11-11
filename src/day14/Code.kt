package day14

import java.io.File
import java.lang.Integer.min

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed, 2503)}")
    println("Part 2 = ${part2(parsed, 2503)}")
}

data class Reindeer(val i1: String, val speed: Int, val fly: Int, val rest: Int) {
    var points = 0
    var distance = 0
    fun tick(elapsed: Int) {
        if (elapsed % (fly + rest) < fly) distance += speed
    }
}

private val lineStructure =
    """(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds\.""".toRegex()

fun parse(input: List<String>) = input.map {
    lineStructure.matchEntire(it)?.destructured?.let {
        val (name, speed, fly, rest) = it.toList()
        Reindeer(name, speed.toInt(), fly.toInt(), rest.toInt())
    }
}.requireNoNulls()

fun part1(input: List<Reindeer>, end: Int): Any? = input.map {
    val flights = end / (it.fly + it.rest)
    val leftTime = end % (it.fly + it.rest)
    it.speed * (flights * it.fly + min(it.fly, leftTime))
}.max()

fun part2(input: List<Reindeer>, end: Int): Any? {
    for (i in 0 until end) {
        input.forEach {
            it.tick(i)
        }
        input.filter { it.distance == input.map { it.distance }.max() }.forEach { it.points++ }
    }
    return input.map { it.points }.max()
}
