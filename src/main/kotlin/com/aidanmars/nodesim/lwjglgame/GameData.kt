package com.aidanmars.nodesim.lwjglgame


class GameData {
    var playerXLocation = 0
    var playerYLocation = 0
    var scale = 1f

    fun getScreenLocation(x: Int, y: Int): Pair<Float, Float> {
        return (x - playerXLocation) * scale to (y - playerYLocation) * scale
    }

    fun getWorldLocation(x: Float, y: Float): Pair<Int, Int> {
        return (x / scale).toInt() + playerXLocation to (y / scale).toInt() + playerYLocation
    }
}