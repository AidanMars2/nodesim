package com.aidanmars.nodesim.lwjglgame.rendering

import com.aidanmars.nodesim.lwjglgame.data.Color
import com.aidanmars.nodesim.lwjglgame.data.Point
import org.lwjgl.opengl.GL32.*
import com.aidanmars.nodesim.lwjglgame.angleBetweenPoints
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun Renderer.fillCircle(
    centerX: Float, centerY: Float,
    radius: Float, centerColor: Color,
    edgeColor: Color = centerColor, numSegments: Int = 24
) {
    draw(GL_TRIANGLE_FAN) {
        fillCircleUnsafe(centerX, centerY, radius, centerColor, edgeColor, numSegments)
    }
}

/**
 * this function MUST be called in a draw block with GL_TRIANGLE_FAN
 */
fun Renderer.fillCircleUnsafe(
    centerX: Float, centerY: Float,
    radius: Float, centerColor: Color,
    edgeColor: Color = centerColor, numSegments: Int = 24
) {
    val centerPoint = Point(centerX, centerY, centerColor)
    val radiansPerSegment = (2f * PI / numSegments).toFloat()
    val points = Array(numSegments + 3) {
        if (it == 0) return@Array centerPoint
        val circlePoint = it - 1

        val totalRadians = radiansPerSegment * circlePoint
        val x = radius * cos(totalRadians) + centerX
        val y = radius * sin(totalRadians) + centerY
        Point(x, y, edgeColor)
    }
    drawPoints(*points)
}

fun Renderer.drawCircle(
    centerX: Float, centerY: Float,
    radius: Float, width: Float,
    color: Color, numSegments: Int = 36
) {
    draw(GL_TRIANGLE_STRIP) {
        drawCircleUnsafe(centerX, centerY, radius, width, color, numSegments)
    }
}

/**
 * this function MUST be called in a draw block with GL_TRIANGLE_STRIP
 */
fun Renderer.drawCircleUnsafe(
    centerX: Float, centerY: Float,
    radius: Float, width: Float,
    color: Color, numSegments: Int = 36
) {
    val circleLineWidth = width * 0.5f
    val innerDistance = radius - circleLineWidth
    val outerDistance = radius + circleLineWidth
    val radiansPerSegment = (2f * PI / numSegments).toFloat()
    val points = Array(numSegments + 2) {
        val totalRadians = radiansPerSegment * it
        val thisDistance = if (it and 1 == 1) innerDistance else outerDistance
        val x = thisDistance * cos(totalRadians) + centerX
        val y = thisDistance * sin(totalRadians) + centerY
        Point(x, y, color)
    }
    drawPoints(*points)
}

fun Renderer.drawCircle(
    centerX: Float, centerY: Float,
    radius: Float, width: Float,
    innerColor: Color, outerColor: Color, numSegments: Int = 36
) {
    val circleLineWidth = width * 0.5f
    val innerDistance = radius - circleLineWidth
    val outerDistance = radius + circleLineWidth
    val radiansPerSegment = (2f * PI / numSegments).toFloat()
    val points = Array(numSegments + 2) {
        val totalRadians = radiansPerSegment * it
        val thisDistance = if (it and 1 == 1) innerDistance else outerDistance
        val x = thisDistance * cos(totalRadians) + centerX
        val y = thisDistance * sin(totalRadians) + centerY
        Point(x, y, if (it and 1 == 1) innerColor else outerColor)
    }
    draw(GL_TRIANGLE_STRIP) { drawPoints(*points) }
}

fun Renderer.drawLine(
    p1: Point, p2: Point, width: Float, cap: Boolean
) {
    draw(GL_TRIANGLE_FAN) {
        drawLineUnsafe(p1, p2, width, cap)
    }
}

/**
 * this function MUST be called in a draw block with GL_TRIANGLE_FAN
 */
fun Renderer.drawLineUnsafe(
    p1: Point, p2: Point, width: Float, cap: Boolean
) {
    val baseAngle = angleBetweenPoints(p1.x, p1.y, p2.x, p2.y)
    val radius = width * 0.5f
    val yAdjust = -cos(baseAngle) * radius
    val xAdjust = sin(baseAngle) * radius
    val point1 = Point(p1.x + xAdjust, p1.y + yAdjust, p1.color)
    val point2 = Point(p1.x - xAdjust, p1.y - yAdjust, p1.color)
    val point3 = Point(p2.x + xAdjust, p2.y + yAdjust, p2.color)
    val point4 = Point(p2.x - xAdjust, p2.y - yAdjust, p2.color)
    if (cap) {
        fillCircle(p1.x, p1.y, radius, p1.color)
        fillCircle(p2.x, p2.y, radius, p2.color)
    }
    drawPoints(
        point1, point2, point3, point4
    )
}