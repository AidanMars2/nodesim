package com.aidanmars.nodesim

import java.awt.Point
import kotlin.math.floor

fun getChunk(x: Int, y: Int): Point {
    return Point(
        floor(x.toDouble() / Constants.CHUNK_SIZE).toInt(),
        floor(y.toDouble() / Constants.CHUNK_SIZE).toInt()
    )
}
