package com.planes_multiplayer.single_player_engine

class PlaneRoundOptions {
    private fun reset() {
        m_ComputerSkillLevel = 2
        m_ShowPlaneAfterKill = false
    }

    var m_ComputerSkillLevel = 2 // 0 - EASY, 1 - MEDIUM, 2- ADVANCED

    var m_ShowPlaneAfterKill = false

    init {
        reset()
    }
}