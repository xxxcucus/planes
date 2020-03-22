package com.planes.common;

import java.util.Objects;

public class Coordinate2D implements Cloneable {

    public Coordinate2D() {}
    public Coordinate2D(int x, int y) {
        m_x = x;
        m_y = y;
    }

    public int x() {
        return m_x;
    }

    public int y() {
        return m_y;
    }

    public Coordinate2D add(final Coordinate2D other) {
        return new Coordinate2D(m_x + other.m_x, m_y + other.m_y);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (getClass() != other.getClass())
            return false;
        Coordinate2D coordinate = (Coordinate2D) other;

        return (m_x == coordinate.m_x) && (m_y == coordinate.m_y);
    }

    @Override
    public Object clone() {
        return new Coordinate2D(m_x, m_y);
    }

    private int m_x = 0;
    private int m_y = 0;
}
