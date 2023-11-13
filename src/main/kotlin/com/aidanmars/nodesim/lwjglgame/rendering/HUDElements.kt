package com.aidanmars.nodesim.lwjglgame.rendering

import com.aidanmars.nodesim.NodeType
import com.aidanmars.nodesim.lwjglgame.Game
import com.aidanmars.nodesim.lwjglgame.ToolType
import com.aidanmars.nodesim.lwjglgame.data.Color
import com.aidanmars.nodesim.lwjglgame.data.Location
import com.aidanmars.nodesim.lwjglgame.data.Point
import com.aidanmars.nodesim.lwjglgame.toRenderScreenLocation
import kotlin.math.sqrt

val GameRenderer.placeElement
    get() = HUDElement(
        {
            it.distance(Location(30f, state.height - 30f)) < 25f
        }, {
            state.currentTool = ToolType.place
        }, {
            val drawLocation = toRenderScreenLocation(
                Location(30f, state.height - 30f),
                state.width, state.height
            )
            if (state.currentTool === ToolType.place) it.drawCircle(
                drawLocation.x, drawLocation.y,
                24f, 15f,
                placeElementGreen, placeElementFadeColor
            )
            it.fillCircle(drawLocation.x, drawLocation.y, HUDElementRadius, placeElementGreen)
            it.drawCircle(drawLocation.x, drawLocation.y, HUDElementRadius, borderWidth, placeElementBorder)
            it.drawLine(
                Point(drawLocation.x - 15f, drawLocation.y, black),
                Point(drawLocation.x + 15f, drawLocation.y, black),
                8f, true
            )
            it.drawLine(
                Point(drawLocation.x, drawLocation.y - 15f, black),
                Point(drawLocation.x, drawLocation.y + 15f, black),
                8f, true
            )
        }
    )

private val placeElementGreen = Color(0f, 0.7f, 0f)
private val placeElementBorder = Color(0f, 0.6f, 0.1f)
private val placeElementFadeColor = placeElementGreen.asTransparent()
private val black = Color(0f, 0f, 0f)
val HALF_SQRT_2 = sqrt(2.0) * 0.5f
private const val borderWidth = 5f
private const val HUDElementRadius = 22f

val GameRenderer.connectElement
    get() = HUDElement(
        {
            it.distance(Location(60f, state.height - 30f)) < 25f
        }, {
            state.currentTool = ToolType.connect
        }, {
            val drawLocation = toRenderScreenLocation(
                Location(30f, state.height - 30f),
                state.width, state.height
            )
            if (state.currentTool === ToolType.connect) it.drawCircle(
                drawLocation.x, drawLocation.y,
                24f, 15f,
                connectElementGray, connectElementFadeColor
            )
            it.fillCircle(drawLocation.x, drawLocation.y, HUDElementRadius, connectElementGray)
            it.drawCircle(drawLocation.x, drawLocation.y, HUDElementRadius, borderWidth, connectElementBorder)
            it.drawLine(
                Point(drawLocation.x - connectElementDiagonalOffset, drawLocation.y - connectElementDiagonalOffset, white),
                Point(drawLocation.x + connectElementDiagonalOffset, drawLocation.y + connectElementDiagonalOffset, white),
                4f, false
            )
            it.drawCircle(
                drawLocation.x - connectElementDiagonalOffset,
                drawLocation.y - connectElementDiagonalOffset,
                5f, 1f, black
            )
            it.drawCircle(
                drawLocation.x + connectElementDiagonalOffset,
                drawLocation.y + connectElementDiagonalOffset,
                5f, 1f, black
            )
        }
    )

private val connectElementGray = Color(0.5f, 0.5f, 0.5f)
private val connectElementBorder = Color(0.25f, 0.25f, 0.25f)
private val connectElementFadeColor = connectElementGray.asTransparent()
private val connectElementDiagonalOffset = HALF_SQRT_2.toFloat() * 15f

val GameRenderer.interactElement
    get() = HUDElement(
        {
            it.distance(Location(90f, state.height - 30f)) < 25f
        }, {
            state.currentTool = ToolType.interact
        }, {
            val drawLocation = toRenderScreenLocation(
                Location(30f, state.height - 30f),
                state.width, state.height
            )
            if (state.currentTool === ToolType.interact) it.drawCircle(
                drawLocation.x, drawLocation.y,
                24f, 15f,
                connectElementGray, connectElementFadeColor
            )
            it.fillCircle(drawLocation.x, drawLocation.y, HUDElementRadius, interactElementBlue)
            it.drawCircle(drawLocation.x, drawLocation.y, HUDElementRadius, borderWidth, interactElementBorder)
            it.fillCircle(drawLocation.x, drawLocation.y, HUDElementRadius * 0.4f, white)
            it.draw { it.drawPoints(
                Point(drawLocation.x - 3f, drawLocation.y + 5f, interactElementBlue),
                Point(drawLocation.x - 3f, drawLocation.y - 15f, interactElementBlue),
                Point(drawLocation.x + 12f, drawLocation.y - 12f, interactElementBlue)
            ) }
            it.draw { it.drawPoints(
                Point(drawLocation.x, drawLocation.y, white),
                Point(drawLocation.x, drawLocation.y - 15f, white),
                Point(drawLocation.x + 9f, drawLocation.y - 12f, white)
            ) }
        }
    )

