package com.planes.android

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.planes.android.IPreferencesService


class PreferencesService internal constructor(private val m_Context: Context): IPreferencesService {
    private var m_ComputerSkill = 2
    override var showPlaneAfterKill = false

    override var computerSkill: Int
        get() = m_ComputerSkill
        set(skill) {
            if (skill < 0 || skill > 2) m_ComputerSkill = 2
            m_ComputerSkill = skill
        }

    override fun readPreferences() {
        val sp = m_Context.getSharedPreferences("gamedifficulty",
                Context.MODE_PRIVATE)
        computerSkill = sp.getInt("computerskill", 2)
        showPlaneAfterKill = sp.getBoolean("showkilledplane", false)
        Log.d("Planes", "readPreferences " + computerSkill + " " + showPlaneAfterKill)
    }

    override fun readFromSavedInstanceState(savedInstanceState: Bundle) {
        computerSkill = savedInstanceState.getInt("gamedifficulty/computerskill")
        showPlaneAfterKill = savedInstanceState.getBoolean("gamedifficulty/showkilledplane")
    }

    override fun writePreferences() {
        val sp = m_Context.getSharedPreferences("gamedifficulty",
                Context.MODE_PRIVATE).edit()
        sp.putInt("computerskill", computerSkill)
        sp.putBoolean("showkilledplane", showPlaneAfterKill)
        sp.commit()
        Log.d("Planes", "writePreferences " + m_ComputerSkill + " " + showPlaneAfterKill)
    }

    override fun writeToSavedInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putInt("gamedifficulty/computerskill", computerSkill)
        savedInstanceState.putBoolean("gamedifficulty/showkilledplane", showPlaneAfterKill)
    }
}