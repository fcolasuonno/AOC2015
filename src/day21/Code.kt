package day21

import java.io.File
import java.lang.Character.isDigit
import kotlin.math.max

fun main(args: Array<String>) {
    val name = if (true) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val (parsed, equipment) = parse(input)
    println("Part 1 = ${part1(parsed, equipment)}")
    println("Part 2 = ${part2(parsed, equipment)}")
}

data class Item(val name: String, val cost: Int, val damage: Int, val armour: Int)

val weapons = listOf(
    Item("Dagger", 8, 4, 0),
    Item("Shortsword", 10, 5, 0),
    Item("Warhammer", 25, 6, 0),
    Item("Longsword", 40, 7, 0),
    Item("Greataxe", 74, 8, 0)
)

val armour = listOf(
    Item("None", 0, 0, 0),
    Item("Leather", 13, 0, 1),
    Item("Chainmail", 31, 0, 2),
    Item("Splintmail", 53, 0, 3),
    Item("Bandedmail", 75, 0, 4),
    Item("Platemail", 102, 0, 5)
)

val rings = listOf(
    Item("None1", 0, 0, 0),
    Item("None2", 0, 0, 0),
    Item("Damage +1", 25, 1, 0),
    Item("Damage +2", 50, 2, 0),
    Item("Damage +3", 100, 3, 0),
    Item("Defense +1", 20, 0, 1),
    Item("Defense +2", 40, 0, 2),
    Item("Defense +3", 80, 0, 3)
)

data class Stat(var hp: Int, var damage: Int, var armour: Int) {
    operator fun plus(item: Item) = copy(damage = damage + item.damage, armour = armour + item.armour)
}

fun parse(input: List<String>) = input.let {
    Stat(it[0].filter(::isDigit).toInt(), it[1].filter(::isDigit).toInt(), it[2].filter(::isDigit).toInt())
} to weapons.flatMap { w ->
    armour.flatMap { a ->
        rings.flatMap { r1 ->
            rings.filter { it != r1 }.map { r2 -> listOf(w, a, r1, r2) }
        }
    }
}.sortedBy { it.sumBy { it.cost } }

fun playerWins(player: Stat, boss: Stat): Boolean {
    var isPlayerTurn = true
    while (player.hp > 0 && boss.hp > 0) {
        val attacker = if (isPlayerTurn) player else boss
        val defender = if (isPlayerTurn) boss else player
        isPlayerTurn = !isPlayerTurn
        defender.hp -= max(1, attacker.damage - defender.armour)
    }
    return player.hp > 0
}

fun part1(boss: Stat, equipment: List<List<Item>>): Any? = equipment.first { (w, a, r1, r2) ->
    val player = Stat(100, 0, 0) + w + a + r1 + r2
    playerWins(player, boss.copy())
}.sumBy { it.cost }

fun part2(boss: Stat, equipment: List<List<Item>>): Any? = equipment.last { (w, a, r1, r2) ->
    val player = Stat(100, 0, 0) + w + a + r1 + r2
    !playerWins(player, boss.copy())
}.sumBy { it.cost }

