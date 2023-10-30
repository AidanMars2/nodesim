package com.aidanmars.nodesim.cli

import com.aidanmars.nodesim.extensions.merge
import com.aidanmars.nodesim.serialization.deserialize
import com.aidanmars.nodesim.serialization.serialize

fun doMergeCli() {
    print("original project: ")
    val original = deserialize(readln())
    print("project to merge: ")
    val toMerge = deserialize(readln())
    print("x offset: ")
    val xOffset = readln().toInt()
    print("y offset: ")
    val yOffset = readln().toInt()
    original.merge(toMerge, xOffset, yOffset)
    println(serialize(original))
}