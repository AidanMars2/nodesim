package com.aidanmars.nodesim

enum class NodeType(val serialName: String, val update: (Int) -> Int) {
    inverter("inverter", { if (it > 0) 0 else 1 }),
    switch("switch", { 0 }),
    switchOn("switch-on", { 1 }),
    light("light", { if (it > 0) 1 else 0 })
}