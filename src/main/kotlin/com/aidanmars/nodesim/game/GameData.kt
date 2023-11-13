package com.aidanmars.nodesim.game

//import com.aidanmars.nodesim.NodeType
//import com.aidanmars.nodesim.Project
//import java.awt.Point
//import java.awt.geom.Point2D
//import java.util.concurrent.LinkedBlockingQueue
//
//object GameData {
//    var project = Project()
//    var xLocation = -320
//    var yLocation = -240
//    var scale = 1.0F
//    var currentTool = ToolType.interact
//    var currentPlaceType = NodeType.switch
//    var selectionPoint1 = Point()
//    var selectionPoint2 = Point()
//    var isFocused = GameUI.isFocusOwner
//    val wasdKeysPressed = BooleanArray(4)// w, a, s, d
//    var mousePressed = false
//    val clickQueue = LinkedBlockingQueue<Input>()
//
//
//    fun getLocationOnScreen(point: Point): Point2D.Float = Point2D.Float(
//        ((point.x - xLocation) * scale),
//        ((point.y - yLocation) * scale)
//    )
//
//    fun getPointOnScreen(point: Point): Point = Point(
//        ((point.x - xLocation) * scale).toInt(),
//        ((point.y - yLocation) * scale).toInt()
//    )
//
//    fun getLocationInWorld(point: Point): Point = Point(
//        ((point.x / scale).toInt() + xLocation),
//        ((point.y / scale).toInt() + yLocation)
//    )
//
//    fun checkData() {
//        if (xLocation > 1_000_000_000) xLocation = 1_000_000_000
//        if (xLocation < -1_000_000_000) xLocation = -1_000_000_000
//        if (yLocation > 1_000_000_000) xLocation = 1_000_000_000
//        if (yLocation < -1_000_000_000) xLocation = -1_000_000_000
//        if (scale < 0.1) scale = 0.1F
//        if (scale > 10.0) scale = 10.0F
//        if (wasdKeysPressed[0]) yLocation -= (6 / scale).toInt()
//        if (wasdKeysPressed[1]) xLocation -= (6 / scale).toInt()
//        if (wasdKeysPressed[2]) yLocation += (6 / scale).toInt()
//        if (wasdKeysPressed[3]) xLocation += (6 / scale).toInt()
//    }
//
//    fun handleClick(point: Point) {
//        when {
//            point.distance(GameUI.closeLocation.asMiddleOf(GameUI.close)) <= 44 -> GameDriver.deInit()
//            point.distance(GameUI.interactLocation.asMiddleOf(GameUI.interact)) <= 22 -> currentTool = ToolType.interact
//            point.distance(GameUI.placeLocation.asMiddleOf(GameUI.place)) <= 22 -> currentTool = ToolType.place
//            point.distance(GameUI.connectLocation.asMiddleOf(GameUI.connect)) <= 22 -> currentTool = ToolType.connect
//            point.distance(GameUI.deleteLocation.asMiddleOf(GameUI.delete)) <= 22 -> currentTool = ToolType.delete
//            else -> {
//                clickQueue.put(Input(Input.InputType.click, Point(), getLocationInWorld(point)))
//            }
//        }
//    }
//
//    fun handleDrag(from: Point, to: Point) {
//        clickQueue.put(Input(Input.InputType.drag, getLocationInWorld(from), getLocationInWorld(to)))
//    }
//}