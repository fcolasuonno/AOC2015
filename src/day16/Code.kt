package day16

import java.io.File

fun main(args: Array<String>) {
    val name = if (true) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed)}")
    println("Part 2 = ${part2(parsed)}")
}

data class Sue(val name: String, val attribs: Map<String, Int>)

private val lineStructure = """Sue (\d+): (\w+): (\d+), (\w+): (\d+), (\w+): (\d+)""".toRegex()

val detect = mapOf(
    "children" to 3,
    "cats" to 7,
    "samoyeds" to 2,
    "pomeranians" to 3,
    "akitas" to 0,
    "vizslas" to 0,
    "goldfish" to 5,
    "trees" to 3,
    "cars" to 2,
    "perfumes" to 1
)

fun parse(input: List<String>) = input.map {
    lineStructure.matchEntire(it)?.destructured?.let {
        val sue = it.toList()
        Sue(sue.first(), sue.drop(1).chunked(2).map { (attr, number) -> attr to number.toInt() }.toMap())
    }
}.requireNoNulls()

fun part1(input: List<Sue>): Any? = input.single { sue ->
    detect.filterKeys { it in sue.attribs.keys } == sue.attribs
}.name

fun part2(input: List<Sue>): Any? {
    val exclusions = listOf("cats", "trees", "pomeranians", "goldfish")
    val exact = detect - exclusions
    return input.single { sue ->
        exact.filterKeys { it in sue.attribs.keys } == sue.attribs - exclusions &&
                (sue.attribs["cats"] ?: Int.MAX_VALUE) > detect.getValue("cats") &&
                (sue.attribs["trees"] ?: Int.MAX_VALUE) > detect.getValue("trees") &&
                (sue.attribs["pomeranians"] ?: 0) < detect.getValue("pomeranians") &&
                (sue.attribs["goldfish"] ?: 0) < detect.getValue("goldfish")
    }.name
}
