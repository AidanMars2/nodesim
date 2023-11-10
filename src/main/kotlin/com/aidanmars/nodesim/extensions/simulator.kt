package com.aidanmars.nodesim.extensions

import com.aidanmars.nodesim.Project

fun Project.tick() {
    val currentUpdates = updates.toList()
    updates.clear()
    currentUpdates.forEach { node -> updateNode(node) }
}