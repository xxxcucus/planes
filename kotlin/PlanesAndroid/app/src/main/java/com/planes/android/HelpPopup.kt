package com.planes.android

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import com.planes.single_player_engine.GameStages
import com.planes.single_player_engine.PlanesRoundJava

class HelpPopup(context: Context, mainLayout: LinearLayoutCompat, curFragment: Int,
        startTutorial: (Int) -> Unit) {

    private var m_CurFragment: Int
    var m_Context: Context
    private var m_MainLayout: LinearLayoutCompat
    var m_StartTutorialLambda: (Int) -> Unit

    private var m_PlaneRound: PlanesRoundInterface
    private lateinit var m_HelpTextView: TextView
    private lateinit var m_HelpTitleTextView: TextView
    private var m_HelpButton: Button? = null

    init {
        m_CurFragment = curFragment
        m_Context = context
        m_MainLayout = mainLayout
        m_StartTutorialLambda = startTutorial

        m_PlaneRound = PlanesRoundJava()
        (m_PlaneRound as PlanesRoundJava).createPlanesRound()
    }

    fun onButtonShowHelpWindowClick(multiplayerVersion: Boolean) {

        if (m_CurFragment in arrayOf(
                R.id.nav_about, R.id.nav_settings, R.id.nav_game_status, R.id.nav_login,
                R.id.nav_logout, R.id.register, R.id.nav_creategame
            )
        )
            return

        // inflate the layout of the popup window
        val inflater =
            m_Context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var popupView = View(m_Context);

        when (m_CurFragment) {
            R.id.nav_game -> {
                val gameStage = m_PlaneRound.getGameStage()
                if (gameStage == GameStages.BoardEditing.value || gameStage == GameStages.Game.value)
                    popupView = inflater.inflate(R.layout.help_popup, null)
                else
                    popupView = inflater.inflate(R.layout.help_popup_novideo, null)
            }
            R.id.nav_videos -> {
                popupView = inflater.inflate(R.layout.help_popup_novideo, null)
            }
        }

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(m_MainLayout, Gravity.CENTER, 0, 0)
        m_HelpTextView = popupView.findViewById(R.id.popup_help_text) as TextView
        m_HelpTitleTextView = popupView.findViewById(R.id.popup_help_title) as TextView

        m_HelpButton = popupView.findViewById(R.id.popup_help_button) as Button?

        when (m_CurFragment) {
            R.id.nav_game -> {
                showHelpGameFragment(multiplayerVersion, popupWindow)
            }
            R.id.nav_videos -> {
                showHelpVideoFragment()
            }
        }

        // dismiss the popup window when touched
        popupView.setOnTouchListener { _, _ ->
            popupWindow.dismiss()
            true
        }
    }

    private fun showHelpGameFragment(multiplayerVersion: Boolean, popupWindow: PopupWindow) {
        val gameStage = m_PlaneRound.getGameStage()
        when (gameStage) {
            GameStages.GameNotStarted.value -> {
                m_HelpTitleTextView.text = m_Context.getString(R.string.game_not_started_stage)
                m_HelpTextView.text = m_Context.getString(R.string.helptext_startnewgame_1)
            }
            GameStages.BoardEditing.value -> {
                m_HelpTitleTextView.text = m_Context.getString(R.string.board_editing_stage)

                m_HelpTextView.text = """
                ${m_Context.getString(R.string.helptext_boardediting_1)}
                ${m_Context.getString(R.string.helptext_boardediting_3)}
                ${m_Context.getString(R.string.helptext_game_3)}
                """.trimIndent()

                m_HelpButton!!.setOnClickListener {
                    m_StartTutorialLambda(1)
                    popupWindow.dismiss()
                }
                m_HelpButton!!.isEnabled = true
            }
            GameStages.Game.value -> {
                m_HelpTitleTextView.text = m_Context.getString(R.string.game_stage)
                if (!multiplayerVersion) {
                    m_HelpTextView.text = """
                    ${m_Context.getString(R.string.helptext_game_1)}
                    ${m_Context.getString(R.string.helptext_game_2)}
                    """.trimIndent()
                    m_HelpButton!!.setOnClickListener {
                        m_StartTutorialLambda(0)
                        popupWindow.dismiss()
                    }
                    m_HelpButton!!.isEnabled = true
                }
            }
        }
    }

    private fun showHelpVideoFragment() {
            m_HelpTitleTextView.text = m_Context.getString(R.string.videos)
            m_HelpTextView.text = """
                    ${m_Context.getString(R.string.helptext_videos1)}
                    ${m_Context.getString(R.string.helptext_videos2)}
                    ${m_Context.getString(R.string.helptext_videos3)}
                    """.trimIndent()
        }
    }
