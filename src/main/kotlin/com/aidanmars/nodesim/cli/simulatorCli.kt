package com.aidanmars.nodesim.cli

import com.aidanmars.nodesim.extensions.doStep
import com.aidanmars.nodesim.serialization.deserialize
import com.aidanmars.nodesim.serialization.serialize

fun doSimulatorCli() {
    print("project: ")
    val project = deserialize(readln())
    print("ticks: ")
    val ticks = readln().toInt()
    for (index in 0..ticks) {
        project.doStep()
    }
    println("project: ${serialize(project)}")
}