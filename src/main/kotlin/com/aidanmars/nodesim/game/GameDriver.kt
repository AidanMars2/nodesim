package com.aidanmars.nodesim.game

import com.aidanmars.nodesim.extensions.tick
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.Timer

object GameDriver : ActionListener {
    private const val TICK_TIME = 25
    val timer = Timer(TICK_TIME, this)
    private var initialized = false

    override fun actionPerformed(e: ActionEvent) {
        GameData.checkData()
        GameData.project.tick()
        GameUI.repaint()
    }

    fun init() {
        if (initialized) return
        initialized = true
        GameWindow.init()
        timer.start()
    }

    fun deInit() {
        if (!initialized) return
        timer.stop()
        GameWindow.deInit()
        initialized = false
    }
}