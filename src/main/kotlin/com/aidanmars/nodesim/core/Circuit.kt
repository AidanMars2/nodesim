package com.aidanmars.nodesim.core

class Circuit(
    val nodes: MutableMap<Int, Node> = mutableMapOf(),
    var updates: MutableSet<Node> = mutableSetOf(),
    val chunks: ChunkMap = ChunkMap(),
    private var nextNodeId: Int = 0
) {
    /**
     * creates a new node
     * @param x the x location of the new node
     * @param y the y location of the new node
     * @param type the type of the new node
     * @return the new node
     */
    fun createNode(x: Int, y: Int, type: NodeType): Node {
        val newId = getNextId()
        val newNode = Node(newId, type, x, y)

        chunks.addNode(newNode)
        nodes[newId] = newNode
        return newNode
    }

    /**
     * deletes a node and the wires attached to it
     * @param node the node to delete
     * @param propagate true if updates should propagate to other nodes
     * @param update true if power should be updated to nodes connected to this node
     */
    fun deleteNode(node: Node, propagate: Boolean = true, update: Boolean = true) {
        node.inputNodes.forEach { it.outputNodes.remove(node) }
        node.outputNodes.forEach { it.inputNodes.remove(node) }
        if (node.outputPower && update) {
            node.outputNodes.forEach {
                it.inputPowerBuffer--
            }
        }
        if (update && propagate) updates.addAll(node.outputNodes)
        nodes.remove(node.id)
        val chunk = getChunk(node.x, node.y)
        chunks[chunk]?.remove(node)
    }

    /**
     * disconnects two nodes
     * @param from the node the power came from
     * @param to the node to power went to
     * @param propagate true if updates should propagate to other nodes
     * @param update true if power should be updated to nodes connected to this node
     */
    fun disconnectNodes(from: Node, to: Node, propagate: Boolean = true, update: Boolean = true): Boolean {
        if (from !in to.inputNodes || to !in from.outputNodes) return false
        from.outputNodes.remove(to)
        to.inputNodes.remove(from)
        if (from.outputPower && update) to.inputPowerBuffer--
        if (update && propagate) updates.add(to)
        return true
    }

    /**
     * connects two nodes
     * @param from the node the power will come from
     * @param to the node to power will go to
     * @param propagate true if updates should propagate to other nodes
     * @param update true if power should be updated to nodes connected to this node
     */
    fun connectNodes(from: Node, to: Node, propagate: Boolean = true, update: Boolean = true): Boolean {
        if (from in to.inputNodes || to in from.outputNodes) return false
        from.outputNodes.add(to)
        to.inputNodes.add(from)
        if (from.outputPower && update) to.inputPowerBuffer++
        if (update && propagate) updates.add(to)
        return true
    }

    /**
     * moves a node
     * @param node the node to move
     * @param newX the new x location of the node
     * @param newY the new y location of the node
     */
    fun moveNode(node: Node, newX: Int, newY: Int) {
        chunks[node.x, node.y]?.remove(node)
        node.x = newX
        node.y = newY
        chunks.getOrPut(newX to newY) { mutableSetOf() }.add(node)
    }

    /**
     * updates a node
     * @param node the node to update
     * @param propagate true if the update should propagate to other nodes
     * @param preUpdate true if the node should have its preUpdate function called
     */
    fun updateNode(node: Node, propagate: Boolean = true, preUpdate: Boolean = true) {
        if (preUpdate) node.preUpdate()
        updates.addAll(node.update(propagate))
    }

    /**
     * triggers a node, like flicking a switch
     * @param node the node to trigger
     * @param propagate true if the update should propagate to other nodes
     */
    fun triggerNode(node: Node, propagate: Boolean = true): Boolean {
        val (triggerSuccess, triggerUpdates) = node.trigger(propagate)
        updates.addAll(triggerUpdates)
        return triggerSuccess
    }

    /**
     * sets a nodes output power safely, like setting the power of a switch
     * @param node the node to set the output power of
     * @param power the desired output power of the node
     * @param propagate true if the update should propagate to other nodes
     * @return true if the node allows power to be set
     */
    fun setNodePower(node: Node, power: Boolean, propagate: Boolean = true): Boolean {
        val (powerSuccess, powerUpdates) = node.setPower(power, propagate)
        updates.addAll(powerUpdates)
        return powerSuccess
    }

    private fun getNextId(): Int = nextNodeId.also { nextNodeId++ }
}