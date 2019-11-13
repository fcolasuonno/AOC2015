package day24

import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part(parsed, parsed.sum() / 3)}")
    println("Part 2 = ${part(parsed, parsed.sum() / 4)}")
}

fun parse(input: List<String>) = input.map {
    it.toInt()
}.requireNoNulls().toSet().toSortedSet()

fun part(input: SortedSet<Int>, weight: Int): Long? {
    val queue = ArrayDeque<Pair<Set<Int>, Int>>()
    val candidates = mutableSetOf<Set<Int>>()
    val seen = mutableSetOf<Set<Int>>()
    input.forEach {
        queue.addFirst(setOf(it) to (weight - it))
    }
    while (queue.isNotEmpty()) {
        val (head, leftWeight) = queue.first()
        queue.removeFirst()
        if (seen.none { it.containsAll(head) }) {
            val size = candidates.map { it.size }.min() ?: Int.MAX_VALUE
            (input - head).filter {
                when {
                    size > (head.size - 1) -> (it <= leftWeight)
                    size == (head.size - 1) -> (it == leftWeight)
                    else -> false
                }
            }.forEach {
                val new = head + it
                if (leftWeight - it == 0 && new.size <= size) {
                    candidates.add(new)
                } else {
                    if (new.size < size) {
                        queue.addFirst(Pair(new, leftWeight - it))
                    }
                }
            }
            seen.add(head)
        }
    }
    return candidates.map { it.fold(1L) { qe, i -> qe * i } }.min()
}
