package com.planes.singleplayerengine

class Coordinate2D(val x: Int, val y: Int) : Cloneable {
    private var m_x = 0
    private var m_y = 0

    init {
        m_x = x
        m_y = y
    }

    constructor(): this(0, 0)

    fun x(): Int {
        return m_x
    }

    fun y(): Int {
        return m_y
    }

    fun add(other: Coordinate2D): Coordinate2D {
        return Coordinate2D(m_x + other.m_x, m_y + other.m_y)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val coordinate = other as Coordinate2D
        return m_x == coordinate.m_x && m_y == coordinate.m_y
    }

    public override fun clone(): Any {
        return Coordinate2D(m_x, m_y)
    }

    override fun hashCode(): Int {
        var result = m_x
        result = 31 * result + m_y
        return result
    }
}