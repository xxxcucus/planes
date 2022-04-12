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

    var m_CurFragment: Int
    var m_Context: Context
    var m_MainLayout: LinearLayoutCompat
    var m_StartTutorialLambda: (Int) -> Unit

    private lateinit var m_PlaneRound: PlanesRoundInterface
    lateinit var m_HelpTextView: TextView
    lateinit var m_HelpTitleTextView: TextView
    lateinit var m_HelpButton: Button

    init {
        m_CurFragment = curFragment
        m_Context = context
        m_MainLayout = mainLayout
        m_StartTutorialLambda = startTutorial

        m_PlaneRound = PlanesRoundJava()
        (m_PlaneRound as PlanesRoundJava).createPlanesRound()
    }

    fun onButtonShowHelpWindowClick() {

        if (m_CurFragment in arrayOf(R.id.nav_about, R.id.nav_settings))
            return

        // inflate the layout of the popup window
        val inflater = m_Context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.help_popup, null)

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(m_MainLayout, Gravity.CENTER, 0, 0)
        m_HelpTextView = popupView.findViewById(R.id.popup_help_text) as TextView
        m_HelpTitleTextView = popupView.findViewById(R.id.popup_help_title) as TextView
        m_HelpButton = popupView.findViewById(R.id.popup_help_button) as Button
        if (m_HelpTextView != null && m_HelpTitleTextView != null) {
            when(m_CurFragment) {
                R.id.nav_game -> {
                    showHelpGameFragment(popupWindow)
                }
                R.id.nav_videos -> {
                    showHelpVideoFragment()
                }
            }
        }

        // dismiss the popup window when touched
        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }
    }


    fun showHelpGameFragment(popupWindow: PopupWindow) {
        var gameStage = m_PlaneRound.getGameStage()
        when (gameStage) {
            GameStages.GameNotStarted.value -> {
                m_HelpTitleTextView.text = m_Context.getString(R.string.game_not_started_stage)
                m_HelpTextView.text = m_Context.getString(R.string.helptext_startnewgame_1)
                m_HelpButton.setEnabled(false)
            }
            GameStages.BoardEditing.value -> {
                m_HelpTitleTextView.text = m_Context.getString(R.string.board_editing_stage)
                m_HelpTextView.text = """
                    ${m_Context.getString(R.string.helptext_boardediting_1)}
                    ${m_Context.getString(R.string.helptext_boardediting_2)}
                    """.trimIndent()
                m_HelpButton.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(view: View) {
                        m_StartTutorialLambda(1)
                        popupWindow.dismiss()
                    }
                })
                m_HelpButton.setEnabled(true)
            }
            GameStages.Game.value -> {
                m_HelpTitleTextView.text = m_Context.getString(R.string.game_stage)
                m_HelpTextView.text = """
                    ${m_Context.getString(R.string.helptext_game_1)}
                    ${m_Context.getString(R.string.helptext_game_2)}
                    """.trimIndent()
                m_HelpButton.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(view: View) {
                        m_StartTutorialLambda(0)
                        popupWindow.dismiss()
                    }
                })
                m_HelpButton.setEnabled(true)
            }
        }
    }

    fun showHelpVideoFragment() {
        m_HelpTitleTextView.text = m_Context.getString(R.string.videos)
        m_HelpTextView.text = """
                    ${m_Context.getString(R.string.helptext_videos1)}
                    ${m_Context.getString(R.string.helptext_videos2)}
                    ${m_Context.getString(R.string.helptext_videos3)}
                    """.trimIndent()
        m_HelpButton.setEnabled(false)
    }

}