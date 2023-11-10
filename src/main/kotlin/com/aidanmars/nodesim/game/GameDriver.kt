package com.aidanmars.nodesim.game

import com.aidanmars.nodesim.Node
import com.aidanmars.nodesim.NodeType
import com.aidanmars.nodesim.Wire
import com.aidanmars.nodesim.extensions.tick
import com.aidanmars.nodesim.getChunk
import java.awt.Point
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.Timer

object GameDriver : ActionListener {
    private const val TICK_TIME = 25
    private val timer = Timer(TICK_TIME, this)
    private val simulationTimer = Timer(TICK_TIME, ProjectSimulationDriver)
    private var initialized = false

    override fun actionPerformed(e: ActionEvent) {
        GameUI.repaint()
    }

    fun startTimers() {
        simulationTimer.start()
        timer.start()
    }

    fun stopTimers() {
        simulationTimer.stop()
        timer.stop()
    }

    fun init() {
        if (initialized) return
        initialized = true
        GameWindow.init()
        startTimers()
    }

    fun deInit() {
        if (!initialized) return
        stopTimers()
        GameWindow.deInit()
        initialized = false
    }

    private object ProjectSimulationDriver : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            GameData.checkData()
            handleInputs()
            GameData.project.tick()
        }

        private fun handleInputs() {
            while (GameData.clickQueue.isNotEmpty()) {
                val input = GameData.clickQueue.poll()
                when (input.type) {
                    Input.InputType.click -> handleClick(input.to)
                    Input.InputType.drag -> handleDrag(input.from, input.to)
                }
            }
        }

        private fun handleClick(point: Point) {
            when (GameData.currentTool) {
                ToolType.place -> {
                    GameData.project.createNode(point.x, point.y, GameData.currentPlaceType)
                }
                ToolType.delete -> {
                    val clickedObject = getClickedObject(point, true)
                    when {
                        clickedObject.second !== null -> GameData.project.deleteWire(clickedObject.second!!)
                        clickedObject.first !== null -> GameData.project.deleteNode(clickedObject.first!!)
                    }
                }
                ToolType.connect -> {}
                ToolType.interact -> {
                    val clickedObject = getClickedObject(point)
                    if (clickedObject.first === null)return
                    when (clickedObject.first!!.type) {
                        NodeType.inverter -> {}
                        NodeType.switch -> {
                            clickedObject.first!!.type = NodeType.switchOn
                            GameData.project.updateNode(clickedObject.first!!)
                        }
                        NodeType.switchOn -> {
                            clickedObject.first!!.type = NodeType.switch
                            GameData.project.updateNode(clickedObject.first!!)
                        }
                        NodeType.light -> {}
                    }
                }
            }
        }

        private fun handleDrag(from: Point, to: Point) {
            when (GameData.currentTool) {
                ToolType.place -> handleClick(to)
                ToolType.delete -> handleClick(to)
                ToolType.connect -> {
                    val fromClickedObject = getClickedObject(from)
                    val toClickedObject = getClickedObject(to)
                    if (fromClickedObject.first === null || toClickedObject.first === null) return
                    GameData.project.layWire(fromClickedObject.first!!, toClickedObject.first!!)
                }
                ToolType.interact -> {
                    val fromClickedObject = getClickedObject(from)
                    if (fromClickedObject.first === null) return
                    GameData.project.moveNode(fromClickedObject.first!!, to.x, to.y)
                }
            }
        }

        private fun getClickedObject(point: Point, checkWires: Boolean = false): Pair<Node?, Wire?> {
            val pointChunk = getChunk(point.x, point.y)
            var clickedNode: Node? = null
            var clickedWire: Wire? = null
            for (chunkX in pointChunk.x-1..pointChunk.x+1) {
                for (chunkY in pointChunk.y-1..pointChunk.y+1) {
                    val chunk = GameData.project.chunks[Point(chunkX, chunkY)]
                    if (chunk === null) continue
                    chunk.lastOrNull {
                        point.distance(Point(it.x, it.y)) < 22
                    }?.also { clickedNode = it }
                    if (!checkWires) continue
                    chunk.forEach {node ->
                        node.inputWires.forEach {
                            val closest = calculateClosestPoint(point, it.input.point(), it.output.point())
                            if (point.distance(closest) < 5) clickedWire = it
                        }
                        node.outputWires.forEach {
                            val closest = calculateClosestPoint(point, it.input.point(), it.output.point())
                            if (point.distance(closest) < 5) clickedWire = it
                        }
                    }
                }
            }
            return clickedNode to clickedWire
        }
    }
}