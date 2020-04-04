package com.planes.common;

import java.util.Vector;

public class VectorIterator<T> {

    public VectorIterator() {
        m_internalList = new Vector<T>();
        reset();
    }

    public void reset() {
        m_idx = -1;
    }

    public boolean hasNext() {
        return (m_idx < m_internalList.size() - 1);
    }

    public T next() {
        m_idx++;
        return m_internalList.get(m_idx);
    }

    public int itemNo() {
        return m_internalList.size();
    }

    //for test purposes
    public void setInternalList(final Vector<T> list) {
        m_internalList = list;
    }

    protected Vector<T> m_internalList;
    private int m_idx;
}
