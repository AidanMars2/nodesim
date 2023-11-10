package com.aidanmars.nodesim.lwjglgame

import com.aidanmars.nodesim.lwjglgame.data.Input
import com.aidanmars.nodesim.lwjglgame.rendering.Renderer
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import java.util.concurrent.LinkedBlockingQueue

object Game {
    private lateinit var errorCallBack: GLFWErrorCallback
    private lateinit var window: Window
    private var running = false
    private val state: GameData = GameData()
    private val inputQueue = LinkedBlockingQueue<Input>()
    private val renderer = Renderer()
    private var windowWidth = 640
    private var windowHeight = 480

    private fun runGraphics() {
        init()
        gameLoop()
        dispose()
    }

    private fun runSimulation() {
        // do simulation async
        TODO()
    }

    fun run() {
        runSimulation()
        runGraphics()
    }

    private fun init() {
        errorCallBack = GLFWErrorCallback.createPrint()
        GLFW.glfwSetErrorCallback(errorCallBack)

        check(GLFW.glfwInit()) { "Unable to initialize GLFW" }

        window = Window(640, 480, "NodeSim", true, inputQueue, ::setWindowSize)
        windowWidth = 640
        windowHeight = 480

        renderer.init()

        running = true
    }

    private fun gameLoop() {
        while (!window.isClosing) {
            render()

            window.update()
        }
    }

    private fun setWindowSize(width: Int, height: Int) {
        windowWidth = width
        windowHeight = height
    }

    private fun render() {
        renderer.setScale(1 / (windowWidth * 0.5f), 1 / (windowHeight * 0.5f))
        TODO()
    }

    private fun dispose() {
        renderer.dispose()
        window.destroy()
        GLFW.glfwTerminate()
        errorCallBack.free()
    }
}