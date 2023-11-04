package com.aidanmars.nodesim.game

import com.aidanmars.nodesim.Constants
import com.aidanmars.nodesim.Node
import com.aidanmars.nodesim.getChunk
import java.awt.*
import java.awt.event.*
import java.awt.geom.Line2D
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.JPanel
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.pow

object GameUI : JPanel(), KeyListener, MouseListener, MouseMotionListener, MouseWheelListener, FocusListener {
    private val connect: Image = ImageIO.read(SimObjectDrawer.resourceInputStreamOf("images/button_connect.svg"))
        .getScaledInstance(2, 2, BufferedImage.SCALE_DEFAULT)
    private val delete: Image = ImageIO.read(SimObjectDrawer.resourceInputStreamOf("images/button_delete.svg"))
        .getScaledInstance(2, 2, BufferedImage.SCALE_DEFAULT)
    private val interact: Image = ImageIO.read(SimObjectDrawer.resourceInputStreamOf("images/button_interact.svg"))
        .getScaledInstance(2, 2, BufferedImage.SCALE_DEFAULT)
    private val place: Image = ImageIO.read(SimObjectDrawer.resourceInputStreamOf("images/button_place.svg"))
        .getScaledInstance(2, 2, BufferedImage.SCALE_DEFAULT)
    private val close: Image = ImageIO.read(SimObjectDrawer.resourceInputStreamOf("images/button_close.svg"))
        .getScaledInstance(2, 2, BufferedImage.SCALE_DEFAULT)
    val closeLocation = Point(0, 10)
    val placeLocation = Point(10, 0)
    val connectLocation = Point(70, 0)
    val deleteLocation = Point(190, 0)
    val interactLocation = Point(130, 0)

    init {
        preferredSize = Dimension(640, 480)
        background = Color.GRAY
    }
    private fun readResolve(): Any = GameUI
    override fun keyTyped(e: KeyEvent) {
        when (e.keyChar) {
            '1' -> GameData.currentTool = ToolType.place
            '2' -> GameData.currentTool = ToolType.connect
            '3' -> GameData.currentTool = ToolType.interact
            '4' -> GameData.currentTool = ToolType.delete
        }
    }

    override fun keyPressed(e: KeyEvent) {
        when (e.keyChar) {
            'w' -> GameData.wasdKeysPressed[0] = true
            'a' -> GameData.wasdKeysPressed[1] = true
            's' -> GameData.wasdKeysPressed[2] = true
            'd' -> GameData.wasdKeysPressed[3] = true
        }
    }

    override fun keyReleased(e: KeyEvent) {
        when (e.keyChar) {
            'w' -> GameData.wasdKeysPressed[0] = false
            'a' -> GameData.wasdKeysPressed[1] = false
            's' -> GameData.wasdKeysPressed[2] = false
            'd' -> GameData.wasdKeysPressed[3] = false
        }
    }

    override fun mouseClicked(e: MouseEvent) {
        GameData.handleClick(e.point)
    }

    override fun mousePressed(e: MouseEvent) {
        GameData.selectionPoint1 = GameData.getLocationInWorld(e.point)
    }

    override fun mouseReleased(e: MouseEvent) {
        GameData.selectionPoint2 = GameData.getLocationInWorld(e.point)
    }

    override fun mouseEntered(e: MouseEvent) {}

    override fun mouseExited(e: MouseEvent) {}

    override fun mouseDragged(e: MouseEvent) {
        GameData.selectionPoint2 = GameData.getLocationInWorld(e.point)
    }

    override fun mouseMoved(e: MouseEvent) {}

    override fun mouseWheelMoved(e: MouseWheelEvent) {
        val scaleScale = 1.1.pow(abs(e.preciseWheelRotation))
        GameData.scale = if (e.preciseWheelRotation < 0) GameData.scale / scaleScale else GameData.scale * scaleScale
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        val g2d = g as? Graphics2D
        if (g2d !== null) draw(g2d)

        Toolkit.getDefaultToolkit().sync()
    }

    private fun draw(graphics2D: Graphics2D) {
        SimObjectDrawer.initScale(GameData.scale)
        drawBackGround(graphics2D)
        drawCircuits(graphics2D)
        drawHUD(graphics2D)
    }

