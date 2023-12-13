package com.aidanmars.nodesim.core

class ChunkMap : MutableMap<Pair<Int, Int>, MutableSet<Node>> {
    private val map = mutableMapOf<Pair<Int, Int>, MutableSet<Node>>()
    override val entries: MutableSet<MutableMap.MutableEntry<Pair<Int, Int>, MutableSet<Node>>>
        get() = map.entries
    override val keys: MutableSet<Pair<Int, Int>>
        get() = map.keys
    override val size: Int
        get() = map.size
    override val values: MutableCollection<MutableSet<Node>>
        get() = map.values

    override fun clear() {
        map.clear()
    }

    override fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    override fun remove(key: Pair<Int, Int>): MutableSet<Node>? {
        return map.remove(key)
    }

    override fun putAll(from: Map<out Pair<Int, Int>, MutableSet<Node>>) {
        map.putAll(from)
    }

    override fun put(key: Pair<Int, Int>, value: MutableSet<Node>): MutableSet<Node>? {
        return map.put(key, value)
    }

    override fun get(key: Pair<Int, Int>): MutableSet<Node>? {
        return map[key]
    }

    override fun containsValue(value: MutableSet<Node>): Boolean {
        return map.containsValue(value)
    }

    override fun containsKey(key: Pair<Int, Int>): Boolean {
        return map.containsKey(key)
    }

    operator fun get(x: Int, y: Int): MutableSet<Node>? {
        return map[x to y]
    }

    operator fun set(x: Int, y: Int, value: MutableSet<Node>) {
        map[x to y] = value
    }

    fun addNode(node: Node) {
        map.getOrPut(getChunk(node.x, node.y)) { mutableSetOf() }.add(node)
    }
}