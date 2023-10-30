package com.aidanmars.nodesim.extensions

import com.aidanmars.nodesim.Node
import com.aidanmars.nodesim.Project

fun Project.copy(xRange: IntRange, yRange: IntRange, cut: Boolean): Project {
    verifyWires()
    val result = Project()
    val nodesToRemove = mutableListOf<String>()
    val resultNodesToRemove = mutableListOf<String>()
    nodes.keys.toList().forEach { nodeId ->
        val node = nodes[nodeId]!!
        if (node.inRange(xRange, yRange)) {
            result.nodes[nodeId] = node.copy()
            var wireId = node.firstWire
            while (wireId.isNotBlank()) {
                val wire = wires[wireId]!!

                result.wires[wireId] = wire.copy()

                wireId = wire.next
            }
            if (cut) nodesToRemove.add(nodeId)
        } else {
            var shouldCopyToResult = false
            var wireId = node.firstWire
            while (wireId.isNotBlank()) {
                val wire = wires[wireId]!!


                if (nodes[wire.output]!!.inRange(xRange, yRange) && !shouldCopyToResult) {
                    shouldCopyToResult = true
                    result.nodes[nodeId] = node.copy(firstWire = wireId)
                    resultNodesToRemove.add(nodeId)
                    result.wires[wireId] = wire.copy(previous = "")
                }
                if (shouldCopyToResult) result.wires[wireId] = wire

                wireId = wire.next
            }
        }
    }
    if (cut) {
        nodesToRemove.forEach {
            deleteNode(it)
        }
    }
    resultNodesToRemove.forEach {
        result.deleteNode(it)
    }
    result.verifyWires()

    return result
}


private fun Node.inRange(xRange: IntRange, yRange: IntRange): Boolean =
    x in xRange && y in yRange