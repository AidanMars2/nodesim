package com.aidanmars.nodesim.game

import com.aidanmars.nodesim.NodeType
import java.awt.BasicStroke
import java.awt.BasicStroke.CAP_ROUND
import java.awt.BasicStroke.JOIN_ROUND
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Point
import java.awt.geom.AffineTransform
import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.awt.image.ImageObserver
import java.io.InputStream
import javax.imageio.ImageIO


object SimObjectDrawer {
    private val wireSectionOff: BufferedImage = ImageIO.read(resourceInputStreamOf("images/arrow_off.svg"))
    private val wireSectionOn: BufferedImage = ImageIO.read(resourceInputStreamOf("images/arrow_on.svg"))
    val inverterOn: BufferedImage = ImageIO.read(resourceInputStreamOf("images/inverter_on.svg"))
    private val inverterOff: BufferedImage = ImageIO.read(resourceInputStreamOf("images/inverter_off.svg"))
    private val lightOn: BufferedImage = ImageIO.read(resourceInputStreamOf("images/light_on.svg"))
    val lightOff: BufferedImage = ImageIO.read(resourceInputStreamOf("images/light_off.svg"))
    private val switchOn: BufferedImage = ImageIO.read(resourceInputStreamOf("images/switch_on.svg"))
    val switchOff: BufferedImage = ImageIO.read(resourceInputStreamOf("images/switch.svg"))
    private var wireSectionOffScaled: BufferedImage = wireSectionOff
    private var wireSectionOnScaled: BufferedImage = wireSectionOn
    private var inverterOnScaled: BufferedImage = inverterOn
    private var inverterOffScaled: BufferedImage = inverterOff
    private var lightOnScaled: BufferedImage = lightOn
    private var lightOffScaled: BufferedImage = lightOff
    private var switchOnScaled: BufferedImage = switchOn
    private var switchOffScaled: BufferedImage = switchOff
    private var scale = 1.0F
    private var nodeSize = 50/*size of node image*/ * scale

    fun drawWire(
        point1: Point,
        point2: Point,
        power: Boolean,
        g2D: Graphics2D,
        observer: ImageObserver?
    ) {
        val rotation = angleBetweenPoints(point1, point2)
        val middle = point1.middle(point2).asMiddleOf(wireSectionOnScaled)
        drawRotatedImage(
            if (power) wireSectionOnScaled else wireSectionOffScaled,
            g2D,
            observer,
            middle.x,
            middle.y,
            rotation
        )
        val lineThickness = (10.0 * scale).toFloat()
        g2D.stroke = BasicStroke(lineThickness, CAP_ROUND, JOIN_ROUND)
        g2D.color = Color.BLACK
        g2D.draw(Line2D.Float(point1, point2))

        val powerLineThickness = lineThickness * 0.8F
        g2D.stroke = BasicStroke(powerLineThickness, CAP_ROUND, JOIN_ROUND)
        g2D.color = if (power) Color.YELLOW else Color.DARK_GRAY
        g2D.draw(Line2D.Float(point1, point2))
    }

    private fun drawRotatedImage(
        image: BufferedImage,
        g2d: Graphics2D,
        observer: ImageObserver?,
        drawLocationX: Int,
        drawLocationY: Int,
        rotation: Double
    ) {

        // Rotation information
        val rotationRequired = Math.toRadians(rotation)
        val locationX = (image.width shr 1).toDouble()
        val locationY = (image.height shr 1).toDouble()
        val tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY)
        val op = AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR)

        // Drawing the rotated image at the required drawing locations
        g2d.drawImage(op.filter(image, null), drawLocationX, drawLocationY, observer)
    }

    fun resourceInputStreamOf(path: String): InputStream? {
        return javaClass.classLoader.getResourceAsStream(path)
    }

    fun initScale(scale: Float) {
        this.scale = scale
        nodeSize = 50/*size of node image*/ * scale
        val tx = AffineTransform.getScaleInstance(scale.toDouble(), scale.toDouble())
        val op = AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR)
        wireSectionOnScaled = op.filter(wireSectionOn, null)
        wireSectionOffScaled = op.filter(wireSectionOff, null)
        inverterOffScaled = op.filter(inverterOff, null)
        inverterOnScaled = op.filter(inverterOn, null)
        lightOffScaled = op.filter(lightOff, null)
        lightOnScaled = op.filter(lightOn, null)
        switchOffScaled = op.filter(switchOff, null)
        switchOnScaled = op.filter(switchOn, null)
    }

    fun drawNode(
        type: NodeType,
        power: Boolean,
        location: Point,
        graphics2D: Graphics2D,
        observer: ImageObserver?
    ) {
        graphics2D.stroke = BasicStroke(0F)
        if (scale < 0.125) {
            graphics2D.color = when (type) {
                NodeType.inverter -> if (power) inverterOnColor else inverterOffColor
                NodeType.switch -> switchOffColor
                NodeType.switchOn -> switchOnColor
                NodeType.light -> if (power) lightOnColor else lightOffColor
            }
            graphics2D.fill(Rectangle2D.Float(location.x.toFloat(), location.y.toFloat(), nodeSize, nodeSize))
            return
        }
        val image = when (type) {
            NodeType.inverter -> if (power) inverterOnScaled else inverterOffScaled
            NodeType.switch -> switchOffScaled
            NodeType.switchOn -> switchOnScaled
            NodeType.light -> if (power) lightOnScaled else lightOffScaled
        }
        val drawLocation = location.fromMiddleOf(image)
        graphics2D.drawImage(image, drawLocation.x, drawLocation.y, observer)
    }

    private val inverterOnColor = Color(0, 200, 200)
    private val inverterOffColor = Color(0, 100, 100)
    private val switchOnColor = Color(255, 40, 0)
    private val switchOffColor = Color(120, 10, 0)
    private val lightOnColor = Color(200, 200, 200)
    private val lightOffColor = Color(25, 25, 25)
}