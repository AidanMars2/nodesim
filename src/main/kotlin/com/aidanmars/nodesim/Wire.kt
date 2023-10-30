package com.aidanmars.nodesim

data class Wire(
    val input: String,
    val output: String,
    var next: String,
    var previous: String
) {
}