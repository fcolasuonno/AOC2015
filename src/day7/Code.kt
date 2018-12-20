package day7

import java.io.File

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed)}")
    println("Part 2 = ${part2(parsed)}")
}

sealed class Gate(val output: String) {
    abstract fun evaluate(values: Map<String, Int>): Int?
}

fun numberOrValue(input: String, values: Map<String, Int>) =
    input.toIntOrNull() ?: values[input]

class Assign(val input: String, output: String) : Gate(output) {
    override fun evaluate(values: Map<String, Int>) = numberOrValue(input, values)
}

class And(val input1: String, val input2: String, output: String) : Gate(output) {
    override fun evaluate(values: Map<String, Int>) =
        numberOrValue(input2, values)?.let {
            numberOrValue(input1, values)?.and(it)
        }
}

class Or(val input1: String, val input2: String, output: String) : Gate(output) {
    override fun evaluate(values: Map<String, Int>) =
        numberOrValue(input2, values)?.let {
            numberOrValue(input1, values)?.or(it)
        }
}

class LShift(val input1: String, val input2: String, output: String) : Gate(output) {
    override fun evaluate(values: Map<String, Int>) =
        numberOrValue(input2, values)?.let {
            numberOrValue(input1, values)?.shl(it)
        }
}

class RShift(val input1: String, val input2: String, output: String) : Gate(output) {
    override fun evaluate(values: Map<String, Int>) =
        numberOrValue(input2, values)?.let {
            numberOrValue(input1, values)?.shr(it)
        }
}

class Not(val input: String, output: String) : Gate(output) {
    override fun evaluate(values: Map<String, Int>) = numberOrValue(input, values)?.let { it.inv() and 0xffff }

}

private val lineStructure = """(.+) -> (.+)""".toRegex()
private val ops = mutableMapOf<Regex, (List<String>, String) -> Gate>(
    """([0-9a-z]+)""".toRegex() to { match, out -> Assign(match[1], out) },
    """(.+) AND (.+)""".toRegex() to { match, out -> And(match[1], match[2], out) },
    """(.+) OR (.+)""".toRegex() to { match, out -> Or(match[1], match[2], out) },
    """(.+) LSHIFT (.+)""".toRegex() to { match, out -> LShift(match[1], match[2], out) },
    """(.+) RSHIFT (.+)""".toRegex() to { match, out -> RShift(match[1], match[2], out) },
    """NOT (.+)""".toRegex() to { match, out -> Not(match[1], out) }
)

fun parse(input: List<String>) = input.map {
    lineStructure.matchEntire(it)?.destructured?.let {
        val (gate, output) = it
        if (!ops.any { it.key.matches(gate) }) {
            println(gate)
        }
        ops.mapNotNull { it.key.matchEntire(gate)?.let { result -> it.value(result.groupValues, output) } }.single()
    }
}.requireNoNulls()

fun part1(input: List<Gate>): Int? {
    val values = mutableMapOf<String, Int>()
    while (input.any { it.evaluate(values) == null }) {
        input.forEach { it.evaluate(values)?.let { output -> values[it.output] = output } }
    }
    input.forEach { it.evaluate(values)?.let { output -> values[it.output] = output } }
    return values["a"]
}

fun part2(input: List<Gate>): Int? {
    var values = mutableMapOf<String, Int>()
    while (input.any { it.evaluate(values) == null }) {
        input.forEach { it.evaluate(values)?.let { output -> values[it.output] = output } }
    }
    input.forEach { it.evaluate(values)?.let { output -> values[it.output] = output } }
    val a = values.getValue("a")
    values = mutableMapOf("b" to a)
    while (input.filterNot { it.output == "b" }.any { it.evaluate(values) == null }) {
        input.filterNot { it.output == "b" }
            .forEach { it.evaluate(values)?.let { output -> values[it.output] = output } }
    }
    input.filterNot { it.output == "b" }.forEach { it.evaluate(values)?.let { output -> values[it.output] = output } }
    return values["a"]
}