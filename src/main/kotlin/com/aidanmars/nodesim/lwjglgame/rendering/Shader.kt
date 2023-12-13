package com.aidanmars.nodesim.lwjglgame.rendering

import com.aidanmars.nodesim.lwjglgame.resourceInputStreamOf
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


/**
 * This class represents a shader.
 *
 * @author Heiko Brumme
 */
class Shader(type: Int) {
    /**
     * Stores the handle of the shader.
     */
    var handle = 0

    init {
        handle = GL20.glCreateShader(type)
    }

    /**
     * Sets the source code of this shader.
     *
     * @param source GLSL Source Code for the shader
     */
    fun source(source: CharSequence) {
        GL20.glShaderSource(handle, source)
    }

    /** Compiles the shader and checks its status afterwards.  */
    fun compile() {
        GL20.glCompileShader(handle)
        checkStatus()
    }

    /**
     * Checks if the shader was compiled successfully.
     */
    private fun checkStatus() {
        val status = GL20.glGetShaderi(handle, GL20.GL_COMPILE_STATUS)
        if (status != GL11.GL_TRUE) {
            throw RuntimeException(GL20.glGetShaderInfoLog(handle))
        }
    }

    /**
     * Deletes the shader.
     */
    fun delete() {
        GL20.glDeleteShader(handle)
    }

    companion object {
        /**
         * Creates a shader with specified type and source and compiles it. The type
         * in the tutorial should be either `GL_VERTEX_SHADER` or
         * `GL_FRAGMENT_SHADER`.
         *
         * @param type   Type of the shader
         * @param source Source of the shader
         *
         * @return Compiled Shader from the specified source
         */
        fun createShader(type: Int, source: CharSequence): Shader {
            val shader = Shader(type)
            shader.source(source)
            shader.compile()
            return shader
        }

        /**
         * Loads a shader from a file.
         *
         * @param type Type of the shader
         * @param path File path of the shader
         *
         * @return Compiled Shader from specified file
         */
        fun loadShader(type: Int, path: String): Shader {
            val builder = StringBuilder()
            try {
                resourceInputStreamOf(path)!!.use { `in` ->
                    BufferedReader(InputStreamReader(`in`)).use { reader ->
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            builder.append(line).append("\n")
                        }
                    }
                }
            } catch (ex: IOException) {
                throw RuntimeException(
                    "Failed to load a shader file!"
                            + System.lineSeparator() + ex.message
                )
            }
            val source: CharSequence = builder.toString()
            return createShader(type, source)
        }
    }
}