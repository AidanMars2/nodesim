package com.aidanmars.nodesim.game

import com.aidanmars.nodesim.Node
import com.aidanmars.nodesim.extensions.tick
import com.aidanmars.nodesim.getChunk
import java.awt.Point
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.Timer

object GameDriver : ActionListener {
    private const val TICK_TIME = 25
    val timer = Timer(TICK_TIME, this)
    val simulationTimer = Timer(TICK_TIME, ProjectSimulationDriver)
    private var initialized = false

    override fun actionPerformed(e: ActionEvent) {
        GameData.checkData()
        GameData.project.tick()
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
            handleInputs()
            GameData.project.tick()
        }

        private fun handleInputs() {

        }

        private fun getClickedNode(point: Point): Node? {
            val pointChunk = getChunk(point.x, point.y)
            var clickedNode: Node? = null
            for (chunkX in pointChunk.x-1..pointChunk.x+1) {
                for (chunkY in pointChunk.y-1..pointChunk.y+1) {
                    val chunk = GameData.project.chunks[Point(chunkX, chunkY)]
                    if (chunk === null) continue
                    chunk.lastOrNull {
                        point.distance(Point(it.x, it.y)) < 22
                    }?.also { clickedNode = it }
                }
            }
            return clickedNode
        }
    }
}