package com.aidanmars.nodesim.lwjglgame.data

data class Input(
    val type: Type,
    val mods: Int,
    val mouseLocation: Location,
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