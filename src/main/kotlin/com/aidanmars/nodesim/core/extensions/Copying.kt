package com.aidanmars.nodesim.core.extensions

import com.aidanmars.nodesim.core.Circuit
import com.aidanmars.nodesim.core.Node

/**
 * copies the nodes in the specified area to a new circuit
 * @param regionX the region in the x axis
 * @param regionY the region in the y axis
 * @param xOffset the x offset to add to the node locations in the new circuit
 * @param yOffset the y offset to add to the node locations in the new circuit
 * @param cut true if the nodes in the old circuit are to be removed
 * @param propagate true if nodes that are updated should be added to the update list
 * @param update true if nodes that have lines disconnected should have their input power updated,
 * this causes them to update (see the parameter propagate).
 */
fun Circuit.copy(
    regionX: IntRange, regionY: IntRange,
    xOffset: Int, yOffset: Int,
    cut: Boolean,
    propagate: Boolean = true, update: Boolean = true
): Circuit {
    val nodesInRegion = getNodesInRegion(regionX, regionY)
    val result = Circuit()
    val nodeMap = LinkedHashMap<Node, Node>((nodesInRegion.size / 0.75f).toInt())
    // create nodes in copied circuit
    nodesInRegion.forEach {
        it.preUpdate()
        val newNode = result.createNode(it.x + xOffset, it.y + yOffset, it.type)
        newNode.outputPower = it.outputPower
        newNode.inputPower = it.inputPower
        newNode.inputPowerBuffer = it.inputPowerBuffer
        nodeMap[it] = newNode
    }
    // copy over updates
    result.updates.addAll(nodesInRegion.filter { it in updates }.map { nodeMap[it]!! })
    // connect nodes in copied circuit
    nodesInRegion.forEach { oldNode ->
        oldNode.outputNodes.forEach {
            if (it in nodesInRegion) {
                result.connectNodes(nodeMap[oldNode]!!, nodeMap[it]!!, false, false)
            }
        }
        oldNode.inputNodes.forEach {
            if (update && it.outputPower && it !in nodesInRegion) {
                oldNode.inputPowerBuffer--
                if (propagate) result.updates.add(nodeMap[oldNode]!!)
            }
        }
    }
    if (cut) {
        nodesInRegion.forEach {
            deleteNode(it, propagate, update)
        }
    }
    return result
}