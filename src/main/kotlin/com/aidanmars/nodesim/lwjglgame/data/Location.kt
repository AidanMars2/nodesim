package com.aidanmars.nodesim.lwjglgame.data

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class Location(val x: Float, val y: Float) {
    fun middle(other: Location): Location {
        val dx = other.x - x
        val dy = other.y - y
        return Location(x - dx, y - dy)
    }

    fun distance(other: Location): Float {
        return sqrt((x - other.x).pow(2) + (y - other.y).pow(2))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Location

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}
