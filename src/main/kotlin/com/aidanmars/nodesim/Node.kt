package com.aidanmars.nodesim

data class Node(
    val id: Long,
    val type: NodeType,
    var x: Int,
    var y: Int,
    val inputWires: MutableList<Wire>,
    val outputWires: MutableList<Wire>,
    var inputPower: Int,
    var output: Byte
) {
    fun update() {
        output = type.update(inputPower)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.toInt()
    }
}