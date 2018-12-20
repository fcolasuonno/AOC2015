package day6

import java.io.File
import kotlin.math.max

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed)}")
    println("Part 2 = ${part2(parsed)}")
}

enum class Action {
    ON,
    TOGGLE,
    OFF
}

data class Instruction(val action: Action, val x1: Int, val y1: Int, val x2: Int, val y2: Int)

private val lineStructure = """(.*) (\d+),(\d+) through (\d+),(\d+)""".toRegex()

fun parse(input: List<String>) = input.map {
    lineStructure.matchEntire(it)?.destructured?.let {
        val (action, x1, y1, x2, y2) = it
        Instruction(
            when (action) {
                "turn on" -> Action.ON
                "toggle" -> Action.TOGGLE
                "turn off" -> Action.OFF
                else -> throw IllegalStateException()
            }, x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt()
        )
    }
}.requireNoNulls()

fun part1(input: List<Instruction>): Int {
    val grid = List(1000) {
        MutableList(1000) { false }
    }
    input.forEach {
        for (y in it.y1..it.y2) {
            for (x in it.x1..it.x2) {
                grid[x][y] = when (it.action) {
                    Action.ON -> true
                    Action.TOGGLE -> !grid[x][y]
                    Action.OFF -> false
                }
            }
        }
    }
    return grid.sumBy { it.count { it == true } }
}

fun part2(input: List<Instruction>): Int {
    val grid = List(1000) {
        MutableList(1000) { 0 }
    }
    input.forEach {
        for (y in it.y1..it.y2) {
            for (x in it.x1..it.x2) {
                val currentVal = grid[x][y]
                grid[x][y] = when (it.action) {
                    Action.ON -> currentVal + 1
                    Action.TOGGLE -> currentVal + 2
                    Action.OFF -> max(currentVal - 1, 0)
                }
            }
        }
    }
    return grid.sumBy { it.sum() }
}