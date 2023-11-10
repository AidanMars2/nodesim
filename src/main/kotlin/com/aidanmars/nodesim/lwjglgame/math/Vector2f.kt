package com.aidanmars.nodesim.lwjglgame.math

import java.nio.FloatBuffer
import kotlin.math.sqrt

/**
 * This class represents a (x,y)-Vector. GLSL equivalent to vec2.
 *
 * @author Heiko Brumme
 */
class Vector2f {
    var x: Float
    var y: Float

    /**
     * Creates a default 2-tuple vector with all values set to 0.
     */
    constructor() {
        x = 0f
        y = 0f
    }

    /**
     * Creates a 2-tuple vector with specified values.
     *
     * @param x x value
     * @param y y value
     */
    constructor(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    /**
     * Calculates the squared length of the vector.
     *
     * @return Squared length of this vector
     */
    fun lengthSquared(): Float {
        return x * x + y * y
    }

    /**
     * Calculates the length of the vector.
     *
     * @return Length of this vector
     */
    fun length(): Float {
        return sqrt(lengthSquared().toDouble()).toFloat()
    }

    /**
     * Normalizes the vector.
     *
     * @return Normalized vector
     */
    fun normalize(): Vector2f {
        val length = length()
        return divide(length)
    }

    /**
     * Adds this vector to another vector.
     *
     * @param other The other vector
     *
     * @return Sum of this + other
     */
    fun add(other: Vector2f): Vector2f {
        val x = x + other.x
        val y = y + other.y
        return Vector2f(x, y)
    }

    /**
     * Negates this vector.
     *
     * @return Negated vector
     */
    fun negate(): Vector2f {
        return scale(-1f)
    }

    /**
     * Subtracts this vector from another vector.
     *
     * @param other The other vector
     *
     * @return Difference of this - other
     */
    fun subtract(other: Vector2f): Vector2f {
        return add(other.negate())
    }

    /**
     * Multiplies a vector by a scalar.
     *
     * @param scalar Scalar to multiply
     *
     * @return Scalar product of this * scalar
     */
    fun scale(scalar: Float): Vector2f {
        val x = x * scalar
        val y = y * scalar
        return Vector2f(x, y)
    }

    /**
     * Divides a vector by a scalar.
     *
     * @param scalar Scalar to multiply
     *
     * @return Scalar quotient of this / scalar
     */
    fun divide(scalar: Float): Vector2f {
        return scale(1f / scalar)
    }

    /**
     * Calculates the dot product of this vector with another vector.
     *
     * @param other The other vector
     *
     * @return Dot product of this * other
     */
    fun dot(other: Vector2f): Float {
        return x * other.x + y * other.y
    }

    /**
     * Calculates a linear interpolation between this vector with another
     * vector.
     *
     * @param other The other vector
     * @param alpha The alpha value, must be between 0.0 and 1.0
     *
     * @return Linear interpolated vector
     */
    fun lerp(other: Vector2f, alpha: Float): Vector2f {
        return scale(1f - alpha).add(other.scale(alpha))
    }

    /**
     * Stores the vector in a given Buffer.
     *
     * @param buffer The buffer to store the vector data
     */
    fun toBuffer(buffer: FloatBuffer) {
        buffer.put(x).put(y)
        buffer.flip()
    }
}
