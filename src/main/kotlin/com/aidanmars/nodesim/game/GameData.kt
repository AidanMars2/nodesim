package com.aidanmars.nodesim.game

import com.aidanmars.nodesim.Project
import java.awt.Point
import java.awt.geom.Point2D

object GameData {
    var project = Project()
    var xLocation = 0
    var yLocation = 0
    var scale = 1.0
    var currentTool = ToolType.interact
    var selectionPoint1 = Point(0, 0)
    var selectionPoint2 = Point(0, 0)
    var isFocused = GameUI.isFocusOwner
    val wasdKeysPressed = BooleanArray(4)// w, a, s, d


    fun getLocationOnScreen(point: Point): Point2D.Double = Point2D.Double(
        ((point.x - xLocation) * scale),
        ((point.y - yLocation) * scale)
    )

    fun getPointOnScreen(point: Point): Point = Point(
        ((point.x - xLocation) * scale).toInt(),
        ((point.y - yLocation) * scale).toInt()
    )

    fun getLocationInWorld(point: Point): Point = Point(
        ((point.x + xLocation) / scale).toInt(),
        ((point.y + yLocation) / scale).toInt()
    )

    fun checkData() {
        if (xLocation > 1_000_000_000) xLocation = 1_000_000_000
        if (xLocation < -1_000_000_000) xLocation = -1_000_000_000
        if (yLocation > 1_000_000_000) xLocation = 1_000_000_000
        if (yLocation < -1_000_000_000) xLocation = -1_000_000_000
        if (scale < 0.1) scale = 0.1
        if (scale > 10.0) scale = 10.0
    }

    fun handleClick(point: Point) {
        when {
            point.distance(GameUI.closeLocation) < 160 -> GameDriver.deInit()
            point.distance(GameUI.interactLocation) < 98 -> currentTool = ToolType.interact
            point.distance(GameUI.placeLocation) < 98 -> currentTool = ToolType.place
            point.distance(GameUI.connectLocation) < 98 -> currentTool = ToolType.connect
            point.distance(GameUI.deleteLocation) < 98 -> currentTool = ToolType.delete
            else -> {
                if (currentTool === ToolType.connect) return
                val locationInWorld = getLocationInWorld(point)

            }
        }
    }
}