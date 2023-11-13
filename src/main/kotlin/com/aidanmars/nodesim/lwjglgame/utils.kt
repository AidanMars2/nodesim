package com.aidanmars.nodesim.lwjglgame

import com.aidanmars.nodesim.lwjglgame.data.Location
import com.aidanmars.nodesim.lwjglgame.rendering.Window
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

fun Window.toRenderScreenLocation(location: Location): Location {
    return Location(location.x - (width shr 1),
        /*pixel are from top, rendering is from bottom*/(height - location.y) - (height shr 1))
}

fun toRenderScreenLocation(
    location: Location,
    windowWidth: Int, windowHeight: Int
): Location {
    return Location(location.x - (windowWidth shr 1),
        /*pixel are from top, rendering is from bottom*/(windowHeight - location.y) - (windowHeight shr 1))
}
