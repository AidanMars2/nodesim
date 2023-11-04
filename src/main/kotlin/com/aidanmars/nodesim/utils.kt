package com.aidanmars.nodesim

import java.awt.Point
import java.awt.geom.Point2D
import java.util.Random
import kotlin.math.floor

fun generateId(initial: Char): String {
    val random = Random()
    return String(CharArray(7) {
        return@CharArray if (it == 0) initial else idChars.randomChar(random)
    })
}

const val idChars = "abcdefghijklmnopqrstuvwqyz0123456789"

fun String.randomChar(random: Random): Char = this[random.nextInt(length - 1)]

fun getChunk(x: Int, y: Int): Point {
    return Point(
        floor(x.toDouble() / Constants.CHUNK_SIZE).toInt(),
        floor(y.toDouble() / Constants.CHUNK_SIZE).toInt()
    )
}
