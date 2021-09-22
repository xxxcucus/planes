package com.planes_multiplayer.android

import android.content.Context
import android.util.Log

class PreferencesService internal constructor(private val m_Context: Context) {
    private var m_ComputerSkill = 2
    var showPlaneAfterKill = false

    var computerSkill: Int
        get() = m_ComputerSkill
        set(skill) {
            if (skill < 0 || skill > 2) m_ComputerSkill = 2
            m_ComputerSkill = skill
        }

    fun readPreferences() {
        val sp = m_Context.getSharedPreferences("gamedifficulty",
                Context.MODE_PRIVATE)
        computerSkill = sp.getInt("computerskill", 2)
        showPlaneAfterKill = sp.getBoolean("showkilledplane", false)
        Log.d("Planes", "readPreferences " + computerSkill + " " + showPlaneAfterKill)
    }

    fun writePreferences() {
        val sp = m_Context.getSharedPreferences("gamedifficulty",
                Context.MODE_PRIVATE).edit()
        sp.putInt("computerskill", computerSkill)
        sp.putBoolean("showkilledplane", showPlaneAfterKill)
        sp.commit()
        Log.d("Planes", "writePreferences " + m_ComputerSkill + " " + showPlaneAfterKill)
    }



}