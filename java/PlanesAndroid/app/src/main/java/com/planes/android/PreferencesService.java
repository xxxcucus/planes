package com.planes.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class PreferencesService {

    PreferencesService(Context context) {
        m_Context = context;
    }

    public int getComputerSkill() {
        return m_ComputerSkill;
    }

    public void setComputerSkill(int skill) {
        if (skill < 0 || skill > 2)
            m_ComputerSkill = 2;
        m_ComputerSkill = skill;
    }

    public boolean getShowPlaneAfterKill() {
        return m_ShowPlaneAfterKill;
    }

    public void setShowPlaneAfterKill(boolean show) {
        m_ShowPlaneAfterKill = show;
    }

    public void readPreferences() {
        SharedPreferences sp = m_Context.getSharedPreferences("gamedifficulty",
                MODE_PRIVATE);

        m_ComputerSkill = sp.getInt("computerskill", 2);
        m_ShowPlaneAfterKill =  sp.getBoolean("showkilledplane", false);
        Log.d("Planes", "readPreferences " + m_ComputerSkill + " " + m_ShowPlaneAfterKill);
    }


    public void writePreferences() {
        SharedPreferences.Editor sp = m_Context.getSharedPreferences("gamedifficulty",
                MODE_PRIVATE).edit();
        sp.putInt("computerskill", m_ComputerSkill);
        sp.putBoolean("showkilledplane", m_ShowPlaneAfterKill);
        sp.commit();
        Log.d("Planes", "writePreferences " + m_ComputerSkill + " " + m_ShowPlaneAfterKill);
    }

    private Context m_Context;
    private int m_ComputerSkill = 2;
    private boolean m_ShowPlaneAfterKill = false;
}
