package com.aidanmars.nodesim.core

class Circuit(
    val nodes: MutableMap<Int, Node> = mutableMapOf(),
    var updates: MutableSet<Node> = mutableSetOf(),
    val chunks: ChunkMap = ChunkMap(),
    private var nextNodeId: Int = 0
) {
    fun createNode(x: Int, y: Int, type: NodeType): Node {
        val newId = getNextId()
        val newNode = Node(newId, type, x, y)

        chunks.addNode(newNode)
        nodes[newId] = newNode
        return newNode
    }

    fun deleteNode(node: Node, update: Boolean = true) {
        node.inputNodes.forEach { it.outputNodes.remove(node) }
        node.outputNodes.forEach { it.inputNodes.remove(node) }
        if (node.outputPower) {
            node.outputNodes.forEach {
                it.inputPowerBuffer--
            }
        }
        if (update) updates.addAll(node.outputNodes) // and gate logic
        nodes.remove(node.id)
        val chunk = getChunk(node.x, node.y)
        chunks[chunk]?.remove(node)
    }

    fun disconnectNodes(from: Node, to: Node, update: Boolean = true): Boolean {
        if (from !in to.inputNodes || to !in from.outputNodes) return false
        from.outputNodes.remove(to)
        to.inputNodes.remove(from)
        if (from.outputPower) to.inputPowerBuffer--
        if (update) updates.add(to) // and gate logic
        return true
    }

    fun connectNodes(from: Node, to: Node, update: Boolean = true): Boolean {
        if (from in to.inputNodes || to in from.outputNodes) return false
        from.outputNodes.add(to)
        to.inputNodes.add(from)
        if (from.outputPower) to.inputPowerBuffer++
        if (update) updates.add(to) // and gate logic
        return true
    }

    fun moveNode(node: Node, newX: Int, newY: Int) {
        chunks[node.x, node.y]?.remove(node)
        node.x = newX
        node.y = newY
        chunks.getOrPut(newX to newY) { mutableSetOf() }.add(node)
    }

    fun updateNode(node: Node, update: Boolean = true) {
        updates.addAll(node.update(update))
    }

    fun triggerNode(node: Node, update: Boolean = true): Boolean {
        val (triggerSuccess, triggerUpdates) = node.trigger(update)
        updates.addAll(triggerUpdates)
        return triggerSuccess
    }

    fun setNodePower(node: Node, power: Boolean, update: Boolean = true): Boolean {
        val (powerSuccess, powerUpdates) = node.setPower(power, update)
        updates.addAll(powerUpdates)
        return powerSuccess
    }

    private fun getNextId(): Int = nextNodeId.also { nextNodeId++ }
}