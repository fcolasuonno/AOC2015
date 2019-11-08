package day12

import java.io.File

fun main(args: Array<String>) {
    val name = if (false) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed)}")
    println("Part 2 = ${part2(parsed)}")
}

interface JSON {
    fun flatten(filtered: Boolean): List<JSON> =
        listOf(this) + children?.flatMap { it.flatten(filtered) }.orEmpty()

    val children: List<JSON>? get() = null
    val start: Int
    var end: Int
}

data class JSONNumber(val string: List<Char>, override val start: Int = 0, override var end: Int = start) : JSON {
    val value: Int

    init {
        var index = start
        while (string[index] in '0'..'9' || string[index] == '-') index++
        end = index
        value = string.subList(start, end).joinToString(separator = "").toInt()
    }
}

data class JSONString(val string: List<Char>, override val start: Int = 0, override var end: Int = start) : JSON {
    val value: String

    init {
        var index = start + 1
        while (string[index] != '"') index++
        end = index + 1
        value = string.subList(start + 1, end - 1).joinToString(separator = "")
    }
}

data class JSONArray(val string: List<Char>, override val start: Int = 0, override var end: Int = start) : JSON {
    override val children = mutableListOf<JSON>()

    init {
        var index = start + 1
        while (string[index] != ']') {
            if (string[index] == ',') index++
            children.add(when (string[index]) {
                '[' -> JSONArray(string, index)
                '{' -> JSONObject(string, index)
                '-' -> JSONNumber(string, index)
                in ('0'..'9') -> JSONNumber(string, index)
                '"' -> JSONString(string, index)
                else -> throw IllegalArgumentException()
            }.also { index = it.end })
        }
        end = index + 1
    }
}

data class JSONObject(val string: List<Char>, override val start: Int = 0, override var end: Int = start) : JSON {
    private val map = mutableMapOf<JSONString, JSON>()

    override fun flatten(filtered: Boolean) =
        if (filtered && map.values.any { it is JSONString && it.value == "red" }) emptyList() else super.flatten(
            filtered
        )

    override val children get() = map.values.toList()

    init {
        var index = start + 1
        while (string[index] != '}') {
            if (string[index] == ',') index++
            val key = JSONString(string, index).also { index = it.end }
            if (string[index] == ':') index++
            val value = when (string[index]) {
                '[' -> JSONArray(string, index)
                '{' -> JSONObject(string, index)
                '-' -> JSONNumber(string, index)
                in ('0'..'9') -> JSONNumber(string, index)
                '"' -> JSONString(string, index)
                else -> throw IllegalArgumentException()
            }.also { index = it.end }
            map[key] = value
        }
        end = index + 1
    }
}

fun parse(input: List<String>) = input.take(1).map { string ->
    if (string[0] == '[') {
        JSONArray(string.toList())
    } else {
        JSONObject(string.toList())
    }
}.requireNoNulls()

fun part1(input: List<JSON>): Any? = input.map { it.flatten(false).filterIsInstance<JSONNumber>().sumBy { it.value } }

fun part2(input: List<JSON>): Any? = input.map { it.flatten(true).filterIsInstance<JSONNumber>().sumBy { it.value } }
