package com.aidanmars.nodesim.lwjglgame

import org.lwjgl.opengl.GL
import java.awt.Point
import java.io.InputStream
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.atan2

fun resourceInputStreamOf(path: String): InputStream? {
    return Thread.currentThread().contextClassLoader.getResourceAsStream(path)
}

fun isDefaultContext() = GL.getCapabilities().OpenGL32

fun angleBetweenPoints(
    x1: Float, y1: Float,
    x2: Float, y2: Float
): Float {
    val dx = x1 - x2
    val dy = y1 - y2
    return atan(dx / dy) + if (dy < 0) PI.toFloat() else 0f
}

const val HALF_PI = PI * 0.5
const val TWO_PI = PI * 2
