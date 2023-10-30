package com.aidanmars.nodesim.serialization

import com.aidanmars.nodesim.Node
import com.aidanmars.nodesim.NodeType
import com.aidanmars.nodesim.Project
import com.aidanmars.nodesim.Wire


fun deserialize(serialized: String): Project {
    val segments = serialized.split('|', limit = 4)
    if (segments.size != 4) throw IllegalArgumentException("invalid number of segments")
    val nodes = mutableMapOf<String, Node>()
    for (serialNode in segments[1].split('@')) {
        if (serialNode.isBlank()) continue
        val attributes = serialNode.split('#')
        nodes[attributes[1]] = Node(
            when (attributes[2]) {
                "switch" -> NodeType.switch
                "switch-on" -> NodeType.switchOn
                "inverter" -> NodeType.inverter
                "light" -> NodeType.light
                else -> throw IllegalArgumentException("invalid node type")
            },
            attributes[3].toInt(),
            attributes[4].toInt(),
            attributes[5],
            attributes[6].toInt(),
            attributes[7].toInt()
        )
    }
    val wires = mutableMapOf<String, Wire>()
    for (serialWire in segments[2].split('@')) {
        if (serialWire.isBlank()) continue
        val attributes = serialWire.split('#')
        wires[attributes[1]] = Wire(
            attributes[2],
            attributes[3],
            attributes[4],
            attributes[5]
        )
    }
    return Project(nodes, wires, segments[3].split('#').filter { it.isNotBlank() }.toMutableSet())
}