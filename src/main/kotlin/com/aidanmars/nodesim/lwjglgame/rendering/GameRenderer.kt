package com.aidanmars.nodesim.lwjglgame.rendering

import com.aidanmars.nodesim.Node
import com.aidanmars.nodesim.Wire
import com.aidanmars.nodesim.WorldLocation
import com.aidanmars.nodesim.getChunk
import com.aidanmars.nodesim.lwjglgame.GameData
import com.aidanmars.nodesim.lwjglgame.ToolType
import com.aidanmars.nodesim.lwjglgame.angleBetweenPoints
import com.aidanmars.nodesim.lwjglgame.data.Color
import com.aidanmars.nodesim.lwjglgame.data.Location
import com.aidanmars.nodesim.lwjglgame.data.Point
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class GameRenderer(
    val renderer: Renderer,
    val state: GameData,
) {
    fun init() {
        state.registerHUDElement(placeElement)
        state.registerHUDElement(connectElement)
        state.registerHUDElement(interactElement)
        state.registerHUDElement(deleteElement)
        state.registerHUDElement(inverterElement)
        state.registerHUDElement(lightElement)
        state.registerHUDElement(switchElement)
        state.registerHUDElement(closeElement)
    }

    fun render(windowWidth: Int, windowHeight: Int) {
        val halfWidth = (windowWidth shr 1).toFloat()
        val halfHeight = (windowHeight shr 1).toFloat()
        renderer.setScale(1 / halfWidth, 1 / halfHeight)
        drawBackGround(
            state.scale,
            halfWidth, halfHeight
        )
        drawCircuits(halfWidth, halfHeight)

        // TODO: draw hud
    }

    private fun drawCircuits(
        halfWidth: Float, halfHeight: Float
    ) {
        val halfGameWidth = (halfWidth / state.scale).toInt()
        val halfGameHeight = (halfHeight / state.scale).toInt()
        val bottomLeftGameX = state.playerXLocation - halfGameWidth
        val bottomLeftGameY = state.playerYLocation - halfGameHeight
        val topRightGameX = state.playerXLocation + halfGameWidth
        val topRightGameY = state.playerYLocation + halfGameHeight
        val bottomLeftChunk = getChunk(bottomLeftGameX, bottomLeftGameY)
        val topRightChunk = getChunk(topRightGameX, topRightGameY)
        val wiresToDraw = mutableSetOf<Wire>()
        for (chunkX in bottomLeftChunk.x..topRightChunk.x) {
            for (chunkY in bottomLeftChunk.y..topRightChunk.y) {
                val chunk = state.project.chunks[WorldLocation(chunkX, chunkY)]
                if (chunk === null) continue

                renderChunk(chunk, wiresToDraw)
            }
        }
        drawWires(wiresToDraw)
    }

    private fun renderChunk(
        chunk: List<Node>, wiresToDraw: MutableSet<Wire>
    ) {
        chunk.forEach {
            val screenLocation = state.getScreenLocation(it.x, it.y)
            it.type.render(
                renderer,
                screenLocation.x, screenLocation.y,
                state.scale,
                it.output
            )
            wiresToDraw.addAll(it.inputWires)
            wiresToDraw.addAll(it.outputWires)
        }
    }

    private fun drawWires(
        wiresToDraw: MutableSet<Wire>
    ) {
        wiresToDraw.forEach {
            val inputDrawPoint = state.getScreenLocation(it.input.x, it.input.y)
            val outputDrawPoint = state.getScreenLocation(it.output.x, it.output.y)
            renderer.drawLine(
                Point(inputDrawPoint.x, inputDrawPoint.y, black),
                Point(outputDrawPoint.y, outputDrawPoint.x, black),
                10f * state.scale, true
            )
            drawWireTriangle(inputDrawPoint, outputDrawPoint, it.input.output)
            val wireColor = if (it.input.output) wireOnColor else wireOffColor
            renderer.drawLine(
                Point(inputDrawPoint.x, inputDrawPoint.y, wireColor),
                Point(outputDrawPoint.y, outputDrawPoint.x, wireColor),
                8f * state.scale, true
            )
        }
    }

    private fun drawWireTriangle(
        inputDrawPoint: Location,
        outputDrawPoint: Location,
        power: Boolean
    ) {
        val triangleLocation = inputDrawPoint.middle(outputDrawPoint)
        val angle = angleBetweenPoints(
            inputDrawPoint.x, inputDrawPoint.y,
            outputDrawPoint.x, outputDrawPoint.y
        )
        val mainSin = sin(angle) * state.scale
        val mainCos = cos(angle) * state.scale
        val firstBackCornerSin = sin(angle + triangleAngle) * state.scale
        val firstBackCornerCos = cos(angle + triangleAngle) * state.scale
        val secondBackCornerSin = sin(angle - triangleAngle) * state.scale
        val secondBackCornerCos = cos(angle - triangleAngle) * state.scale

        val outerMainPoint = Point(
            mainCos * outerTriangleDistance + triangleLocation.x,
            mainSin * outerTriangleDistance + triangleLocation.y, black)
        val outerFirstBackPoint = Point(
            firstBackCornerCos * outerTriangleDistance + triangleLocation.x,
            firstBackCornerSin * outerTriangleDistance + triangleLocation.y, black)
        val outerSecondBackPoint = Point(
            secondBackCornerCos * outerTriangleDistance + triangleLocation.x,
            secondBackCornerSin * outerTriangleDistance + triangleLocation.y, black)
        renderer.draw { renderer.drawPoints(outerFirstBackPoint, outerMainPoint, outerSecondBackPoint) }

        val wireColor = if (power) wireOnColor else wireOffColor
        val innerMainPoint = Point(
            mainCos * innerTriangleDistance + triangleLocation.x,
            mainSin * innerTriangleDistance + triangleLocation.y, wireColor)
        val innerFirstBackPoint = Point(
            firstBackCornerCos * innerTriangleDistance + triangleLocation.x,
            firstBackCornerSin * innerTriangleDistance + triangleLocation.y, wireColor)
        val innerSecondBackPoint = Point(
            secondBackCornerCos * innerTriangleDistance + triangleLocation.x,
            secondBackCornerSin * innerTriangleDistance + triangleLocation.y, wireColor)
        renderer.draw { renderer.drawPoints(innerMainPoint, innerFirstBackPoint, innerSecondBackPoint) }
    }

    private val innerTriangleDistance = 20f
    private val outerTriangleDistance = 25f
    private val triangleAngle = (PI - 1).toFloat()

    private val black = Color(0f, 0f, 0f)
    private val wireOffColor = Color(0.2f, 0.2f, 0.2f)
    private val wireOnColor = Color(234f/255, 215f/255, 0f)

    /**
     * draw lines of background
     */
    private fun drawBackGround(
        scale: Float, halfWidth: Float, halfHeight: Float
    ) {
        val halfGameWidth = (halfWidth / scale).toInt()
        val halfGameHeight = (halfHeight / scale).toInt()
        val bottomLeftGameX = state.playerXLocation - halfGameWidth
        val bottomLeftGameY = state.playerYLocation - halfGameHeight
        val bottomLeftModX40 = bottomLeftGameX % 40
        val bottomLeftModY40 = bottomLeftGameY % 40
        val lineBeginX = bottomLeftGameX - bottomLeftModX40
        val lineBeginY = bottomLeftGameY - bottomLeftModY40
        val numOfLinesX = ((halfGameWidth * 2) / 40) + 1
        val numOfLinesY = ((halfGameHeight * 2) / 40) + 1
        val chunkLines = mutableListOf<Pair<Location, Location>>()
        val zeroZeroLines = mutableListOf<Pair<Location, Location>>()
        val currentLineColor = if (state.currentTool == ToolType.delete) deleteLineColor else lineColor
        for (lineX in 0..numOfLinesX) {
            val gameXLocation = lineBeginX + (lineX * 40)
            val drawXLocation = state.getScreenLocation(gameXLocation, 0).x
            when {
                gameXLocation == 0 -> {
                    zeroZeroLines.add(Location(drawXLocation, -halfHeight) to Location(drawXLocation, halfHeight))
                    continue
                }
                gameXLocation % 160 == 0 -> {
                    chunkLines.add(Location(drawXLocation, -halfHeight) to Location(drawXLocation, halfHeight))
                    continue
                }
            }
            renderer.drawLine(
                Point(drawXLocation, -halfHeight, currentLineColor),
                Point(drawXLocation, halfHeight, currentLineColor),
                5f, false
            )
        }
        for (lineY in 0..numOfLinesY) {
            val gameYLocation = lineBeginY + (lineY * 40)
            val drawYLocation = state.getScreenLocation(gameYLocation, 0).y
            when {
                gameYLocation == 0 -> {
                    zeroZeroLines.add(Location(-halfWidth, drawYLocation) to Location(halfWidth, drawYLocation))
                    continue
                }
                gameYLocation % 160 == 0 -> {
                    chunkLines.add(Location(-halfWidth, drawYLocation) to Location(halfWidth, drawYLocation))
                    continue
                }
            }
            renderer.drawLine(
                Point(-halfWidth, drawYLocation, currentLineColor),
                Point(halfWidth, drawYLocation, currentLineColor),
                5f, false
            )
        }
        chunkLines.forEach {
            renderer.drawLine(
                Point(it.first.x, it.first.y, chunkLineColor),
                Point(it.second.x, it.second.y, chunkLineColor),
                5f, false
            )
        }
        zeroZeroLines.forEach {
            renderer.drawLine(
                Point(it.first.x, it.first.y, zeroZeroLineColor),
                Point(it.second.x, it.second.y, zeroZeroLineColor),
                7f, false
            )
        }
    }

    private val deleteLineColor = Color(1f, 0f, 0f)
    private val lineColor = Color(0.5f, 0.5f, 0.5f)
    private val zeroZeroLineColor = Color(0f, 0f, 0f)
    private val chunkLineColor = Color(0.25f, 0.25f, 0.25f)
}