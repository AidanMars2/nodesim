package com.aidanmars.nodesim

data class Node(
    var type: NodeType,
    var x: Int,
    var y: Int,
    var firstWire: String,
    var inputPower: Int,
    var output: Int
) {
    fun update() {
        output = type.update(inputPower)
    }
}