package com.aidanmars.nodesim.lwjglgame

import com.aidanmars.nodesim.NodeType
import com.aidanmars.nodesim.Project
import com.aidanmars.nodesim.WorldLocation
import com.aidanmars.nodesim.lwjglgame.data.Input
import com.aidanmars.nodesim.lwjglgame.data.Location
import com.aidanmars.nodesim.lwjglgame.rendering.HUDElement
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
    var width = glfwGetVideoMode(glfwGetPrimaryMonitor())?.width() ?: 640
    var height = glfwGetVideoMode(glfwGetPrimaryMonitor())?.height() ?: 480
    private val wasdKeysPressed = BooleanArray(4) // w, a, s, d
    val hudElements = mutableListOf<HUDElement>()


    fun getScreenLocation(x: Int, y: Int): Location {
        return Location((x - playerXLocation) * scale, (y - playerYLocation) * scale)
    }

    fun getWorldLocation(x: Float, y: Float): WorldLocation {
        return WorldLocation((x / scale).toInt() + playerXLocation, (y / scale).toInt() + playerYLocation)
    }

    fun getWorldLocation(location: Location): WorldLocation = getWorldLocation(location.x, location.y)

    fun tick(mouseLocation: Location) {
        if (wasdKeysPressed[0]) playerYLocation += (6 / scale).toInt()
        if (wasdKeysPressed[1]) playerXLocation -= (6 / scale).toInt()
        if (wasdKeysPressed[2]) playerYLocation -= (6 / scale).toInt()
        if (wasdKeysPressed[3]) playerXLocation += (6 / scale).toInt()
        selectionLocation2 = getWorldLocation(mouseLocation)
    }

    fun handleInput(inputQueue: LinkedBlockingQueue<Input>) {
        while (inputQueue.isNotEmpty()) {
            val it = inputQueue.poll()
            if (!it.mouseInScreen) continue
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
            GLFW_KEY_E -> setPlaceType(NodeType.inverter)
            GLFW_KEY_R -> setPlaceType(NodeType.light)
            GLFW_KEY_T -> setPlaceType(NodeType.switch)
        }
    }

    private fun setPlaceType(type: NodeType) {
        currentPlaceType = type
        currentTool = ToolType.place
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
        val hudElement = hudElements.lastOrNull { it.hitPredicate(input.mouseLocation) }
        if (hudElement !== null) {
            hudElement.hitAction()
            return
        }
        selectionLocation2 = getWorldLocation(input.mouseLocation)
        handleClick()
    }

    private fun handleClick() {

    }

    fun registerHUDElement(element: HUDElement) {
        hudElements.add(element)
    }

    private fun handleScroll(input: Input) {
        val scaleScale = 1.1.pow(input.doubleValue).toFloat()
        scale = if (input.doubleValue > 0) scale * scaleScale else scale / scaleScale
        if (scale < 0.1f) scale = 0.1f
        if (scale > 10f) scale = 10f
    }
}