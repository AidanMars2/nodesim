package com.aidanmars.nodesim.extensions

import com.aidanmars.nodesim.Project

fun Project.tick() {
    val currentUpdates = updates.toList()
    updates.clear()
    currentUpdates.forEach { node ->
        if (node.id !in nodes) return@forEach
        val oldOutput = node.output
        node.update()
        val decrementWires = oldOutput && !node.output
        val incrementWires = node.output && !oldOutput

        node.outputWires.forEach { wire ->
            when {
                decrementWires -> decrementNodePower(wire.output)
                incrementWires -> incrementNodePower(wire.output)
            }
        }
    }
}