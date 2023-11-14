package com.aidanmars.nodesim.lwjglgame.rendering

import com.aidanmars.nodesim.lwjglgame.data.Point
import com.aidanmars.nodesim.lwjglgame.data.VertexArrayObject
import com.aidanmars.nodesim.lwjglgame.data.VertexBufferObject
import com.aidanmars.nodesim.lwjglgame.math.Vector2f
import org.lwjgl.opengl.GL32.*
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import kotlin.properties.Delegates

class Renderer {
    private lateinit var program: ShaderProgram
    private var drawing = false
    private lateinit var vao: VertexArrayObject
    private lateinit var vbo: VertexBufferObject
    private lateinit var vertices: FloatBuffer
    private var numVertices = 0
    private var xScale = 1f
    private var yScale = 1f
    private var scaleAttributePosition by Delegates.notNull<Int>()

    fun dispose() {
        MemoryUtil.memFree(vertices)
        vao.delete()
        vbo.delete()

        program.delete()
    }

    fun init() {
        setupShaderProgram()

        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        glClearColor(0.75f, 0.75f, 0.75f, 1f)
    }

    /**
     * Begin rendering.
     */
    fun begin() {
        check(!drawing) { "Renderer is already drawing!" }
        drawing = true
        numVertices = 0
    }

    /**
     * End rendering.
     */
    fun end(drawType: Int = GL_TRIANGLES) {
        check(drawing) { "Renderer isn't drawing!" }
        drawing = false
        flush(drawType)
    }

    inline fun draw(drawType: Int = GL_TRIANGLES, block: () -> Unit) {
        begin()
        block()
        end(drawType)
    }

    private fun flush(drawType: Int = GL_TRIANGLES) {
        if (numVertices <= 0) return
        vertices.flip()
        program.use()
        vao.bind()

        /* Upload the new vertex data */vbo.bind(GL_ARRAY_BUFFER)
        vbo.uploadSubData(GL_ARRAY_BUFFER, 0, vertices)

        /* Draw batch */glDrawArrays(drawType, 0, numVertices)

        /* Clear vertex data for next batch */vertices.clear()
        numVertices = 0
    }

    private fun checkSpaceInBuffer(remainingFloats: Int, drawType: Int = GL_TRIANGLES) {
        if (vertices.remaining() < remainingFloats) flush(drawType)
    }

    fun drawPoints(vararg points: Point, drawType: Int = GL_TRIANGLES) {
        checkSpaceInBuffer(points.size * 6, drawType)
        for (point in points) {
            vertices.put(point.x).put(point.y)
                .put(point.color.red).put(point.color.green)
                .put(point.color.blue).put(point.color.alpha)
        }
        numVertices += points.size
    }

    private fun setupShaderProgram() {
        vao = VertexArrayObject()
        vao.bind()

        vbo = VertexBufferObject()
        vbo.bind(GL_ARRAY_BUFFER)

        vertices = MemoryUtil.memAllocFloat(4096)

        /* Upload null data to allocate storage for the VBO */
        val size: Long = (vertices.capacity() * java.lang.Float.BYTES).toLong()
        vbo.uploadData(GL_ARRAY_BUFFER, size, GL_DYNAMIC_DRAW)

        val vertexShader = Shader.loadShader(GL_VERTEX_SHADER, "graphics/basic.vert")
        val fragShader = Shader.loadShader(GL_FRAGMENT_SHADER, "graphics/basic.frag")

        program = ShaderProgram()
        program.attachShader(vertexShader)
        program.attachShader(fragShader)
        program.bindFragmentDataLocation(0, "FragColor")
        scaleAttributePosition = program.getAttributeLocation("scale")
        program.link()
        program.use()

        /* Delete linked shaders */vertexShader.delete()
        fragShader.delete()

        /* Specify Vertex Pointers */specifyVertexAttributes()
    }

    private fun specifyVertexAttributes() {
        //val posAttrib = program.getAttributeLocation("position")
        program.enableVertexAttribute(0)// location attribute
        program.pointVertexAttribute(0, 2, 6 * Float.SIZE_BYTES, 0)

        //val colAttrib = program.getAttributeLocation("vertexColor")
        program.enableVertexAttribute(1)// color attribute
        program.pointVertexAttribute(1, 4, 6 * Float.SIZE_BYTES, 2 * Float.SIZE_BYTES)
    }

    private fun setShaderScale() {
        program.use()
        program.setUniform(scaleAttributePosition, Vector2f(xScale, yScale))
    }

    fun setScale(x: Float, y: Float) {
        xScale = x
        yScale = y
        setShaderScale()
    }
}