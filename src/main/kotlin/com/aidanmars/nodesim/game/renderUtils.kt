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