package com.aidanmars.nodesim

data class Wire(
    val id: Long,
    val input: Node,
    val output: Node
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Wire

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.toInt()
    }
}