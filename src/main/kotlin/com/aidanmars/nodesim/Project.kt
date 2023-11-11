package com.aidanmars.nodesim

import java.awt.Point
import java.util.Random
import java.util.concurrent.ConcurrentHashMap

class Project(
    val nodes: ConcurrentHashMap<Long, Node> = ConcurrentHashMap(),
    val wires: ConcurrentHashMap<Long, Wire> = ConcurrentHashMap(),
    val updates: MutableSet<Node> = mutableSetOf(),
    private val random: Random = Random(),
    val chunks: ConcurrentHashMap<WorldLocation, MutableList<Node>> = ConcurrentHashMap()
) {
    /**
     * @return the new wire, or null if either the from or to node don't exist within this project
     */
    fun layWire(from: Node, to: Node, update: Boolean = true): Wire? {
        if (!nodeExists(from) || !nodeExists(to)) return null
        val newWire = Wire(
            getNewWireId(),
            from,
            to
        )
        from.outputWires.add(newWire)
        to.inputWires.add(newWire)
        wires[newWire.id] = newWire
        if (update && from.output) {
            incrementNodePower(to)
        }
        return newWire
    }

    fun deleteWire(wire: Wire, update: Boolean = true) {
        if (!wireExists(wire)) return
        wire.input.outputWires.remove(wire)
        wire.output.inputWires.remove(wire)
        wires.remove(wire.id)
        if (update && wire.input.output && nodes.containsKey(wire.output.id)) {
            decrementNodePower(wire.output)
        }
    }

    fun createNode(x: Int, y: Int, type: NodeType, update: Boolean = true): Node {
        val node = Node(
            getNewNodeId(),
            type, x, y,
            output = update && type.update(0)
        )
        nodes[node.id] = node
        chunks.getOrPut(getChunk(x, y)) { mutableListOf() }.add(node)
        return node
    }

    fun deleteNode(node: Node, update: Boolean = true) {
        if (!nodeExists(node)) return
        val wireInputs = node.inputWires.toList()
        val wireOutputs = node.outputWires.toList()

        wireInputs.forEach { deleteWire(it, false) }
        wireOutputs.forEach { deleteWire(it, update) }

        nodes.remove(node.id)
    }

    fun moveNode(node: Node, x: Int, y: Int) {
        if (!nodeExists(node)) return
        chunks[getChunk(node.x, node.y)]?.remove(node)
        chunks.getOrPut(getChunk(x, y)) { mutableListOf() }.add(node)
        node.x = x
        node.y = y
    }

    fun updateNode(node: Node): () -> Unit {
        if (!nodes.containsKey(node.id)) return {}
        val oldOutput = node.output
        node.update()
        val decrementWires = oldOutput && !node.output
        val incrementWires = node.output && !oldOutput

        return {
            node.outputWires.forEach { wire ->
                when {
                    decrementWires -> decrementNodePower(wire.output)
                    incrementWires -> incrementNodePower(wire.output)
                }
            }
        }
    }

    private fun nodeExists(node: Node): Boolean = (node === nodes[node.id])
    private fun wireExists(wire: Wire): Boolean = (wire === wires[wire.id])

    fun verifyWires() {
        wires.keys.toList().forEach { wireId ->
            val wire = wires[wireId]!!
            if (nodes.containsKey(wire.input.id) || nodes.containsKey(wire.output.id)) deleteWire(wire)
        }
    }

    private fun incrementNodePower(node: Node) {
        node.inputPower++
        if (node.inputPower == 1) updates.add(node)
    }

    private fun decrementNodePower(node: Node) {
        node.inputPower--
        if (node.inputPower == 0) updates.add(node)
    }

    private fun getNewNodeId(): Long {
        var id = random.nextLong()
        while (nodes.containsKey(id)) {
            id = random.nextLong()
        }
        return id
    }

    private fun getNewWireId(): Long {
        var id = random.nextLong()
        while (wires.containsKey(id)) {
            id = random.nextLong()
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