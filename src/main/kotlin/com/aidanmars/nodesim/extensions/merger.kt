package com.aidanmars.nodesim.extensions

import com.aidanmars.nodesim.Node
import com.aidanmars.nodesim.Project
import com.aidanmars.nodesim.Wire

fun Project.merge(other: Project, xAdjust: Int, yAdjust: Int) {
    other.verifyWires()
    val nodeIdMap = mutableMapOf<String, String>()
    val wireIdMap = mutableMapOf<String, String>()
    other.nodes.forEach { (id, node) ->
        nodes[nodeIdMap.getOrPut(id) { getNewNodeId() }] = Node(
            node.type,
            node.x + xAdjust,
            node.y + yAdjust,
            wireIdMap.getOrPut(node.firstWire) { getNewWireId() },
            node.inputPower,
            node.output
        )
    }
    other.wires.forEach { (id, wire) ->
        wires[wireIdMap[id]!!] = Wire(
            nodeIdMap.getOrPut(wire.input) { getNewNodeId() },
            nodeIdMap.getOrPut(wire.output) { getNewNodeId() },
            wireIdMap.getOrPut(wire.next) { getNewWireId() },
            wireIdMap.getOrPut(wire.previous) { getNewWireId() },
        )
    }
    updates.addAll(other.updates.map { nodeIdMap.getOrPut(it) { getNewNodeId() } })
}