package com.aidanmars.nodesim.serialization

//import com.aidanmars.nodesim.Project
//
//fun serialize(project: Project): String {
//    val builder = StringBuilder()
//    builder.append('|')
//    for (entry in project.nodes) {
//        builder.append("@#")
//        builder.append(entry.key)
//        builder.append('#')
//        builder.append(entry.value.type.serialName)
//        builder.append('#')
//        builder.append(entry.value.x)
//        builder.append('#')
//        builder.append(entry.value.y)
//        builder.append('#')
//        builder.append(entry.value.firstWire)
//        builder.append('#')
//        builder.append(entry.value.inputPower)
//        builder.append('#')
//        builder.append(entry.value.output)
//    }
//    builder.append('|')
//    for (entry in project.wires) {
//        builder.append("@#")
//        builder.append(entry.key)
//        builder.append('#')
//        builder.append(entry.value.input)
//        builder.append('#')
//        builder.append(entry.value.output)
//        builder.append('#')
//        builder.append(entry.value.next)
//        builder.append('#')
//        builder.append(entry.value.previous)
//    }
//    builder.append('|')
//    for (update in project.updates) {
//        builder.append('#')
//        builder.append(update)
//    }
//    return builder.toString()
//}