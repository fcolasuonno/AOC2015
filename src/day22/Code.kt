package day22

import java.io.File
import java.util.*
import kotlin.math.max

fun main(args: Array<String>) {
    val name = if (true) "test.txt" else "input.txt"
    val dir = ::main::class.java.`package`.name
    val input = File("src/$dir/$name").readLines()
    val parsed = parse(input)
    println("Part 1 = ${part1(parsed, Stat.Player(50, 500))}")
    println("Part 2 = ${part2(parsed, Stat.Player(50, 500))}")
}

sealed class Stat {
    abstract fun update()

    abstract var hp: Int
    abstract val effects: MutableList<Effect>

    data class Player(
        override var hp: Int,
        var mana: Int,
        override val effects: MutableList<Effect> = mutableListOf()
    ) : Stat() {
        fun deepCopy() = copy(effects = effects.map { it.copy() }.toMutableList())

        private var armourBonus = 0
        val armour: Int
            get() = armourBonus

        override fun update() {
            armourBonus = 0
            effects.forEach {
                it.duration--
                mana += it.mana
                armourBonus += it.armour
            }
            effects.removeIf { it.duration == 0 }
        }
    }

    data class Boss(
        override var hp: Int,
        val damage: Int,
        override val effects: MutableList<Effect> = mutableListOf()
    ) : Stat() {
        fun deepCopy() = copy(effects = effects.map { it.copy() }.toMutableList())
        override fun update() {
            effects.forEach {
                it.duration--
                hp -= it.damage
            }
            effects.removeIf { it.duration == 0 }
        }
    }
}

data class Spell(
    val name: String,
    val cost: Int,
    val damage: Int,
    val heal: Int,
    val effect: Effect?
) {
    override fun toString() = name
}

data class Effect(
    val name: String,
    val armour: Int,
    val damage: Int,
    val mana: Int,
    var duration: Int
)

val magicMissile = Spell("Magic Missile", 53, 4, 0, null)
val drain = Spell("Drain", 73, 2, 2, null)
val shield = Spell("Shield", 113, 0, 0, Effect("shieldEffect", 7, 0, 0, 6))
val poison = Spell("Poison", 173, 0, 0, Effect("poisonEffect", 0, 3, 0, 6))
val recharge = Spell("Recharge", 229, 0, 0, Effect("rechargeEffect", 0, 0, 101, 5))
val spells = listOf(
    magicMissile,
    drain,
    shield,
    poison,
    recharge
).sortedByDescending { it.cost }

data class Game(val player: Stat.Player, val boss: Stat.Boss, val totalCost: Int) {
    fun update(spell: Spell, hard: Boolean = false): Game {
        val player = player.deepCopy()
        val boss = boss.deepCopy()

        if (hard) {
            player.hp -= 1
        }
        if (player.hp > 0 && boss.hp > 0) {
            player.update()
            boss.update()

            spell.effect?.let {
                player.effects += it.copy()
                boss.effects += it.copy()
            }
            player.mana -= spell.cost
            player.hp += spell.heal
            boss.hp -= spell.damage
        }
        if (player.hp > 0 && boss.hp > 0) {
            player.update()
            boss.update()

            player.hp -= max(1, boss.damage - player.armour)
        }
        return Game(player, boss, totalCost + spell.cost)
    }
}

fun parse(input: List<String>) = input.let {
    Stat.Boss(it[0].filter(Character::isDigit).toInt(), it[1].filter(Character::isDigit).toInt())
}

fun part1(boss: Stat.Boss, player: Stat.Player): Int {
    val unchecked = ArrayDeque<Pair<Game, Spell>>()
    spells.forEach {
        unchecked.addFirst(Game(player, boss, 0) to it)
    }
    var cost = Int.MAX_VALUE
    while (unchecked.isNotEmpty()) {
        val gamePair = unchecked.first
        unchecked.removeFirst()
        val new = gamePair.first.update(gamePair.second)
        if (new.boss.hp <= 0) {
            if (new.totalCost < cost) {
                cost = new.totalCost
            }
        }
        if (new.player.hp > 0 && new.player.mana > 0 && new.totalCost <= cost) {
            spells.filter {
                it.effect?.let { it.name !in new.player.effects.filter { it.duration != 1 }.map { it.name } } ?: true
            }.forEach {
                unchecked.addFirst(new to it)
            }
        }
    }
    return cost
}

fun part2(boss: Stat.Boss, player: Stat.Player): Int {
    val unchecked = ArrayDeque<Pair<Game, Spell>>()
    spells.forEach {
        unchecked.addFirst(Game(player, boss, 0) to it)
    }
    var cost = Int.MAX_VALUE
    while (unchecked.isNotEmpty()) {
        val gamePair = unchecked.first
        unchecked.removeFirst()
        val new = gamePair.first.update(gamePair.second, true)
        if (new.boss.hp <= 0) {
            if (new.totalCost < cost) {
                cost = new.totalCost
            }
        }
        if (new.player.hp > 0 && new.player.mana > 0 && new.totalCost <= cost) {
            spells.filter {
                it.effect?.let { it.name !in new.player.effects.filter { it.duration != 1 }.map { it.name } } ?: true
            }.forEach {
                unchecked.addFirst(new to it)
            }
        }
    }
    return cost
}
