package com.planes_multiplayer.single_player_engine

class GuessPoint(val row: Int, val col: Int, val tp: Type) : Cloneable {

    var m_row = 0
    var m_col = 0
    var m_type = Type.Miss

    init {
        m_row = row
        m_col = col
        m_type = tp
    }

    constructor(row: Int, col: Int): this(row, col, Type.Miss) {
        m_row = row
        m_col = col
    }

    fun row(): Int {
        return m_row
    }

    fun col(): Int {
        return m_col
    }

    fun type(): Type {
        return m_type
    }
    //sets the result of the guess
    fun setType(tp: Type) {
        m_type = tp
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val gp = other as GuessPoint
        return m_row == gp.m_row && m_col == gp.m_col
    }

    val isDead: Boolean
        get() = m_type === Type.Dead
    val isHit: Boolean
        get() = m_type === Type.Hit
    val isMiss: Boolean
        get() = m_type === Type.Miss

    public override fun clone(): Any {
        return GuessPoint(m_row, m_col, m_type)
    }
}