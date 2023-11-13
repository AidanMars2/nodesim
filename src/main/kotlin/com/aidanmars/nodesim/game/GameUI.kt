package com.aidanmars.nodesim.game

import com.aidanmars.nodesim.*
import java.awt.*
import java.awt.event.*
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import javax.imageio.ImageIO
import javax.swing.JPanel
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.pow

//object GameUI : JPanel(), KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
//    val connect: Image = ImageIO.read(SimObjectDrawer.resourceInputStreamOf("images/button_connect.svg"))
//    val delete: Image = ImageIO.read(SimObjectDrawer.resourceInputStreamOf("images/button_delete.svg"))
//    val interact: Image = ImageIO.read(SimObjectDrawer.resourceInputStreamOf("images/button_interact.svg"))
//    val place: Image = ImageIO.read(SimObjectDrawer.resourceInputStreamOf("images/button_place.svg"))
//    val close: Image = ImageIO.read(SimObjectDrawer.resourceInputStreamOf("images/button_close.svg"))
//    val closeLocation = Point(0, 10)
//    val placeLocation = Point(10, 0)
//    val connectLocation = Point(70, 0)
//    val deleteLocation = Point(190, 0)
//    val interactLocation = Point(130, 0)
//
//    init {
//        preferredSize = Dimension(640, 480)
//        background = Color.LIGHT_GRAY
//    }
//    private fun readResolve(): Any = GameUI
//    override fun keyTyped(e: KeyEvent) {
//        when (e.keyChar) {
//            '1' -> GameData.currentTool = ToolType.place
//            '2' -> GameData.currentTool = ToolType.connect
//            '3' -> GameData.currentTool = ToolType.interact
//            '4' -> GameData.currentTool = ToolType.delete
//            else -> checkPlaceTypeKey(e.keyChar)
//        }
//    }
//
//    private fun checkPlaceTypeKey(key: Char) {
//        when (key) {
//            'e' -> {
//                GameData.currentTool = ToolType.place
//                GameData.currentPlaceType = NodeType.inverter
//            }
//            'r' -> {
//                GameData.currentTool = ToolType.place
//                GameData.currentPlaceType = NodeType.light
//            }
//            't' -> {
//                GameData.currentTool = ToolType.place
//                GameData.currentPlaceType = NodeType.switch
//            }
//        }
//    }
//
//    override fun keyPressed(e: KeyEvent) {
//        when (e.keyChar) {
//            'w' -> GameData.wasdKeysPressed[0] = true
//            'a' -> GameData.wasdKeysPressed[1] = true
//            's' -> GameData.wasdKeysPressed[2] = true
//            'd' -> GameData.wasdKeysPressed[3] = true
//        }
//    }
//
//    override fun keyReleased(e: KeyEvent) {
//        when (e.keyChar) {
//            'w' -> GameData.wasdKeysPressed[0] = false
//            'a' -> GameData.wasdKeysPressed[1] = false
//            's' -> GameData.wasdKeysPressed[2] = false
//            'd' -> GameData.wasdKeysPressed[3] = false
//        }
//    }
//
//    override fun mouseClicked(e: MouseEvent) {}
//
//    override fun mousePressed(e: MouseEvent) {
//        GameData.selectionPoint1.location = getMousePosition()
//        GameData.mousePressed = true
//    }
//
//    override fun mouseReleased(e: MouseEvent) {
//        GameData.selectionPoint2.location = getMousePosition()
//        GameData.mousePressed = false
//        val dragDistance = GameData.selectionPoint1.distance(GameData.selectionPoint2)
//        when {
//            dragDistance < 10.0 -> {
//                GameData.handleClick(GameData.selectionPoint2)
//            }
//            else -> GameData.handleDrag(GameData.selectionPoint1, GameData.selectionPoint2)
//        }
//    }
//
//    override fun mouseEntered(e: MouseEvent) {}
//
//    override fun mouseExited(e: MouseEvent) {}
//
//    override fun mouseDragged(e: MouseEvent) {
//        GameData.selectionPoint2 = getMousePosition()
//    }
//
//    override fun mouseMoved(e: MouseEvent) {}
//
//    override fun mouseWheelMoved(e: MouseWheelEvent) {
//        val scaleScale = 1.1.pow(abs(e.preciseWheelRotation)).toFloat()
//        GameData.scale = if (e.preciseWheelRotation > 0) GameData.scale / scaleScale else GameData.scale * scaleScale
//    }
//
//    override fun paintComponent(g: Graphics) {
//        super.paintComponent(g)
//
//        val g2d = g as? Graphics2D
//        if (g2d !== null) draw(g2d)
//
//        Toolkit.getDefaultToolkit().sync()
//    }
//
//    private fun draw(graphics2D: Graphics2D) {
//        SimObjectDrawer.initScale(GameData.scale)
//        drawBackGround(graphics2D)
//        drawCircuits(graphics2D)
//        drawHUD(graphics2D)
//    }
//
//    private val chunkLineColor = Color(96, 96, 96)
//    private fun drawBackGround(g2d: Graphics2D) {
//        if (GameData.scale < 0.125) return
//
//        val tileSize = GameData.scale * Constants.TILE_SIZE
//        val numTilesX = ceil(size.getWidth() / tileSize).toInt() + 1
//        val numTilesY = ceil(size.getHeight() / tileSize).toInt() + 1
//
//        val originX = -(GameData.xLocation % Constants.TILE_SIZE)
//        val originY = -(GameData.yLocation % Constants.TILE_SIZE)
//        var chunkX = GameData.xLocation / Constants.TILE_SIZE and 15
//        var chunkY = GameData.yLocation / Constants.TILE_SIZE and 15
//
//        val scaledOriginX = originX * GameData.scale
//        val scaledOriginY = originY * GameData.scale
//        val backGroundLineBaseColor = if (GameData.currentTool === ToolType.delete) Color.RED else Color.GRAY
//        val backgroundChunkLineColor = if(GameData.currentTool === ToolType.delete) Color(160, 0, 0) else chunkLineColor
//
//        g2d.color = backGroundLineBaseColor
//        g2d.stroke = BasicStroke((3.0F * GameData.scale))
//
//        var x = scaledOriginX
//        val yMax = size.getHeight().toFloat()
//        for (tileX in 0..numTilesX) {
//            if (chunkX == 0) g2d.color = backgroundChunkLineColor
//            g2d.draw(Line2D.Float(x, 0.0F, x, yMax))
//            x += tileSize
//            if (chunkX == 0) g2d.color = backGroundLineBaseColor
//            chunkX = (chunkX + 1) and 15
//        }
//        var y = scaledOriginY
//        val xMax = size.getWidth().toFloat()
//        for (tileY in 0..numTilesY) {
//            if (chunkY == 0) g2d.color = backgroundChunkLineColor
//            g2d.draw(Line2D.Float(0.0F, y, xMax, y))
//            y += tileSize
//            if (chunkY == 0) g2d.color = backGroundLineBaseColor
//            chunkY = (chunkY + 1) and 15
//        }
//        g2d.color = Color.BLACK
//        g2d.stroke = BasicStroke(5f * GameData.scale)
//        val zeroZeroScreenLocation = GameData.getLocationOnScreen(Point())
//        if (zeroZeroScreenLocation.x in 0F..xMax) {
//            g2d.draw(Line2D.Float(zeroZeroScreenLocation.x, 0F, zeroZeroScreenLocation.x, yMax))
//        }
//        if (zeroZeroScreenLocation.y in 0F..yMax) {
//            g2d.draw(Line2D.Float(0F, zeroZeroScreenLocation.y, xMax, zeroZeroScreenLocation.y))
//        }
//    }
//
//    private fun drawCircuits(g2d: Graphics2D) {
//        val firstChunk = getChunk(GameData.xLocation, GameData.yLocation)
//        val lastChunk = getChunk(
//            GameData.xLocation + (size.width / GameData.scale).toInt(),
//            GameData.yLocation + (size.height / GameData.scale).toInt()
//        )
//        val wiresToDraw = mutableSetOf<Wire>()
//        for (chunkX in firstChunk.x..lastChunk.x) {
//            for (chunkY in firstChunk.y..lastChunk.y) {
//                val chunk = GameData.project.chunks[Point(chunkX, chunkY)]
//                if (chunk === null) continue
//                drawChunk(g2d, chunk, wiresToDraw)
//            }
//        }
//        drawWires(g2d, wiresToDraw)
//    }
//
//    private fun drawChunk(
//        g2d: Graphics2D,
//        chunk: List<Node>,
//        wiresToDraw: MutableSet<Wire>
//    ) {
//        chunk.forEach { node ->
//            val nodeScreenLocation = GameData.getPointOnScreen(Point(node.x, node.y))
//            SimObjectDrawer.drawNode(
//                node.type,
//                node.output,
//                nodeScreenLocation,
//                g2d,
//                this
//            )
//            if (GameData.scale < 0.125) return@forEach
//            drawNodeWires(g2d, node, wiresToDraw)
//        }
//    }
//
//    private fun drawNodeWires(g2d: Graphics2D, node: Node, wiresToDraw: MutableSet<Wire>) {
//        val nodeScreenLocation = GameData.getPointOnScreen(Point(node.x, node.y))
//        wiresToDraw.addAll(node.inputWires)
//        wiresToDraw.addAll(node.inputWires)
//        node.outputWires.forEach {
//            SimObjectDrawer.drawWire(
//                nodeScreenLocation,
//                GameData.getPointOnScreen(Point(it.input.x, it.input.y)),
//                node.output,
//                g2d,
//                this
//            )
//        }
//    }
//
//    private fun drawWires(g2d: Graphics2D, wiresToDraw: MutableSet<Wire>) {
//        wiresToDraw.forEach {
//            SimObjectDrawer.drawWire(
//                GameData.getPointOnScreen(Point(it.input.x, it.input.y)),
//                GameData.getPointOnScreen(Point(it.output.x, it.output.y)),
//                it.input.output,
//                g2d,
//                this
//            )
//        }
//    }
//
//    private fun drawHUD(g2d: Graphics2D) {
//        val yLocation = (size.getHeight() - 49 - 10).toInt()
//        val closeXLocation = (size.getWidth() - 98).toInt()
//        changeHUDLocations(yLocation, closeXLocation)
//        g2d.drawImage(place, placeLocation.x, placeLocation.y, this)
//        g2d.drawImage(connect, connectLocation.x, connectLocation.y, this)
//        g2d.drawImage(interact, interactLocation.x, interactLocation.y, this)
//        g2d.drawImage(delete, deleteLocation.x, deleteLocation.y, this)
//        g2d.drawImage(close, closeLocation.x, closeLocation.y, this)
//        g2d.color = Color.GREEN
//        g2d.stroke = BasicStroke(5F)
//        when (GameData.currentTool) {
//            ToolType.place -> {
//                g2d.drawOval(placeLocation.x - 1, yLocation - 1, 51, 51)
//                drawPlacementNodes(g2d)
//            }
//            ToolType.connect -> g2d.drawOval(connectLocation.x - 1, yLocation - 1, 51, 51)
//            ToolType.interact -> g2d.drawOval(interactLocation.x - 1, yLocation - 1, 51, 51)
//            ToolType.delete -> g2d.drawOval(deleteLocation.x - 1, yLocation - 1, 51, 51)
//        }
//    }
//
//    private fun changeHUDLocations(toolYLocations: Int, closeXLocation: Int) {
//        placeLocation.y = toolYLocations
//        connectLocation.y = toolYLocations
//        interactLocation.y = toolYLocations
//        deleteLocation.y = toolYLocations
//        closeLocation.x = closeXLocation
//    }
//
//    private fun drawPlacementNodes(g2d: Graphics2D) {
//        val nodeYLocations = placeLocation.y - 60
//        val nodeCircle = Ellipse2D.Float(
//            when(GameData.currentPlaceType) {
//                NodeType.inverter -> 10
//                NodeType.switch -> 130
//                NodeType.switchOn -> -200
//                NodeType.light -> 70
//            }.toFloat(),
//            nodeYLocations.toFloat(),
//            50F, 50F
//        )
//        g2d.color = Color.CYAN
//        g2d.stroke = BasicStroke(6F)
//        g2d.fill(nodeCircle)
//        g2d.drawImage(SimObjectDrawer.inverterOn, placeLocation.x - 1, nodeYLocations - 1, this)
//        g2d.drawImage(SimObjectDrawer.lightOff, connectLocation.x + 5, nodeYLocations + 5, this)
//        g2d.drawImage(SimObjectDrawer.switchOff, interactLocation.x + 5, nodeYLocations + 5, this)
//    }
//}