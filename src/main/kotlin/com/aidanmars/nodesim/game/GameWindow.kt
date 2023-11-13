package com.aidanmars.nodesim.game

//import javax.swing.JFrame
//
//object GameWindow {
//    var initialized = false
//    var window: JFrame? = null
//
//    fun init() {
//        if (initialized) return
//        initialized = true
//        window?.dispose()
//        window = JFrame("Node Simulator")
//
//        window!!.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
//        window!!.add(GameUI)
//        window!!.addKeyListener(GameUI)
//        window!!.addMouseListener(GameUI)
//        window!!.addMouseMotionListener(GameUI)
//        window!!.addMouseWheelListener(GameUI)
//        window!!.isResizable = true
//        window!!.pack()
//        window!!.setLocationRelativeTo(null)
//        window!!.isVisible = true
//    }
//
//    fun deInit() {
//        if (!initialized) return
//        window?.dispose()
//        initialized = false
//    }
//}