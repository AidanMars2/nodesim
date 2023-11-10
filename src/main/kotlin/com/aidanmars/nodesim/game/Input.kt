package com.aidanmars.nodesim.game

import java.awt.Point

data class Input(
    val type: InputType,
    val from: Point,
    val to: Point
) {
    enum class InputType {
        click,
        drag
    }
}