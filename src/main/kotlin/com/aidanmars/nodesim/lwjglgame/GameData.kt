package com.aidanmars.nodesim.lwjglgame

import com.aidanmars.nodesim.NodeType
import com.aidanmars.nodesim.Project
import com.aidanmars.nodesim.WorldLocation
import com.aidanmars.nodesim.lwjglgame.data.Input
import com.aidanmars.nodesim.lwjglgame.data.Location
import org.lwjgl.glfw.GLFW.*
import java.util.concurrent.LinkedBlockingQueue
import kotlin.math.pow


class GameData {
    var playerXLocation = 0
    var playerYLocation = 0
    var scale = 1f
    val project = Project()
    var currentTool = ToolType.interact
    var currentPlaceType = NodeType.switch
    var selectionLocation1 = WorldLocation(0, 0)
    var selectionLocation2 = WorldLocation(0, 0)
    val wasdKeysPressed = BooleanArray(4)

    fun getScreenLocation(x: Int, y: Int): Location {
        return Location((x - playerXLocation) * scale, (y - playerYLocation) * scale)
    }

    fun getWorldLocation(x: Float, y: Float): WorldLocation {
        return WorldLocation((x / scale).toInt() + playerXLocation, (y / scale).toInt() + playerYLocation)
    }

    fun getWorldLocation(location: Location): WorldLocation = getWorldLocation(location.x, location.y)

    fun handleInput(inputQueue: LinkedBlockingQueue<Input>) {
        inputQueue.forEach {
            if (!it.mouseInScreen) return@forEach
            when (it.type) {
                Input.Type.keyPress -> handleKeyPress(it)
                Input.Type.keyRelease -> handleKeyRelease(it)
                Input.Type.mousePress -> handleMousePress(it)
                Input.Type.mouseRelease -> handleMouseRelease(it)
                Input.Type.scroll -> handleScroll(it)
            }
        }
    }

    private fun handleKeyPress(input: Input) {
        when (input.value) {
            GLFW_KEY_1 -> currentTool = ToolType.place
            GLFW_KEY_2 -> currentTool = ToolType.connect
            GLFW_KEY_3 -> currentTool = ToolType.interact
            GLFW_KEY_4 -> currentTool = ToolType.delete
            GLFW_KEY_W -> wasdKeysPressed[0] = true
            GLFW_KEY_A -> wasdKeysPressed[1] = true
            GLFW_KEY_S -> wasdKeysPressed[2] = true
            GLFW_KEY_D -> wasdKeysPressed[3] = true
            GLFW_KEY_E -> currentPlaceType = NodeType.inverter
            GLFW_KEY_R -> currentPlaceType = NodeType.light
            GLFW_KEY_T -> currentPlaceType = NodeType.switch
        }
    }

    private fun handleKeyRelease(input: Input) {
        when (input.value) {
            GLFW_KEY_W -> wasdKeysPressed[0] = false
            GLFW_KEY_A -> wasdKeysPressed[1] = false
            GLFW_KEY_S -> wasdKeysPressed[2] = false
            GLFW_KEY_D -> wasdKeysPressed[3] = false
        }
    }

    private fun handleMousePress(input: Input) {
        selectionLocation1 = getWorldLocation(input.mouseLocation)
    }

    private fun handleMouseRelease(input: Input) {
        selectionLocation2 = getWorldLocation(input.mouseLocation)
        handleClick()
    }

    private fun handleClick() {

    }

    private fun handleScroll(input: Input) {
        val scaleScale = 1.1.pow(input.doubleValue).toFloat()
        scale = if (input.doubleValue > 0) scale * scaleScale else scale / scaleScale
    }
}