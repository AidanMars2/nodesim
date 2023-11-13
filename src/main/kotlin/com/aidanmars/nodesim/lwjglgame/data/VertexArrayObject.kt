package com.aidanmars.nodesim.lwjglgame.data

import org.lwjgl.opengl.GL30

/**
 * This class represents a Vertex Array Object (VAO).
 *
 * @author Heiko Brumme
 */
class VertexArrayObject {
    /**
     * Getter for the Vertex Array Object ID.
     *
     * @return Handle of the VAO
     */
    /**
     * Stores the handle of the VAO.
     */
    val id: Int = GL30.glGenVertexArrays()

    /**
     * Binds the VAO.
     */
    fun bind() {
        GL30.glBindVertexArray(id)
    }

    /**
     * Deletes the VAO.
     */
    fun delete() {
        GL30.glDeleteVertexArrays(id)
    }
}
