package com.aidanmars.nodesim.core.extensions

import com.aidanmars.nodesim.core.*
import java.io.InputStream
import java.io.OutputStream
import kotlin.math.max

fun Circuit.advancedSerialize(stream: OutputStream) {
    verify()
    stream.writeInt(nodes.size)
    var index = -1
    val indexedNodes = nodes.map { idNodePair ->
        index++
        idNodePair.value to index
    }.toMap()

    nodes.forEach { (_, node) ->
        stream.write(node.type.ordinal)
        stream.writeInt(node.x)
        stream.writeInt(node.y)
        stream.write(if (node.outputPower) 1 else 0)
        stream.writeInt(node.outputNodes.size)
        node.outputNodes.forEach {
            stream.writeInt(indexedNodes[it]!!)
        }
    }

    stream.writeInt(updates.size)
    updates.forEach {
        stream.writeInt(indexedNodes[it]!!)
    }
}

fun advancedDeserializeCircuit(stream: InputStream): Circuit {
    val numNodes = stream.readInt()
    val rawNodes = Array(numNodes) {
        Node(it, NodeType.entries[stream.read()],
            stream.readInt(), stream.readInt(),
            outputPower = stream.read() == 1
        ) to Array(stream.readInt()) { stream.readInt() } // read output nodes
    }

    val chunks = ChunkMap()
    rawNodes.forEach { nodePair ->
        nodePair.second.forEach {
            rawNodes[it].first.inputNodes.add(nodePair.first)
        }
        nodePair.first.outputNodes.addAll(nodePair.second.map { rawNodes[it].first })

        chunks.addNode(nodePair.first)
    } // connect nodes

    val nodes = rawNodes.associate { nodePair ->
        val node = nodePair.first
        node.inputPower = node.inputNodes.count { it.outputPower }
        node.id to node
    }.toMutableMap()
    val updates = Array(stream.readInt()) {
        nodes[stream.readInt()]
    }.toMutableSet()

    updates.removeIf { it === null }
    return Circuit(
        nodes,
        updates as MutableSet<Node>,
        chunks,
        numNodes
    )
}

private fun OutputStream.writeInt(value: Int) {
    write(value)
    write(value ushr 8)
    write(value ushr 16)
    write(value ushr 24)
}

private fun InputStream.readInt(): Int {
    val byte1 = read()
    val byte2 = read()
    val byte3 = read()
    val byte4 = read()
    if (byte1 < 0 || byte2 < 0 || byte3 < 0 || byte4 < 0) return 0
    return byte1 + byte2 shl 8 + byte3 shl 16 + byte4 shl 24
}

/**
 * the serialization variant built to be interoperable with the original node logic simulator
 */
fun Circuit.stringSerialize(): String {
    val builder = StringBuilder()
    builder.append("#2|")
    nodes.forEach { (_, node) ->
        builder.append("@#")
        builder.append(getNodeTypeStringId(node.type))
        builder.append('#')
        builder.append(node.x)
        builder.append('#')
        builder.append(node.y)
        builder.append('#')
        builder.append(if (node.outputPower) 1 else 0)
    }

    builder.append('|')

    val nodeIdMap = mutableMapOf<Int, Int>()
    nodes.onEachIndexed { index, entry ->
        nodeIdMap[entry.key] = index
    }
    nodes.forEach { (id, node) ->
        val index = nodeIdMap[id]
        node.outputNodes.forEach {
            builder.append("@#")
            builder.append(index)
            builder.append('#')
            builder.append(nodeIdMap[it.id])
        }
    }

    builder.append('|')

    updates.forEach {
        builder.append('#')
        builder.append(nodeIdMap[it.id])
    }
    return builder.toString()
}

/**
 * @throws IllegalArgumentException if an error occurs in the deserialization
 */
fun deserializeToCircuit(input: String): Circuit {
    if (!input.startsWith("#2|")) throw IllegalArgumentException("unknown serialization version")
    val sections = input.substring(3).split('|', limit = 3)
    if (sections.size < 3) throw IllegalArgumentException("bad input")

    var greatestId = 0
    val nodes = sections[0].split('@').filter { it != "" }.mapIndexed { index, nodeString ->
        val nodeParts = nodeString.split('#')
        if (nodeParts.size != 5) throw IllegalArgumentException("bad node")
        greatestId = max(index, greatestId)

        try {
            index to Node(
                index,
                getNodeTypeFromStringId(Integer.parseInt(nodeParts[1])),
                Integer.parseInt(nodeParts[2]), Integer.parseInt(nodeParts[3]),
                outputPower = nodeParts[4] == "1"
            )
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("bad node")
        }
    }.toMap().toMutableMap()
    val circuit = Circuit(
        nodes,
        mutableSetOf(),
        ChunkMap(),
        greatestId + 1
    )

    sections[1].split('@').forEach {
        if (it == "") return@forEach
        val wireParts = it.split('#')
        if (wireParts.size != 3) throw IllegalArgumentException("bad connection")

        val fromId: Int
        val toId: Int
        try {
            fromId = Integer.parseInt(wireParts[1])
            toId = Integer.parseInt(wireParts[2])
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("bad connection")
        }
        if (fromId !in nodes || toId !in nodes) throw IllegalArgumentException("bad connection")
        circuit.connectNodes(nodes[fromId]!!, nodes[toId]!!, false)
    }

    sections[2].split('#').forEach {
        if (it == "") return@forEach
        val id: Int
        try {
            id = Integer.parseInt(it)
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("bad update")
        }
        circuit.updates.add(nodes[id] ?: throw IllegalArgumentException("bad update"))
    }

    return circuit
}

private fun getNodeTypeStringId(type: NodeType): Int {
    return when (type) {
        NodeType.Switch -> 3
        NodeType.SwitchOn -> 4
        NodeType.Light -> 2
        NodeType.NorGate -> 1
        NodeType.AndGate -> 6
        NodeType.XorGate -> 7
    }
}

private fun getNodeTypeFromStringId(id: Int): NodeType {
    return when (id) {
        1 -> NodeType.NorGate
        2 -> NodeType.Light
        3 -> NodeType.Switch
        4 -> NodeType.SwitchOn
        6 -> NodeType.AndGate
        7 -> NodeType.XorGate
        else -> NodeType.Light
    }
}