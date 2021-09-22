package com.planes_multiplayer.android

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.planes_multiplayer.single_player_engine.PlanesRoundJava

class OptionsActivity : AppCompatActivity() {
    private var m_ComputerSkill = 2
    private var m_ShowPlaneAfterKill = false
    private lateinit var m_PlaneRound: PlanesRoundInterface
    lateinit var m_Listener_skill: OnItemSelectedListener
    lateinit var m_Listener_show: OnItemSelectedListener
    lateinit var  m_Spinner_Skill: Spinner
    lateinit var  m_Spinner_Show: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
        m_Spinner_Skill = findViewById<View>(R.id.computer_skill_spinner) as Spinner
        val adapter_skill = ArrayAdapter.createFromResource(this,
                R.array.computer_skills, R.layout.spinner_item)
        adapter_skill.setDropDownViewResource(R.layout.spinner_item)
        m_Spinner_Skill.adapter = adapter_skill
        m_Spinner_Skill.onItemSelectedListener = null
        m_Spinner_Show = findViewById<View>(R.id.show_plane_after_kill) as Spinner
        val adapter_show = ArrayAdapter.createFromResource(this,
                R.array.yesno_options, R.layout.spinner_item)
        adapter_show.setDropDownViewResource(R.layout.spinner_item)
        m_Spinner_Show.adapter = adapter_show
        m_Spinner_Show.onItemSelectedListener = null
        val extras = intent.extras
        if (extras != null) {
            m_ComputerSkill = extras.getInt("gamedifficulty/computerskill")
            m_Spinner_Skill.setSelection(m_ComputerSkill)
            m_ShowPlaneAfterKill = extras.getBoolean("gamedifficulty/showkilledplane")
            m_Spinner_Show.setSelection(if (m_ShowPlaneAfterKill) 0 else 1)
        }
        m_Listener_show = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)
                println(item.toString())
                val shouldbepos = if (m_ShowPlaneAfterKill) 0 else 1
                if (pos == shouldbepos) return
                if (!m_PlaneRound.setShowPlaneAfterKill(pos == 0)) {
                    onWarning()
                    parent.setSelection(if (m_ShowPlaneAfterKill) 0 else 1)
                } else {
                    m_ShowPlaneAfterKill = pos == 0
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        m_Spinner_Show.onItemSelectedListener = m_Listener_show
        m_Listener_skill = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)
                println(item.toString())
                if (pos == m_ComputerSkill) return
                if (!m_PlaneRound.setComputerSkill(pos)) {
                    onWarning()
                    parent.setSelection(m_ComputerSkill)
                } else {
                    m_ComputerSkill = pos
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        m_Spinner_Skill.onItemSelectedListener = m_Listener_skill
        m_PlaneRound = PlanesRoundJava()
        (m_PlaneRound as PlanesRoundJava).createPlanesRound()
    }

    override fun onBackPressed() {
        val pushIntent = this.intent
        pushIntent.putExtra("gamedifficulty/computerskill", m_ComputerSkill)
        pushIntent.putExtra("gamedifficulty/showkilledplane", m_ShowPlaneAfterKill)
        setResult(RESULT_OK, pushIntent)
        finish()
    }

    fun onWarning() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.warning_options, null)

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(findViewById<View>(R.id.options_layout) as LinearLayout, Gravity.CENTER, 0, 0)

        // dismiss the popup window when touched
        popupView.setOnTouchListener { _, event ->
            popupWindow.dismiss()
            true
        }
    }
}