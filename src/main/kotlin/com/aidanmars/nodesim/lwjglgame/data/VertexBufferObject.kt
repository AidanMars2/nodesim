package com.aidanmars.nodesim.lwjglgame.data

import org.lwjgl.opengl.GL15
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * This class represents a Vertex Buffer Object (VBO).
 *
 * @author Heiko Brumme
 */
class VertexBufferObject {
    /**
     * Getter for the Vertex Buffer Object ID.
     *
     * @return Handle of the VBO
     */
    /**
     * Stores the handle of the VBO.
     */
    val id = GL15.glGenBuffers()

    /**
     * Binds this VBO with specified target. The target in the tutorial should
     * be `GL_ARRAY_BUFFER` most of the time.
     *
     * @param target Target to bind
     */
    fun bind(target: Int) {
        GL15.glBindBuffer(target, id)
    }

    /**
     * Upload vertex data to this VBO with specified target, data and usage. The
     * target in the tutorial should be `GL_ARRAY_BUFFER` and usage
     * should be `GL_STATIC_DRAW` most of the time.
     *
     * @param target Target to upload
     * @param data   Buffer with the data to upload
     * @param usage  Usage of the data
     */
    fun uploadData(target: Int, data: FloatBuffer, usage: Int) {
        GL15.glBufferData(target, data, usage)
    }

    /**
     * Upload null data to this VBO with specified target, size and usage. The
     * target in the tutorial should be `GL_ARRAY_BUFFER` and usage
     * should be `GL_STATIC_DRAW` most of the time.
     *
     * @param target Target to upload
     * @param size   Size in bytes of the VBO data store
     * @param usage  Usage of the data
     */
    fun uploadData(target: Int, size: Long, usage: Int) {
        GL15.glBufferData(target, size, usage)
    }

    /**
     * Upload sub data to this VBO with specified target, offset and data. The
     * target in the tutorial should be `GL_ARRAY_BUFFER` most of the
     * time.
     *
     * @param target Target to upload
     * @param offset Offset where the data should go in bytes
     * @param data   Buffer with the data to upload
     */
    fun uploadSubData(target: Int, offset: Long, data: FloatBuffer) {
        GL15.glBufferSubData(target, offset, data)
    }

    /**
     * Upload element data to this EBO with specified target, data and usage.
     * The target in the tutorial should be `GL_ELEMENT_ARRAY_BUFFER`
     * and usage should be `GL_STATIC_DRAW` most of the time.
     *
     * @param target Target to upload
     * @param data   Buffer with the data to upload
     * @param usage  Usage of the data
     */
    fun uploadData(target: Int, data: IntBuffer, usage: Int) {
        GL15.glBufferData(target, data, usage)
    }

    /**
     * Deletes this VBO.
     */
    fun delete() {
        GL15.glDeleteBuffers(id)
    }
}
