package com.aidanmars.nodesim

enum class NodeType(val update: (Int) -> Boolean) {
    inverter({ it == 0 }),
    switch({ false }),
    switchOn({ true }),
    light({ it > 0 })
}