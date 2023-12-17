package com.aidanmars.nodesim.core.extensions

import com.aidanmars.nodesim.core.Circuit
import com.aidanmars.nodesim.core.Node
import com.aidanmars.nodesim.core.NodeType
import com.aidanmars.nodesim.core.getChunk
import kotlin.math.max
import kotlin.math.min

/**
 * moves all nodes in a region to a single location.
 * Switches are unaffected by this.
 * @param regionX the region in the x axis
 * @param regionY the region in the y axis
 * @param newX the new x location to move all the nodes to
 * @param newY the new y location to move all the nodes to
 * @param bufferIO true if a light should buffer the input/output of the packaged region
 */
fun Circuit.packageRegion(
    regionX: IntRange, regionY: IntRange,
    newX: Int, newY: Int,
    bufferIO: Boolean = true
) {
    val nodesToChange = getNodesInRegion(regionX, regionY)
    nodesToChange.forEach { node ->
        when (node.type) {
            NodeType.Switch, NodeType.SwitchOn ->
                packageSwitch(bufferIO, node)

            NodeType.Light ->
                packageLight(newX, newY, bufferIO, nodesToChange, node)

            else -> moveNode(node, newX, newY)
        }
    }
}

private fun Circuit.packageSwitch(
    bufferIO: Boolean, node: Node
) {
    if (!bufferIO) return

    val bufferNode = createNode(node.x + 20, node.y + 20, NodeType.Light)
    bufferNode.outputPower = node.outputPower
    bufferNode.inputPowerBuffer = if (node.outputPower) 1 else 0

    node.outputNodes.forEach {
        connectNodes(bufferNode, it, false, false)
    }
    node.outputNodes.toList().forEach {
        disconnectNodes(node, it, false, false)
    }
}

private fun Circuit.packageLight(
    newX: Int, newY: Int,
    bufferIO: Boolean, nodesToChange: Set<Node>, node: Node
) {
    val isOutput = node.outputNodes.any { it !in nodesToChange } || node.outputNodes.isEmpty()
    val oldX = node.x
    val oldY = node.y
    moveNode(node, newX, newY)
    if (!bufferIO || !isOutput) return

    // update buffer node
    val bufferNode = createNode(oldX, oldY, NodeType.Light)
    bufferNode.outputPower = node.outputPower
    bufferNode.inputPowerBuffer = if (node.outputPower) 1 else 0
    connectNodes(node, bufferNode, false, false)

    // move connections to buffer node
    val connectionsToChange = node.outputNodes.filter { it !in nodesToChange }
    connectionsToChange.forEach {
        connectNodes(bufferNode, it, false, false)
        disconnectNodes(node, it, false, false)
    }
}