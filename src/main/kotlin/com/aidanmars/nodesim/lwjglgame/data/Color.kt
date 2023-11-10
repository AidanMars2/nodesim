package com.aidanmars.nodesim.lwjglgame.data

data class Color(val red: Float, val green: Float, val blue: Float, val alpha: Float = 1f) {
    fun asTransparent() = Color(red, green, blue, 0f)
}
