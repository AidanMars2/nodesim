package com.aidanmars.nodesim.core

const val SIZE_TILE = 40
const val SIZE_CHUNK = 16

fun getChunk(x: Int, y: Int): Pair<Int, Int> {
    var chunkX = x / (SIZE_TILE * SIZE_CHUNK)
    if (x < 0) chunkX--
    var chunkY = y / (SIZE_TILE * SIZE_CHUNK)
    if (y < 0) chunkY--
    return chunkX to chunkY
}