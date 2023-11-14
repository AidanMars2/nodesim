package com.aidanmars.nodesim.lwjglgame

import com.aidanmars.nodesim.extensions.tick
import com.aidanmars.nodesim.lwjglgame.data.Input
import com.aidanmars.nodesim.lwjglgame.rendering.GameRenderer
import com.aidanmars.nodesim.lwjglgame.rendering.Renderer
import com.aidanmars.nodesim.lwjglgame.rendering.Window
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL33
import java.util.concurrent.LinkedBlockingQueue
import java.util.logging.Level
import java.util.logging.Logger

object Game {
    private lateinit var errorCallBack: GLFWErrorCallback
    private lateinit var window: Window
    private var running = false
    private val state: GameData = GameData()
    private val inputQueue = LinkedBlockingQueue<Input>()
    private val renderer = Renderer()
    private var windowWidth = 640
    private var windowHeight = 480
    private val timer = Timer()
    private val gameRenderer = GameRenderer(renderer, state)

    fun run() {
        init()
        runSimulation()
        gameLoop()
        dispose()
    }

    private fun runSimulation() {
        // do simulation async
        Thread {
            running = true
            while (running) {
                state.tick(window.toRenderScreenLocation(window.getMouseLocation()))
                state.handleInput(inputQueue)
                state.project.tick()
                sync(40)// force tick rate
            }
        }.start()
    }

    fun end() {
        window.isClosing = true
        running = false
    }

    private fun init() {
        errorCallBack = GLFWErrorCallback.createPrint()
        GLFW.glfwSetErrorCallback(errorCallBack)

        check(GLFW.glfwInit()) { "Unable to initialize GLFW" }

        window = Window(640, 480, "NodeSim", true, inputQueue, ::setWindowSize)
        windowWidth = 640
        windowHeight = 480

        renderer.init()
        gameRenderer.init()

        running = true
    }

    private fun gameLoop() {
        while (!window.isClosing) {
            render()

            window.update()
        }
        running = false
    }

    private fun setWindowSize(width: Int, height: Int) {
        windowWidth = width
        windowHeight = height
        state.width = width
        state.height = height
    }

    private fun render() {
        GL33.glClear(GL33.GL_COLOR_BUFFER_BIT or GL33.GL_DEPTH_BUFFER_BIT)
        gameRenderer.render(windowWidth, windowHeight)
    }

    private fun dispose() {
        renderer.dispose()
        window.destroy()
        GLFW.glfwTerminate()
        errorCallBack.free()
    }

    /**
     * Synchronizes the game at specified updates per second.
     *
     * @param ups updates per second
     */
    private fun sync(ups: Int) {
        val lastLoopTime: Double = timer.lastLoopTime
        var now: Double = timer.time
        val targetTime = 1f / ups
        while (now - lastLoopTime < targetTime) {
            Thread.yield()

            /* This is optional if you want your game to stop consuming too much
             * CPU but you will loose some accuracy because Thread.sleep(1)
             * could sleep longer than 1 millisecond */
            try {
                Thread.sleep(1)
            } catch (ex: InterruptedException) {
                Logger.getLogger(Game::class.java.getName()).log(Level.SEVERE, null, ex)
            }
            now = timer.time
        }
    }
}