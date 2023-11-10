package com.aidanmars.nodesim

import java.awt.Point
import java.util.Random

class Project(
    val nodes: MutableMap<Long, Node> = mutableMapOf(),
    val wires: MutableMap<Long, Wire> = mutableMapOf(),
    val updates: MutableSet<Node> = mutableSetOf(),
    private val random: Random = Random(),
    val chunks: MutableMap<Point, MutableList<Node>> = mutableMapOf()
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
        if (update && wire.input.output && wire.output.id in nodes) {
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

    fun updateNode(node: Node) {
        if (node.id !in nodes) return
        val oldOutput = node.output
        node.update()
        val decrementWires = oldOutput && !node.output
        val incrementWires = node.output && !oldOutput

        node.outputWires.forEach { wire ->
            when {
                decrementWires -> decrementNodePower(wire.output)
                incrementWires -> incrementNodePower(wire.output)
            }
        }
    }

    private fun nodeExists(node: Node): Boolean = (node === nodes[node.id])
    private fun wireExists(wire: Wire): Boolean = (wire === wires[wire.id])

    fun verifyWires() {
        wires.keys.toList().forEach { wireId ->
            val wire = wires[wireId]!!
            if (wire.input.id !in nodes || wire.output.id !in nodes) deleteWire(wire)
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
        while (id in nodes) {
            id = random.nextLong()
        }
        return id
    }

    private fun getNewWireId(): Long {
        var id = random.nextLong()
        while (id in wires) {
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