package com.aidanmars.nodesim.lwjglgame.rendering

import com.aidanmars.nodesim.lwjglgame.data.Location
import com.aidanmars.nodesim.lwjglgame.rendering.Renderer

data class HUDElement(
    val hitPredicate: (Location) -> Boolean,
    val hitAction: () -> Unit,
    val draw: (Renderer) -> Unit
)
