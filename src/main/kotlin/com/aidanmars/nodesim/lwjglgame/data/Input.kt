package com.aidanmars.nodesim.lwjglgame.data

data class Input(
    val type: Type,
    val mods: Int,
    val mouseLocationX: Float,
    val mouseLocationY: Float,
    val mouseInScreen: Boolean,
    val doubleValue: Double = 0.0,
    val floatValue: Float = 0.0f,
    val value: Int = 0,
) {
    enum class Type {
        keyPress,
        keyRelease,
        mousePress,
        mouseRelease,
        scroll
    }
}