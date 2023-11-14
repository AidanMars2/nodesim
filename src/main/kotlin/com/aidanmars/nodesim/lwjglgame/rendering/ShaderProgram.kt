package com.aidanmars.nodesim.lwjglgame.rendering

import com.aidanmars.nodesim.lwjglgame.math.*
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL33
import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryStack

/**
 * This class represents a shader program.
 *
 * @author Heiko Brumme
 */
class ShaderProgram {
    /**
     * Stores the handle of the program.
     */
    private val id: Int

    /**
     * Creates a shader program.
     */
    init {
        id = GL33.glCreateProgram()
    }

    /**
     * Attach a shader to this program.
     *
     * @param shader Shader to get attached
     */
    fun attachShader(shader: Shader) {
        GL33.glAttachShader(id, shader.handle)
    }

    /**
     * Binds the fragment out color variable.
     *
     * @param number Color number you want to bind
     * @param name   Variable name
     */
    fun bindFragmentDataLocation(number: Int, name: CharSequence) {
        GL30.glBindFragDataLocation(id, number, name)
    }

    /**
     * Link this program and check it's status afterwards.
     */
    fun link() {
        GL33.glLinkProgram(id)
        checkStatus()
    }

    /**
     * Gets the location of an attribute variable with specified name.
     *
     * @param name Attribute name
     *
     * @return Location of the attribute
     */
    fun getAttributeLocation(name: CharSequence): Int {
        return GL33.glGetAttribLocation(id, name)
    }

    /**
     * Enables a vertex attribute.
     *
     * @param location Location of the vertex attribute
     */
    fun enableVertexAttribute(location: Int) {
        GL33.glEnableVertexAttribArray(location)
    }

    /**
     * Disables a vertex attribute.
     *
     * @param location Location of the vertex attribute
     */
    fun disableVertexAttribute(location: Int) {
        GL33.glDisableVertexAttribArray(location)
    }

    /**
     * Sets the vertex attribute pointer.
     *
     * @param location Location of the vertex attribute
     * @param size     Number of values per vertex
     * @param stride   Offset between consecutive generic vertex attributes in
     * bytes
     * @param offset   Offset of the first component of the first generic vertex
     * attribute in bytes
     */
    fun pointVertexAttribute(location: Int, size: Int, stride: Int, offset: Int) {
        GL33.glVertexAttribPointer(location, size, GL11.GL_FLOAT, false, stride, offset.toLong())
    }

    /**
     * Gets the location of an uniform variable with specified name.
     *
     * @param name Uniform name
     *
     * @return Location of the uniform
     */
    fun getUniformLocation(name: CharSequence): Int {
        return GL33.glGetUniformLocation(id, name)
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value    Value to set
     */
    fun setUniform(location: Int, value: Int) {
        GL33.glUniform1i(location, value)
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value    Value to set
     */
    fun setUniform(location: Int, value: Vector2f) {
        MemoryStack.stackPush().use { stack ->
            val buffer = stack.mallocFloat(2)
            value.toBuffer(buffer)
            GL33.glUniform2fv(location, buffer)
        }
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value    Value to set
     */
    fun setUniform(location: Int, value: Vector3f) {
        MemoryStack.stackPush().use { stack ->
            val buffer = stack.mallocFloat(3)
            value.toBuffer(buffer)
            GL33.glUniform3fv(location, buffer)
        }
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value    Value to set
     */
    fun setUniform(location: Int, value: Vector4f) {
        MemoryStack.stackPush().use { stack ->
            val buffer = stack.mallocFloat(4)
            value.toBuffer(buffer)
            GL33.glUniform4fv(location, buffer)
        }
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value    Value to set
     */
    fun setUniform(location: Int, value: Matrix2f) {
        MemoryStack.stackPush().use { stack ->
            val buffer = stack.mallocFloat(2 * 2)
            value.toBuffer(buffer)
            GL33.glUniformMatrix2fv(location, false, buffer)
        }
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value    Value to set
     */
    fun setUniform(location: Int, value: Matrix3f) {
        MemoryStack.stackPush().use { stack ->
            val buffer = stack.mallocFloat(3 * 3)
            value.toBuffer(buffer)
            GL33.glUniformMatrix3fv(location, false, buffer)
        }
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value    Value to set
     */
    fun setUniform(location: Int, value: Matrix4f) {
        MemoryStack.stackPush().use { stack ->
            val buffer = stack.mallocFloat(4 * 4)
            value.toBuffer(buffer)
            GL33.glUniformMatrix4fv(location, false, buffer)
        }
    }

    /**
     * Use this shader program.
     */
    fun use() {
        GL33.glUseProgram(id)
    }

    /**
     * Checks if the program was linked successfully.
     */
    fun checkStatus() {
        val status = GL33.glGetProgrami(id, GL33.GL_LINK_STATUS)
        if (status != GL11.GL_TRUE) {
            throw RuntimeException(GL33.glGetProgramInfoLog(id))
        }
    }

    /**
     * Deletes the shader program.
     */
    fun delete() {
        GL33.glDeleteProgram(id)
    }
}
