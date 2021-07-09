package com.planes.common;

public class PlaneRoundOptions {

    public PlaneRoundOptions() {
        reset();
    }

    private void reset() {
        m_ComputerSkillLevel = 2;
        m_ShowPlaneAfterKill = false;
    }

    public int m_ComputerSkillLevel = 2; // 0 - EASY, 1 - MEDIUM, 2- ADVANCED
    public boolean m_ShowPlaneAfterKill = false;
}
