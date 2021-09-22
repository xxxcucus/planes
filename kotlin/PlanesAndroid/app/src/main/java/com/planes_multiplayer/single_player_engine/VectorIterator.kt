package com.planes_multiplayer.single_player_engine

import java.util.*

open class VectorIterator<T> {
    protected var m_internalList: Vector<T>
    private var m_idx = 0

    init {
        m_internalList = Vector()
        reset()
    }

    fun reset() {
        m_idx = -1
    }

    operator fun hasNext(): Boolean {
        return m_idx < m_internalList.size - 1
    }

    operator fun next(): T {
        m_idx++
        return m_internalList[m_idx]
    }

    fun itemNo(): Int {
        return m_internalList.size
    }

    //for test purposes
    fun setInternalList(list: Vector<T>) {
        m_internalList = list
    }

}