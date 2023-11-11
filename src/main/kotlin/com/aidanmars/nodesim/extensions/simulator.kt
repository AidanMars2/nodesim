package com.aidanmars.nodesim.extensions

import com.aidanmars.nodesim.Project

fun Project.tick() {
    val currentUpdates = updates.toList()
    updates.clear()
    currentUpdates.map { node -> updateNode(node) }.forEach { it() }
}