    private fun drawBackGround(g2d: Graphics2D) {
        if (GameData.scale < 0.125) return

        val tileSize = GameData.scale * Constants.TILE_SIZE
        val numTilesX = ceil(size.getWidth() / tileSize).toInt() + 1
        val numTilesY = ceil(size.getHeight() / tileSize).toInt() + 1

        val originX = (GameData.xLocation - (GameData.xLocation % Constants.TILE_SIZE))
        val originY = (GameData.yLocation - (GameData.yLocation % Constants.TILE_SIZE))

        val scaledOriginX = originX * GameData.scale
        val scaledOriginY = originY * GameData.scale

        g2d.color = Color.DARK_GRAY
        g2d.stroke = BasicStroke((3.0F * GameData.scale).toFloat())

        var x = scaledOriginX
        val yMax = size.getHeight()
        for (tileX in 0..numTilesX) {
            g2d.draw(Line2D.Double(x, 0.0, x, yMax))
            x += tileSize
        }
        var y = scaledOriginY
        val xMax = size.getWidth()
        for (tileY in 0..numTilesY) {
            g2d.draw(Line2D.Double(0.0, y, xMax, y))
            y += tileSize
        }
        g2d.color = Color.BLACK
//        val zeroZeroScreenLocation = GameData.getLocationOnScreen(Point())
    }

    private fun drawCircuits(g2d: Graphics2D) {
        val firstChunk = getChunk(GameData.xLocation, GameData.yLocation)
        val lastChunk = getChunk(
            GameData.xLocation + (size.width / GameData.scale).toInt(),
            GameData.yLocation + (size.height / GameData.scale).toInt()
        )
        val wiresDrawn = mutableSetOf<Long>()
        for (chunkX in firstChunk.x..lastChunk.x) {
            for (chunkY in firstChunk.y..lastChunk.y) {
                val chunk = GameData.project.chunks[Point(chunkX, chunkY)]
                if (chunk === null) continue
                drawChunk(g2d, wiresDrawn, chunk)
            }
        }
    }

    private fun drawChunk(
        g2d: Graphics2D,
        drawnWires: MutableSet<Long>,
        chunk: List<Node>
    ) {
        chunk.forEach { node ->
            val nodeScreenLocation = GameData.getPointOnScreen(Point(node.x, node.y))
            SimObjectDrawer.drawNode(
                node.type,
                node.output > 0,
                nodeScreenLocation,
                g2d,
                this
            )
            if (GameData.scale < 0.125) return@forEach
            drawNodeWires(g2d, drawnWires, node)
        }
    }

    private fun drawNodeWires(g2d: Graphics2D, drawnWires: MutableSet<Long>, node: Node) {
        val nodeScreenLocation = GameData.getPointOnScreen(Point(node.x, node.y))
        node.inputWires.forEach {
            if (it.id in drawnWires) return@forEach
            drawnWires.add(it.id)
            SimObjectDrawer.drawWire(
                GameData.getPointOnScreen(Point(it.input.x, it.input.y)),
                nodeScreenLocation,
                it.input.output > 0,
                GameData.scale,
                g2d,
                this
            )
        }
        node.outputWires.forEach {
            if (it.id in drawnWires) return@forEach
            drawnWires.add(it.id)
            SimObjectDrawer.drawWire(
                nodeScreenLocation,
                GameData.getPointOnScreen(Point(it.input.x, it.input.y)),
                node.output > 0,
                GameData.scale,
                g2d,
                this
            )
        }
    }

    private fun drawHUD(g2d: Graphics2D) {
        val yLocation = (size.getHeight() - (49 * 2) + 10).toInt()
        val closeXLocation = (size.getWidth() - (88 * 2) - 10).toInt()
        changeHUDLocations(yLocation, closeXLocation)
        g2d.drawImage(place, placeLocation.x, placeLocation.y, this)
        g2d.drawImage(connect, connectLocation.x, connectLocation.y, this)
        g2d.drawImage(interact, interactLocation.x, interactLocation.y, this)
        g2d.drawImage(delete, deleteLocation.x, deleteLocation.y, this)
        g2d.drawImage(close, closeLocation.x, closeLocation.y, this)
        g2d.color = Color.GREEN
        g2d.stroke = BasicStroke(5F)
        when (GameData.currentTool) {
            ToolType.place -> g2d.drawOval(4, yLocation - 1, 51, 51)
            ToolType.connect -> g2d.drawOval(59, yLocation - 1, 51, 51)
            ToolType.interact -> g2d.drawOval(114, yLocation - 1, 51, 51)
            ToolType.delete -> g2d.drawOval(169, yLocation - 1, 51, 51)
        }
    }

    private fun changeHUDLocations(toolYLocations: Int, closeXLocation: Int) {
        placeLocation.y = toolYLocations
        connectLocation.y = toolYLocations
        interactLocation.y = toolYLocations
        deleteLocation.y = toolYLocations
        closeLocation.x = closeXLocation
    }

    override fun focusGained(e: FocusEvent) {
        if (GameData.isFocused) return
        GameData.isFocused = true
        GameDriver.timer.start()
    }

    override fun focusLost(e: FocusEvent) {
        if (!GameData.isFocused) return
        GameData.isFocused = false
        GameDriver.timer.stop()
    }
}