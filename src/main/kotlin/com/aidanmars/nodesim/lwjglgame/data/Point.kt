package com.aidanmars.nodesim.lwjglgame.data

data class Point(val x: Float, val y: Float, val color: Color) {
    override fun toString(): String {
        return "Point(x=$x, y=$y, color=$color)"
    }
}
