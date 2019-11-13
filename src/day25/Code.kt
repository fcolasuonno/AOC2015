package day25

import java.io.File

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed)}")
}

data class CodeLocation(val row: Int, val col: Int) {
    fun diagonal() = (col + row) * (col + row - 1) / 2 - row + 1

}

private val lineStructure =
    """To continue, please consult the code grid in the manual\.  Enter the code at row (\d+), column (\d+)\.""".toRegex()

fun parse(input: List<String>) = input.single().let {
    lineStructure.matchEntire(it)?.destructured?.let {
        val (row, col) = it.toList()
        CodeLocation(row.toInt(), col.toInt())
    }
}!!

fun part1(input: CodeLocation): Any? = generateSequence(20151125L) { prev ->
    (prev * 252533L) % 33554393L
}.take(input.diagonal()).last()
