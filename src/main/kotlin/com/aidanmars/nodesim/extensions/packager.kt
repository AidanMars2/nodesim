package com.aidanmars.nodesim.extensions

//import com.aidanmars.nodesim.Node
//import com.aidanmars.nodesim.NodeType
//import com.aidanmars.nodesim.Project
//import com.aidanmars.nodesim.Wire
//
//fun Project.packageIt(xNew: Int = 0, yNew: Int = 0) {
//    cullSwitchWires()
//    val keyList = nodes.keys.toList()
//    for (key in keyList) {
//        val value = nodes[key]!!
//        if (value.type === NodeType.switch || value.type === NodeType.switchOn || value.type === NodeType.light) {
//            if (handleSwitch(key, value)) continue
//        }
//        value.x = xNew
//        value.y = yNew
//    }
//}
//
//private fun Project.cullSwitchWires() {
//    wires.filter {
//        return@filter when (nodes[it.value.output]!!.type) {
//            NodeType.switchOn, NodeType.switch -> true
//            else -> false
//        }
//    }.forEach { (id, wire) ->
//        val removeWire = nodes[wire.output]!!.type === NodeType.switch ||
//                nodes[wire.output]!!.type === NodeType.switchOn
//        if (removeWire) {
//            wires.remove(id)
//            if (wire.next.isNotBlank()) wires[wire.next]!!.previous = wire.previous
//            if (wire.previous.isNotBlank()) wires[wire.previous]!!.next = wire.next
//        }
//    }
//}
//
//private fun Project.handleSwitch(name: String, node: Node): Boolean {
//    if (node.type === NodeType.light && node.firstWire.isNotBlank()) return false
//    val newNodePair = createNode(node.x + 15, node.y + 15, node.type)
//    val newNode = newNodePair.second
//    val newWireId = getNewWireId()
//    val isOutput = node.type === NodeType.light
//
//    if (isOutput) {
//        node.firstWire = newWireId
//        newNode.inputPower = node.output
//        newNode.update()
//    } else {
//        newNode.firstWire = newWireId
//        newNode.update()
//        node.type = NodeType.light
//    }
//
//    wires[newWireId] = Wire(
//        if (isOutput) name else newNodePair.first,
//        if (isOutput) newNodePair.first else name,
//        "",
//        ""
//    )
//
//    return true
//}