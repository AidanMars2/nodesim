package com.aidanmars.nodesim.lwjglgame

import java.io.InputStream
import kotlin.math.PI
import kotlin.math.atan

fun resourceInputStreamOf(path: String): InputStream? {
    return Thread.currentThread().contextClassLoader.getResourceAsStream(path)
}

fun angleBetweenPoints(
    x1: Float, y1: Float,
    x2: Float, y2: Float
): Float {
    val dx = x1 - x2
    val dy = y1 - y2
    return atan(dx / dy) + if (dy < 0) PI.toFloat() else 0f
}
