package com.aidanmars.nodesim.cli

//import com.aidanmars.nodesim.extensions.copy
//import com.aidanmars.nodesim.serialization.deserialize
//import com.aidanmars.nodesim.serialization.serialize
//
//fun doCutCli() {
//    print("project: ")
//    val project = deserialize(readln())
//    print("selection x1: ")
//    val x1 = readln().toInt()
//    print("selection x2: ")
//    val x2 = readln().toInt()
//    print("selection y1: ")
//    val y1 = readln().toInt()
//    print("selection y2: ")
//    val y2 = readln().toInt()
//    val selection = project.copy(x1..x2, y1..y2, true)
//    println("project: ${serialize(project)}")
//    println("selection: ${serialize(selection)}")
//}