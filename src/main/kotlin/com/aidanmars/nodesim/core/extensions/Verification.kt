package com.aidanmars.nodesim.core.extensions

import com.aidanmars.nodesim.core.Circuit

fun Circuit.verify() {
    nodes.forEach { (_, node) ->
        node.inputNodes.removeAll { it.id !in nodes }
        node.outputNodes.removeAll { it.id !in nodes }
        val oldInputPower = node.inputPower
        node.inputPowerBuffer = node.inputNodes.count { it.outputPower }
        if (node.inputPower != oldInputPower) updates.add(node)
    }
}