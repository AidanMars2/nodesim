package com.aidanmars.nodesim.game

import java.awt.Image
import java.awt.Point
import kotlin.math.atan


fun angleBetweenPoints(point1: Point, point2: Point): Double {
    val dx = point1.x - point2.x.toDouble()
    val dy = point1.y - point2.y.toDouble()
    return Math.toDegrees(atan((dx / dy))) + if (dy < 0) 180 else 0
}

fun Point.middle(other: Point) =
    Point(x - (x - other.x shr 1), y - (y - other.y shr 1))

fun Point.asMiddleOf(image: Image) =
    Point(x + (image.getWidth(null) shr 1), y + (image.getHeight(null) shr 1))

fun Point.fromMiddleOf(image: Image) =
    Point(x - (image.getWidth(null) shr 1), y - (image.getHeight(null) shr 1))

fun calculateClosestPoint(point: Point, point1: Point, point2: Point): Point {
    val lineSlope = (point1.y - point2.y).toFloat() / (point1.x - point2.x)
    val perpendicular = -1 / lineSlope
    var closestLineX = (((
            ((lineSlope * point1.x) - point1.y) -
                    (perpendicular * point.x)) + point.y) / (lineSlope - perpendicular)).toInt()
    var closestLineY = ((lineSlope * (closestLineX - point1.x)) + point1.y).toInt()
    when {
        lineSlope == 0F -> {
            closestLineX = point.x
            closestLineY = point1.y
        }
        (1 / lineSlope) == 0F -> {
            closestLineX = point1.x
            closestLineY = point.y
        }
    }
    val p1RightOfP2 = point1.x > point2.x
    val cPoint = if (p1RightOfP2) point2 else point1
    val dPoint = if (p1RightOfP2) point1 else point2
    if (closestLineX > dPoint.x) {
        closestLineX = dPoint.x
        closestLineY = dPoint.y
    }
    if (closestLineX < cPoint.x) {
        closestLineX = cPoint.x
        closestLineY = cPoint.y
    }
    return Point(closestLineX, closestLineY)
}