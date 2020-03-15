package com.planes.common;

public class GuessPoint {

    public GuessPoint(int row, int col) {
        m_row = row;
        m_col = col;
    }

    public GuessPoint(int row, int col, Type tp) {
        m_row = row;
        m_col = col;
        m_type = tp;
    }

    //sets the result of the guess
    public void setType(Type tp) {
        m_type = tp;
    }

    public boolean equals(final Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (getClass() != other.getClass())
            return false;
        GuessPoint gp = (GuessPoint) other;

        return (m_row == gp.m_row) && (m_col == gp.m_col);
    }

    public boolean isDead() {
        return m_type == Type.Dead;
    }
    public boolean isHit() {
        return m_type == Type.Hit;
    }
    public boolean isMiss() {
        return m_type == Type.Miss;
    }

    public int m_row = 0;
    public int m_col = 0;
    public Type m_type = Type.Miss;
}
