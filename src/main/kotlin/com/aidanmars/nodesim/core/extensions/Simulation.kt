package com.aidanmars.nodesim.core.extensions

import com.aidanmars.nodesim.core.Circuit

fun Circuit.tick(amount: UInt) {
    if (amount == 0u) return

    for (index in 0u..<amount){
        val thisTickUpdates = updates
        updates = mutableSetOf()
        thisTickUpdates.forEach { updateNode(it) }
    }
}