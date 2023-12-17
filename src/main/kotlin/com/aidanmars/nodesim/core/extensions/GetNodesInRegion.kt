package com.aidanmars.nodesim.core.extensions

import com.aidanmars.nodesim.core.Circuit
import com.aidanmars.nodesim.core.Node
import com.aidanmars.nodesim.core.getChunk
import kotlin.math.max
import kotlin.math.min

/**
 * @return the nodes in the region
 */
fun Circuit.getNodesInRegion(regionX: IntRange, regionY: IntRange): Set<Node> {
    val result = mutableSetOf<Node>()
    val (fromChunkX, fromChunkY) = getChunk(regionX.first, regionY.first)
    val (toChunkX, toChunkY) = getChunk(regionX.last, regionY.last)
    for (chunkX in min(toChunkX, fromChunkX)..max(toChunkX, fromChunkX)) {
        for (chunkY in min(toChunkY, fromChunkY)..max(toChunkY, fromChunkY)) {
            val chunk = chunks[chunkX, chunkY]
            if (chunk === null) continue
            if (chunkX != fromChunkX && chunkX != toChunkX && chunkY != fromChunkY && chunkY != toChunkY) {
                result.addAll(chunk)
            } else {
                chunk.forEach {
                    if (it.x in regionX && it.y in regionY) {
                        result.add(it)
                    }
                }
            }
        }
    }
    return result
}