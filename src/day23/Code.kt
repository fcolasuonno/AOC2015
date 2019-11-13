package day23

import java.io.File

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed)}")
    println("Part 2 = ${part2(parsed)}")
}

data class Op(val symbol: String, val reg: Char, val jump: Int) {
    fun exec(regs: MutableMap<Char, Long>) {
        when (symbol) {
            "hlf" -> regs[reg] = regs.getValue(reg) / 2
            "tpl" -> regs[reg] = regs.getValue(reg) * 3
            "inc" -> regs[reg] = regs.getValue(reg) + 1
            "jmp" -> regs['i'] = regs.getValue('i') + jump - 1
            "jie" -> if (regs.getValue(reg) % 2L == 0L) {
                regs['i'] = regs.getValue('i') + jump - 1
            }
            "jio" -> if (regs.getValue(reg) == 1L) {
                regs['i'] = regs.getValue('i') + jump - 1
            }
        }
        regs['i'] = regs.getValue('i') + 1
    }
}

private val lineStructure = """(\w\w\w) (\w)(, ([+-]?\d+))?""".toRegex()
private val lineStructure1 = """(jmp) ([+-]?\d+)""".toRegex()

fun parse(input: List<String>) = input.map {
    lineStructure.matchEntire(it)?.destructured?.let {
        val (op, reg, _, jump) = it.toList()
        Op(op, reg.single(), jump.toIntOrNull() ?: 0)
    } ?: lineStructure1.matchEntire(it)?.destructured?.let {
        val (op, jump) = it.toList()
        Op(op, 'i', jump.toIntOrNull() ?: 0)
    }
}.requireNoNulls()

fun part1(input: List<Op>): Any? = mutableMapOf('i' to 0L, 'a' to 0L, 'b' to 0L).let { regs ->
    generateSequence {
        input.getOrNull(regs.getValue('i').toInt())?.also { it.exec(regs) }
    }.last()
    regs['b']
}

fun part2(input: List<Op>): Any? = mutableMapOf('i' to 0L, 'a' to 1L, 'b' to 0L).let { regs ->
    generateSequence {
        input.getOrNull(regs.getValue('i').toInt())?.also { it.exec(regs) }
    }.last()
    regs['b']
}
