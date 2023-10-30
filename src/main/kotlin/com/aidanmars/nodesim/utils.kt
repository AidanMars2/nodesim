package com.aidanmars.nodesim

import java.util.Random

fun generateId(initial: Char): String {
    val random = Random()
    return String(CharArray(7) {
        return@CharArray if (it == 0) initial else idChars.randomChar(random)
    })
}

const val idChars = "abcdefghijklmnopqrstuvwqyz0123456789"

fun String.randomChar(random: Random): Char = this[random.nextInt(length - 1)]
