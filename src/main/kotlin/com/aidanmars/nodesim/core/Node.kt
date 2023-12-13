package com.aidanmars.nodesim.core

data class Node(
    val id: Int,
    var type: NodeType,
    var x: Int,
    var y: Int,
    val inputNodes: MutableSet<Node> = mutableSetOf(),
    val outputNodes: MutableSet<Node> = mutableSetOf(),
    var inputPower: Int = inputNodes.count { it.outputPower },
    var outputPower: Boolean = type.update(inputNodes.size, inputPower)
) {
    var inputPowerBuffer = inputPower
    /**
     * @param propagate if this function should update the nodes that depend on this node
     * @return the nodes to be updated next
     */
    fun update(propagate: Boolean = true): Set<Node> {
        inputPower = inputPowerBuffer
        val oldPower = outputPower
        outputPower = type.update(inputNodes.size, inputPower)
        if (!propagate) return emptySet()
        val powerOffset = when {
            oldPower == outputPower -> return emptySet()
            !oldPower -> 1
            else -> -1
        }
        outputNodes.forEach { it.inputPowerBuffer += powerOffset }

        return outputNodes
    }

    fun trigger(propagate: Boolean = true): Pair<Boolean, Set<Node>> {
        type = type.trigger() ?: return false to emptySet()
        return true to update(propagate)
    }

    fun setPower(power: Boolean, propagate: Boolean = true): Pair<Boolean, Set<Node>> {
        type = type.setPower(power) ?: return false to emptySet()
        val updateResults = update(propagate)
        if (outputPower != power) return false to updateResults
        return true to updateResults
    }
}