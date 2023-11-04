package com.aidanmars.nodesim.game

import javax.imageio.ImageIO
import javax.imageio.spi.IIORegistry

fun main() {
    ImageIO.scanForPlugins()
    IIORegistry.getDefaultInstance().registerApplicationClasspathSpis()
    GameDriver.init()
}