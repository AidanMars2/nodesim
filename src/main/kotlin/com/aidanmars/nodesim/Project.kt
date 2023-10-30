package com.aidanmars.nodesim

class Project(
    var nodes: MutableMap<String, Node> = mutableMapOf(),
    var wires: MutableMap<String, Wire> = mutableMapOf(),
    val updates: MutableSet<String> = mutableSetOf(),
) {
    fun layWire(from: String, to: String): String {
        val newId = getNewWireId()
        val fromNode = nodes[from]!!
        val toNode = nodes[to]!!
        wires[newId] = Wire(
            from,
            to,
            fromNode.firstWire,
            ""
        )
        fromNode.firstWire = newId
        if (fromNode.output > 0) {
            incrementNodePower(to, toNode)
        }
        return newId
    }

    fun deleteWire(id: String) {
        val wire = wires.remove(id)!!
        if (wire.previous == "") {
            nodes[wire.input]?.firstWire = wire.next
        } else {
            wires[wire.next]?.previous = wire.previous
            wires[wire.previous]?.next = wire.next
        }
        if ((nodes[wire.input]?.output ?: 0) > 0) {
            val outputNode = nodes[wire.output]
            if (outputNode === null) return
            decrementNodePower(wire.output, outputNode)
        }
    }

    fun createNode(x: Int, y: Int, type: NodeType): Pair<String, Node> {
        val id = getNewNodeId()
        val node = Node(
            type,
            x,
            y,
            "",
            0,
            type.update(0)
        )
        nodes[id] = node
        return id to node
    }

    fun deleteNode(id: String) {
        val node = nodes[id]!!
        while (node.firstWire.isNotBlank()) {
            deleteWire(node.firstWire)
        }
        nodes.remove(id)
        wires.keys.toList().forEach { wireId ->
            val wire = wires[wireId]!!
            if (wire.output == id) deleteWire(wireId)
        }
    }

    fun verifyWires() {
        wires.keys.toList().forEach { wireId ->
            val wire = wires[wireId]!!
            if (wire.input !in nodes || wire.output !in nodes) deleteWire(wireId)
        }
    }

    /**
     * @return a list of ids corresponding to the wires that power the node
     */
    fun getNodePowerSources(id: String): List<String> {
        return wires.keys.filter {
            val wire = wires[it]!!
            wire.output == id
        }
    }

    fun incrementNodePower(id: String, node: Node = nodes[id]!!) {
        node.inputPower++
        if (node.inputPower == 1) updates.add(id)
    }

    fun decrementNodePower(id: String, node: Node = nodes[id]!!) {
        node.inputPower--
        if (node.inputPower == 0) updates.add(id)
    }

    fun getNewNodeId(): String {
        var id = generateId('n')
        while (id in nodes) {
            id = generateId('n')
        }
        return id
    }

    fun getNewWireId(): String {
        var id = generateId('w')
        while (id in wires) {
            id = generateId('w')
        }
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Project

        if (nodes != other.nodes) return false
        if (wires != other.wires) return false
        if (updates != other.updates) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nodes.hashCode()
        result = 31 * result + wires.hashCode()
        result = 31 * result + updates.hashCode()
        return result
    }
}