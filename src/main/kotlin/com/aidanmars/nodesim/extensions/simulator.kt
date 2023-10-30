package com.aidanmars.nodesim.extensions

import com.aidanmars.nodesim.Project

fun Project.doStep() {
    val currentUpdates = updates.toList()
    updates.clear()
    currentUpdates.forEach { nodeId ->
        val node = nodes[nodeId]
        if (node === null) return@forEach
        val oldOutput = node.output
        node.update()
        val decrementWires = oldOutput > node.output
        val incrementWires = node.output > oldOutput

        var wireId = node.firstWire
        while (wireId.isNotBlank()) {
            val wire = wires[wireId]!!

            if (decrementWires) decrementNodePower(wire.output)
            if (incrementWires) incrementNodePower(wire.output)

            wireId = wire.next
        }
    }
}