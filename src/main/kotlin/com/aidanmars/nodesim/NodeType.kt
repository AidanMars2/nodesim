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
                renderer.fillCircle(posX, posY, 26.5f*scale, inverterOnMainColor, inverterFieldFadeColor)
            }
            // structure circle
            renderer.fillCircle(posX, posY, 22f * scale, nodeStructureColor)
            // main indicator color ring
            renderer.drawCircle(posX, posY, 19.25f * scale, 10f * scale,
                if (power) inverterOnMainColor else inverterOffMainColor)

            // border lines
            renderer.drawCircle(posX, posY, 22f * scale, 6f * scale, black)
            renderer.drawCircle(posX, posY, 16.5f * scale, 6f * scale, black)
            renderer.drawCircle(posX, posY, 11f * scale, 6f * scale, black)
        }
    },
    switch {
        override fun update(power: Int): Boolean = false

        override fun render(renderer: Renderer, posX: Float, posY: Float, scale: Float, power: Boolean) {
            // structure circle
            renderer.fillCircle(posX, posY, 22f * scale, nodeStructureColor)
            // main indicator circle
            renderer.fillCircle(posX, posY, 16.5f * scale, switchOffColor)

            // border lines
            renderer.drawCircle(posX, posY, 22f, 6f * scale, black)
            renderer.drawCircle(posX, posY, 16.5f, 6f * scale, black)
        }
    },
    switchOn {
        override fun update(power: Int): Boolean = true

        override fun render(renderer: Renderer, posX: Float, posY: Float, scale: Float, power: Boolean) {
            // glow
            renderer.fillCircle(posX, posY, 27f * scale, red, switchFieldFadeColor)
            // structure circle
            renderer.fillCircle(posX, posY, 22f * scale, nodeStructureColor)
            // main indicator circle
            renderer.fillCircle(posX, posY, 16.5f * scale, switchOnColor, red)

            // border lines
            renderer.drawCircleUnsafe(posX, posY, 22f, 6f * scale, black)
            renderer.drawCircleUnsafe(posX, posY, 16.5f, 6f * scale, black)
        }
    },
    light {
        override fun update(power: Int): Boolean = power > 0

        override fun render(renderer: Renderer, posX: Float, posY: Float, scale: Float, power: Boolean) {
            if (power) {
                // glow
                renderer.fillCircle(posX, posY, 75f * scale, lightOnMainColor, lightFieldFadeColor)
                // main indicator circle
                renderer.fillCircle(posX, posY, 22f * scale, white, lightOnMainColor)
            } else {
                // main indicator circle
                renderer.fillCircle(posX, posY, 22f * scale, lightOffMainColor)
            }
            // main structure
            renderer.fillCircle(posX, posY, 11f * scale, nodeStructureColor)
            // seperator lines
            renderer.drawCircleUnsafe(posX, posY, 22f * scale, 6f * scale, black)
            renderer.drawCircleUnsafe(posX, posY, 11f * scale, 6f * scale, black)
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