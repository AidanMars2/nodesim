package com.aidanmars.nodesim

import com.aidanmars.nodesim.lwjglgame.data.Color
import com.aidanmars.nodesim.lwjglgame.rendering.Renderer
import com.aidanmars.nodesim.lwjglgame.rendering.drawCircle
import com.aidanmars.nodesim.lwjglgame.rendering.drawCircleUnsafe
import com.aidanmars.nodesim.lwjglgame.rendering.fillCircle
import org.lwjgl.opengl.GL32.*

enum class NodeType {
    inverter {
        override fun update(power: Int): Boolean = power == 0

        override fun render(renderer: Renderer, posX: Float, posY: Float, scale: Float, power: Boolean) {
            // glow if powered
            if (power) {
                renderer.fillCircle(posX, posY, 53*scale, inverterOnMainColor, inverterFieldFadeColor)
            }
            // structure circle
            renderer.fillCircle(posX, posY, 44f * scale, nodeStructureColor)
            // main indicator color ring
            renderer.drawCircle(posX, posY, 38.5f * scale, 10f * scale,
                if (power) inverterOnMainColor else inverterOffMainColor)

            renderer.draw(GL_TRIANGLE_STRIP) {
                // border lines
                renderer.drawCircleUnsafe(posX, posY, 44f * scale, 6f * scale, black)
                renderer.drawCircleUnsafe(posX, posY, 33f * scale, 6f * scale, black)
                renderer.drawCircleUnsafe(posX, posY, 22f * scale, 6f * scale, black)
            }
        }
    },
    switch {
        override fun update(power: Int): Boolean = false

        override fun render(renderer: Renderer, posX: Float, posY: Float, scale: Float, power: Boolean) {
            // structure circle
            renderer.fillCircle(posX, posY, 44f * scale, nodeStructureColor)
            // main indicator circle
            renderer.fillCircle(posX, posY, 33f * scale, switchOffColor)

            renderer.draw(GL_TRIANGLE_STRIP) {
                // border lines
                renderer.drawCircleUnsafe(posX, posY, 44f, 6f * scale, black)
                renderer.drawCircleUnsafe(posX, posY, 33f, 6f * scale, black)
            }
        }
    },
    switchOn {
        override fun update(power: Int): Boolean = true

        override fun render(renderer: Renderer, posX: Float, posY: Float, scale: Float, power: Boolean) {
            // glow
            renderer.fillCircle(posX, posY, 53f * scale, red, switchFieldFadeColor)
            // structure circle
            renderer.fillCircle(posX, posY, 44f * scale, nodeStructureColor)
            // main indicator circle
            renderer.fillCircle(posX, posY, 33f * scale, switchOnColor, red)

            renderer.draw(GL_TRIANGLE_STRIP) {
                // border lines
                renderer.drawCircleUnsafe(posX, posY, 44f, 6f * scale, black)
                renderer.drawCircleUnsafe(posX, posY, 33f, 6f * scale, black)
            }
        }
    },
    light {
        override fun update(power: Int): Boolean = power > 0

        override fun render(renderer: Renderer, posX: Float, posY: Float, scale: Float, power: Boolean) {
            if (power) {
                // glow
                renderer.fillCircle(posX, posY, 150f * scale, lightOnMainColor, lightFieldFadeColor)
                // main indicator circle
                renderer.fillCircle(posX, posY, 44f * scale, white, lightOnMainColor)
            } else {
                // main indicator circle
                renderer.fillCircle(posX, posY, 44f * scale, lightOffMainColor)
            }
            // main structure
            renderer.fillCircle(posX, posY, 22f * scale, nodeStructureColor)
            renderer.draw(GL_TRIANGLE_STRIP) {
                // seperator lines
                renderer.drawCircleUnsafe(posX, posY, 44f * scale, 6f * scale, black)
                renderer.drawCircleUnsafe(posX, posY, 22f * scale, 6f * scale, black)
            }
        }
    };

    abstract fun update(power: Int): Boolean
    abstract fun render(renderer: Renderer, posX: Float, posY: Float, scale: Float, power: Boolean)


    protected val nodeStructureColor = Color(0.83f, 0.83f, 0.83f)
    protected val black = Color(0f, 0f, 0f)
    protected val inverterOnMainColor = Color(0f, 1f, 1f)
    protected val inverterFieldFadeColor = inverterOnMainColor.asTransparent()
    protected val inverterOffMainColor = Color(0f, 101f/255, 155f/255)
    protected val lightOffMainColor = Color(0.2f, 0.2f, 0.2f)
    protected val lightOnMainColor = Color(1f, 233f/255, 0f)
    protected val white = Color(1f, 1f, 1f)
    protected val lightFieldFadeColor = lightOnMainColor.asTransparent()
    protected val switchOffColor = Color(188f/255, 0f, 0f)
    protected val switchOnColor = Color(1f, 148f/255, 0f)
    protected val red = Color(1f, 0f, 0f)
    protected val switchFieldFadeColor = red.asTransparent()
}