package com.aidanmars.nodesim.core

enum class NodeType {
    Switch {
        override fun update(totalConnections: Int, totalPower: Int): Boolean {
            return false
        }

        override fun trigger(): NodeType {
            return SwitchOn
        }

        override fun setPower(power: Boolean): NodeType {
            return if (power) SwitchOn else Switch
        }
    },
    SwitchOn {
        override fun update(totalConnections: Int, totalPower: Int): Boolean {
            return true
        }

        override fun trigger(): NodeType {
            return Switch
        }

        override fun setPower(power: Boolean): NodeType {
            return if (power) SwitchOn else Switch
        }
    },
    Light {
        override fun update(totalConnections: Int, totalPower: Int): Boolean {
            return totalPower > 0
        }

        override fun trigger(): NodeType? {
            return null
        }

        override fun setPower(power: Boolean): NodeType? {
            return null
        }
    },
    NorGate {
        override fun update(totalConnections: Int, totalPower: Int): Boolean {
            return totalPower <= 0
        }

        override fun trigger(): NodeType? {
            return null
        }

        override fun setPower(power: Boolean): NodeType? {
            return null
        }
    },
    AndGate {
        override fun update(totalConnections: Int, totalPower: Int): Boolean {
            return totalPower == totalConnections
        }

        override fun trigger(): NodeType? {
            return null
        }

        override fun setPower(power: Boolean): NodeType? {
            return null
        }
    },
    XorGate {
        override fun update(totalConnections: Int, totalPower: Int): Boolean {
            return (totalPower and 1) == 1
        }

        override fun trigger(): NodeType? {
            return null
        }

        override fun setPower(power: Boolean): NodeType? {
            return null
        }
    };

    /**
     * @param totalConnections the amount of input connections this node has
     * @param totalPower the total amount of input connections that are powered
     * @return true if the node should be powered on
     */
    abstract fun update(totalConnections: Int, totalPower: Int): Boolean

    /**
     * @return the new node type, or null if this node type does not allow itself to be triggered
     */
    abstract fun trigger(): NodeType?

    /**
     * @param power the desired power
     * @return the new node type, or null of power can't be set for this node type
     */
    abstract fun setPower(power: Boolean): NodeType?
}