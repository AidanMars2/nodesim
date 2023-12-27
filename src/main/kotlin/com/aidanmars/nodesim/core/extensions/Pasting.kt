package com.aidanmars.nodesim.core.extensions

import com.aidanmars.nodesim.core.Circuit
import com.aidanmars.nodesim.core.Node

fun Circuit.paste(
    other: Circuit,
    xOffset: Int = 0, yOffset: Int = 0
) {
    val nodeMap = mutableMapOf<Node, Node>()
    val otherNodes = other.nodes.values
    otherNodes.forEach { node ->
        val newNode = createNode(node.x + xOffset, node.y + yOffset, node.type)
        newNode.outputPower = node.outputPower
        newNode.inputPower = node.inputPower
        newNode.inputPowerBuffer = node.inputPowerBuffer
        nodeMap[node] = newNode
    }

    otherNodes.forEach { node ->
        val mirrorNode = nodeMap[node]!!
        node.outputNodes.forEach {
            connectNodes(mirrorNode, nodeMap[it]!!, false, false)
        }
        if (node in other.updates) updates.add(mirrorNode)
    }
}