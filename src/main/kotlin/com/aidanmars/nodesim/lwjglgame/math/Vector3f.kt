package com.aidanmars.nodesim.lwjglgame.math

import java.nio.FloatBuffer
import kotlin.math.sqrt

/**
 * This class represents a (x,y,z)-Vector. GLSL equivalent to vec3.
 *
 * @author Heiko Brumme
 */
class Vector3f {
    var x: Float
    var y: Float
    var z: Float

    /**
     * Creates a default 3-tuple vector with all values set to 0.
     */
    constructor() {
        x = 0f
        y = 0f
        z = 0f
    }

    /**
     * Creates a 3-tuple vector with specified values.
     *
     * @param x x value
     * @param y y value
     * @param z z value
     */
    constructor(x: Float, y: Float, z: Float) {
        this.x = x
        this.y = y
        this.z = z
    }

    /**
     * Calculates the squared length of the vector.
     *
     * @return Squared length of this vector
     */
    fun lengthSquared(): Float {
        return x * x + y * y + z * z
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
    fun normalize(): Vector3f {
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
    fun add(other: Vector3f): Vector3f {
        val x = x + other.x
        val y = y + other.y
        val z = z + other.z
        return Vector3f(x, y, z)
    }

    /**
     * Negates this vector.
     *
     * @return Negated vector
     */
    fun negate(): Vector3f {
        return scale(-1f)
    }

    /**
     * Subtracts this vector from another vector.
     *
     * @param other The other vector
     *
     * @return Difference of this - other
     */
    fun subtract(other: Vector3f): Vector3f {
        return add(other.negate())
    }

    /**
     * Multiplies a vector by a scalar.
     *
     * @param scalar Scalar to multiply
     *
     * @return Scalar product of this * scalar
     */
    fun scale(scalar: Float): Vector3f {
        val x = x * scalar
        val y = y * scalar
        val z = z * scalar
        return Vector3f(x, y, z)
    }

    /**
     * Divides a vector by a scalar.
     *
     * @param scalar Scalar to multiply
     *
     * @return Scalar quotient of this / scalar
     */
    fun divide(scalar: Float): Vector3f {
        return scale(1f / scalar)
    }

    /**
     * Calculates the dot product of this vector with another vector.
     *
     * @param other The other vector
     *
     * @return Dot product of this * other
     */
    fun dot(other: Vector3f): Float {
        return x * other.x + y * other.y + z * other.z
    }

    /**
     * Calculates the dot product of this vector with another vector.
     *
     * @param other The other vector
     *
     * @return Cross product of this x other
     */
    fun cross(other: Vector3f): Vector3f {
        val x = y * other.z - z * other.y
        val y = z * other.x - this.x * other.z
        val z = this.x * other.y - this.y * other.x
        return Vector3f(x, y, z)
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
    fun lerp(other: Vector3f, alpha: Float): Vector3f {
        return scale(1f - alpha).add(other.scale(alpha))
    }

    /**
     * Stores the vector in a given Buffer.
     *
     * @param buffer The buffer to store the vector data
     */
    fun toBuffer(buffer: FloatBuffer) {
        buffer.put(x).put(y).put(z)
        buffer.flip()
    }
}
