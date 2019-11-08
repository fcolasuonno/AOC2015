package day10

import java.io.File

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part(parsed, 40)}")
    println("Part 2 = ${part(parsed, 50)}")
}

data class Group(val char: Char, var num: Int = 0)

fun part(list: List<Char>, repeat: Int): Int {
    var sequence = list
    repeat(repeat) {
        sequence = sequence.fold(mutableListOf<Group>()) { groups, char ->
            groups.apply {
                if (lastOrNull()?.char != char) add(Group(char))
                last().num++
            }
        }.flatMap { listOf('0' + it.num, it.char) }
    }
    return sequence.size
}

fun parse(input: List<String>) = input.first().toList()


