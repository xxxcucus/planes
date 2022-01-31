package com.planes.android.preferences

import android.content.Context
import android.os.Bundle
import android.util.Log


class SinglePlayerPreferencesService internal constructor(private val m_Context: Context): ISinglePlayerPreferencesService {
    private var m_ComputerSkill = 2
    override var showPlaneAfterKill = false

    override var computerSkill: Int
        get() = m_ComputerSkill
        set(skill) {
            if (skill < 0 || skill > 2) m_ComputerSkill = 2
            m_ComputerSkill = skill
        }

    override fun readPreferences() {
        val sp_gamedifficulty = m_Context.getSharedPreferences("gamedifficulty",
                Context.MODE_PRIVATE)
        computerSkill = sp_gamedifficulty.getInt("computerskill", 2)
        showPlaneAfterKill = sp_gamedifficulty.getBoolean("showkilledplane", false)

        val sp_multiplayer = m_Context.getSharedPreferences("multiplayer", Context.MODE_PRIVATE)
    }

    override fun readFromSavedInstanceState(savedInstanceState: Bundle) {
        computerSkill = savedInstanceState.getInt("gamedifficulty/computerskill")
        showPlaneAfterKill = savedInstanceState.getBoolean("gamedifficulty/showkilledplane")
    }

    override fun writePreferences() {
        val sp_gamedifficulty = m_Context.getSharedPreferences("gamedifficulty",
                Context.MODE_PRIVATE).edit()
        sp_gamedifficulty.putInt("computerskill", computerSkill)
        sp_gamedifficulty.putBoolean("showkilledplane", showPlaneAfterKill)
        sp_gamedifficulty.commit()
    }

    override fun writeToSavedInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putInt("gamedifficulty/computerskill", computerSkill)
        savedInstanceState.putBoolean("gamedifficulty/showkilledplane", showPlaneAfterKill)
    }
}