private val interactElementBlue = Color(76f/255, 0.8f, 1f)
private val interactElementBorder = interactElementBlue / 2f

val GameRenderer.deleteElement
    get() = HUDElement(
        {
            it.distance(Location(120f, state.height - 30f)) < 25f
        }, {
            state.currentTool = ToolType.delete
        }, {
            val drawLocation = toRenderScreenLocation(
                Location(state.height - 60f, 60f),
                state.width, state.height
            )
            if (state.currentTool === ToolType.delete) it.drawCircle(
                drawLocation.x, drawLocation.y,
                24f, 15f,
                deleteElementRed, deleteElementFadeColor
            )
            it.fillCircle(drawLocation.x, drawLocation.y, HUDElementRadius, deleteElementRed)
            it.drawCircle(drawLocation.x, drawLocation.y, HUDElementRadius, borderWidth, deleteElementBorder)
            it.drawLine(
                Point(drawLocation.x - deleteElementLineOffset, drawLocation.y - deleteElementLineOffset, white),
                Point(drawLocation.x + deleteElementLineOffset, drawLocation.y + deleteElementLineOffset, white),
                7f, true
            )
            it.drawLine(
                Point(drawLocation.x - deleteElementLineOffset, drawLocation.y + deleteElementLineOffset, white),
                Point(drawLocation.x + deleteElementLineOffset, drawLocation.y - deleteElementLineOffset, white),
                7f, true
            )
        }
    )

private val white = Color(1f, 1f, 1f)
private val deleteElementRed = Color(0.8f, 0.2f, 0.2f)
private val deleteElementBorder = Color(0.4f, 0.1f, 0.1f)
private val deleteElementFadeColor = deleteElementRed.asTransparent()
private val deleteElementLineOffset = HALF_SQRT_2.toFloat() * 18f

val GameRenderer.inverterElement
    get() = HUDElement(
        {
            it.distance(Location(30f, state.height - 60f)) < 25f && state.currentTool == ToolType.place
        }, {
            state.currentPlaceType = NodeType.inverter
        }, {
            if (state.currentTool !== ToolType.place) return@HUDElement
            val drawLocation = toRenderScreenLocation(
                Location(30f, state.height - 60f),
                state.width, state.height
            )
            NodeType.inverter.render(it, drawLocation.x, drawLocation.y, 1f, true)
        }
    )

val GameRenderer.lightElement
    get() = HUDElement(
        {
            it.distance(Location(60f, state.height - 60f)) < 25f && state.currentTool == ToolType.place
        }, {
            state.currentPlaceType = NodeType.light
        }, {
            if (state.currentTool !== ToolType.place) return@HUDElement
            val drawLocation = toRenderScreenLocation(
                Location(60f, state.height - 60f),
                state.width, state.height
            )
            NodeType.light.render(it, drawLocation.x, drawLocation.y, 1f, false)
        }
    )

val GameRenderer.switchElement
    get() = HUDElement(
        {
            it.distance(Location(90f, state.height - 60f)) < 25f && state.currentTool == ToolType.place
        }, {
            state.currentPlaceType = NodeType.switch
        }, {
            if (state.currentTool !== ToolType.place) return@HUDElement
            val drawLocation = toRenderScreenLocation(
                Location(90f, state.height - 60f),
                state.width, state.height
            )
            NodeType.switchOn.render(it, drawLocation.x, drawLocation.y, 1f, true)
        }
    )

val GameRenderer.closeElement
    get() = HUDElement(
        {
            it.distance(Location(state.height - 60f, 60f)) < 50f
        }, {
            Game.end()
        }, {
            val drawLocation = toRenderScreenLocation(
                Location(state.height - 60f, 60f),
                state.width, state.height
            )
            it.drawLine(
                Point(drawLocation.x - closeDiagonalLineOffset, drawLocation.y - closeDiagonalLineOffset, closeColor),
                Point(drawLocation.x + closeDiagonalLineOffset, drawLocation.y + closeDiagonalLineOffset, closeColor),
                10f, true
            )
            it.drawLine(
                Point(drawLocation.x - closeDiagonalLineOffset, drawLocation.y + closeDiagonalLineOffset, closeColor),
                Point(drawLocation.x + closeDiagonalLineOffset, drawLocation.y - closeDiagonalLineOffset, closeColor),
                10f, true
            )
        }
    )

private val closeDiagonalLineOffset = HALF_SQRT_2.toFloat() * 47f
private val closeColor = Color(0.25f, 0.25f, 0.25f)