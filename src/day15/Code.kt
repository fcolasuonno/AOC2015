package day15

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

data class Ingredient(
    val name: String,
    val score: Score
)

data class Score(
    val capacity: Int = 0,
    val durability: Int = 0,
    val flavor: Int = 0,
    val texture: Int = 0,
    val calories: Int
) {
    val part1 = max(0, capacity) * max(0, texture) * max(0, flavor) * max(0, durability)

    operator fun times(quantity: Int) =
        copy(capacity * quantity, durability * quantity, flavor * quantity, texture * quantity, calories * quantity)

    operator fun plus(other: Score) = copy(
        capacity + other.capacity,
        durability + other.durability,
        flavor + other.flavor,
        texture + other.texture,
        calories + other.calories
    )
}

private val List<Pair<Ingredient, Int>>.score: Score
    get() = map { (ingredient, quantity) -> ingredient.score * quantity }.reduce { acc, score -> acc + score }
private val lineStructure =
    """(\w+): capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (-?\d+)""".toRegex()

fun parse(input: List<String>) = input.map {
    lineStructure.matchEntire(it)?.destructured?.let {
        operator fun List<String>.component6() = this[5]
        val (name, capacity, durability, flavor, texture, calories) = it.toList()
        Ingredient(name, Score(capacity.toInt(), durability.toInt(), flavor.toInt(), texture.toInt(), calories.toInt()))
    }
}.requireNoNulls()


fun part1(input: List<Ingredient>): Any? = 100.possibleSums(input.size).map {
    input.zip(it).score.part1
}.max()

fun part2(input: List<Ingredient>): Any? = 100.possibleSums(input.size).map {
    input.zip(it).score
}.filter { it.calories == 500 }.map { it.part1 }.max()

private fun Int.possibleSums(size: Int): List<List<Int>> = when {
    size == 1 -> listOf(listOf(this))
    else -> (0..this).flatMap { curr ->
        (this - curr).possibleSums(size - 1).map {
            listOf(curr) + it
        }
    }
}