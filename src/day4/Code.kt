package day4

import java.security.MessageDigest
import kotlin.experimental.and

fun main(args: Array<String>) {
    val input = if (false) "pqrstuv" else "yzbqklnj"
    println("Part 1 = ${part1(input)}")
    println("Part 2 = ${part2(input)}")
}

val md5 = MessageDigest.getInstance("MD5")

fun part1(input: String): Any? {
    var count = 1
    var initial = input + 1
    var bytes = initial.toByteArray()
    var size = bytes.size - 1
    val overflowByte = ('9' + 1).toByte()
    var a: ByteArray
    do {
        count++
        bytes[size]++
        if (bytes[size] == overflowByte) {
            for (i in size downTo input.length) {
                if (bytes[i] == overflowByte) {
                    bytes[i] = '0'.toByte()
                    if (i == input.length) {
                        initial += 0
                        bytes = initial.toByteArray()
                        size = bytes.size - 1
                    } else {
                        bytes[i - 1] = (bytes[i - 1] + 1).toByte()
                    }
                } else {
                    break
                }
            }
        }
        a = md5.digest(bytes)
    } while (a[0] != 0.toByte() || a[1] != 0.toByte() || (a[2].and(0xf0.toByte()) != 0.toByte()))
    return count
}

fun part2(input: String): Any? {
    var count = 1
    var initial = input + 1
    var bytes = initial.toByteArray()
    var size = bytes.size - 1
    val overflowByte = ('9' + 1).toByte()
    var a: ByteArray
    do {
        count++
        bytes[size]++
        if (bytes[size] == overflowByte) {
            for (i in size downTo input.length) {
                if (bytes[i] == overflowByte) {
                    bytes[i] = '0'.toByte()
                    if (i == input.length) {
                        initial += 0
                        bytes = initial.toByteArray()
                        size = bytes.size - 1
                    } else {
                        bytes[i - 1] = (bytes[i - 1] + 1).toByte()
                    }
                } else {
                    break
                }
            }
        }
        a = md5.digest(bytes)
    } while (a[0] != 0.toByte() || a[1] != 0.toByte() || a[2] != 0.toByte())
    return count
}
