package com.aidanmars.nodesim.lwjglgame.rendering

import com.aidanmars.nodesim.lwjglgame.data.Input
import com.aidanmars.nodesim.lwjglgame.data.Location
import com.aidanmars.nodesim.lwjglgame.toRenderScreenLocation
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWFramebufferSizeCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFWMouseButtonCallback
import org.lwjgl.glfw.GLFWScrollCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL33
import org.lwjgl.system.MemoryUtil
import java.util.concurrent.LinkedBlockingQueue
import kotlin.math.floor

/**
 * This class represents a GLFW window.
 *
 * @author Heiko Brumme
 */
class Window(
    var width: Int,
    var height: Int,
    title: CharSequence,
    private var isVSyncEnabled: Boolean,
    private val inputQueue: LinkedBlockingQueue<Input>,
    private val setWindowSize: (width: Int, height: Int) -> Unit
) {
    private val handle: Long
    private val keyCallback: GLFWKeyCallback
    private val mouseButtonCallback: GLFWMouseButtonCallback
    private val windowSizeCallBack: GLFWFramebufferSizeCallback
    private val scrollCallback: GLFWScrollCallback

    init {

        /* Creating a temporary window for getting the available OpenGL version */GLFW.glfwDefaultWindowHints()
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)
        val temp = GLFW.glfwCreateWindow(1, 1, "", MemoryUtil.NULL, MemoryUtil.NULL)
        GLFW.glfwMakeContextCurrent(temp)
        GL.createCapabilities()
        val caps = GL.getCapabilities()
        GLFW.glfwDestroyWindow(temp)

        /* Reset and set window hints */GLFW.glfwDefaultWindowHints()
        check(caps.OpenGL33) { "get a better gpu(openGL 3.3)" }
        /* Hints for OpenGL 3.2 core profile */
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3)
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE)
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE)

        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE)

        /* Create window with specified OpenGL context */
        handle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)
        if (handle == MemoryUtil.NULL) {
            GLFW.glfwTerminate()
            throw RuntimeException("Failed to create the GLFW window!")
        }

        /* Center window on screen */
        val vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())
        GLFW.glfwSetWindowPos(
            handle,
            (vidmode!!.width() - width) / 2,
            (vidmode.height() - height) / 2
        )

        /* Create OpenGL context */GLFW.glfwMakeContextCurrent(handle)
        GL.createCapabilities()

        /* Enable v-sync */
        if (isVSyncEnabled) {
            GLFW.glfwSwapInterval(1)
        }

        /* Set callbacks */
        keyCallback = getNewKeyCallBack()
        GLFW.glfwSetKeyCallback(handle, keyCallback)
        mouseButtonCallback = getNewMouseButtonCallBack()
        GLFW.glfwSetMouseButtonCallback(handle, mouseButtonCallback)
        windowSizeCallBack = getNewFrameBufferSizeCallBack()
        GLFW.glfwSetFramebufferSizeCallback(handle, windowSizeCallBack)
        scrollCallback = getNewScrollCallBack()
        GLFW.glfwSetScrollCallback(handle, scrollCallback)
    }

    private fun getNewKeyCallBack(): GLFWKeyCallback = object : GLFWKeyCallback() {
        override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
            val mouseLocation = getMouseLocation()
            val mouseScreenLocation = toRenderScreenLocation(mouseLocation)
            inputQueue.put(
                Input(
                when (action) {
                    GLFW.GLFW_PRESS -> Input.Type.keyPress
                    else -> Input.Type.keyRelease
                }, mods, mouseScreenLocation,
                    pixelPointInScreen(mouseLocation),
                    value = key
            ))
        }
    }

    private fun getNewMouseButtonCallBack() = object : GLFWMouseButtonCallback() {
        override fun invoke(window: Long, button: Int, action: Int, mods: Int) {
            val mouseLocation = getMouseLocation()
            val mouseScreenLocation = toRenderScreenLocation(mouseLocation)
            inputQueue.put(
                Input(
                when (action) {
                    GLFW.GLFW_PRESS -> Input.Type.mousePress
                    else -> Input.Type.mouseRelease
                }, mods, mouseScreenLocation,
                    pixelPointInScreen(mouseLocation),
                    value = button
            ))
        }
    }

    private fun getNewScrollCallBack() = object : GLFWScrollCallback() {
        override fun invoke(window: Long, xoffset: Double, yoffset: Double) {
            val mouseLocation = getMouseLocation()
            val mouseScreenLocation = toRenderScreenLocation(mouseLocation)
            inputQueue.put(
                Input(
                    Input.Type.scroll,
                    0,
                    mouseScreenLocation,
                    pixelPointInScreen(mouseLocation),
                    yoffset
                )
            )
        }
    }

    private fun pixelPointInScreen(point: Location): Boolean {
        return point.x in 0f..width.toFloat() && point.y in 0f..height.toFloat()
    }

    private fun getNewFrameBufferSizeCallBack() = object : GLFWFramebufferSizeCallback() {
        override fun invoke(window: Long, width: Int, height: Int) {
            GL33.glViewport(0, 0, width, height)
            setWindowSize(width, height)
        }
    }

    fun getMouseLocation(): Location {
        val posX = BufferUtils.createDoubleBuffer(1)
        val posY = BufferUtils.createDoubleBuffer(1)
        GLFW.glfwGetCursorPos(handle, posX, posY)
        return Location(floor(posX[0]).toFloat(), floor(posY[0]).toFloat())
    }

    var isClosing: Boolean
        get() = GLFW.glfwWindowShouldClose(handle)
        set(value) = GLFW.glfwSetWindowShouldClose(handle, value)

    /**
     * Sets the window title
     *
     * @param title New window title
     */
    fun setTitle(title: CharSequence) {
        GLFW.glfwSetWindowTitle(handle, title)
    }

    /**
     * Updates the screen.
     */
    fun update() {
        GLFW.glfwSwapBuffers(handle)
        GLFW.glfwPollEvents()
    }

    /**
     * Destroys the window an releases its callbacks.
     */
    fun destroy() {
        Callbacks.glfwFreeCallbacks(handle)
        GLFW.glfwDestroyWindow(handle)
    }

    /**
     * Setter for v-sync.
     *
     * @param vsync Set to true to enable v-sync
     */
    fun setVSync(vsync: Boolean) {
        isVSyncEnabled = vsync
        GLFW.glfwSwapInterval(if (vsync) 1 else 0)
    }

    fun swapBuffers() = GLFW.glfwSwapBuffers(handle)
}
