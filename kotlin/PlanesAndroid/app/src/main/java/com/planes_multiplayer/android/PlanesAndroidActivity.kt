package com.planes_multiplayer.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.planes_multiplayer.android.Tools.Links.openLink
import com.planes_multiplayer.single_player_engine.PlanesRoundJava

class PlanesAndroidActivity : AppCompatActivity() {

    private lateinit var m_PlaneRound: PlanesRoundInterface
    private lateinit var m_GameBoards: GameBoardsAdapter
    private lateinit var m_GameControls: GameControlsAdapter
    private lateinit var m_PlanesLayout: PlanesVerticalLayout
    private lateinit var m_PreferencesService: PreferencesService

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_planes, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.menu_help) {
            onButtonShowHelpWindowClick()
            return true
        } else if (id == R.id.menu_credits) {
            onButtonShowCreditsWindowClick()
            return true
        } else if (id == R.id.menu_options) {
            onShowOptionsClick()
            return true
        } else if (id == R.id.menu_videos) {
            onShowOtherVideosClick()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        m_PlaneRound = PlanesRoundJava()
        (m_PlaneRound as PlanesRoundJava).createPlanesRound()
        m_PlanesLayout = findViewById<View>(R.id.planes_layout) as PlanesVerticalLayout
        var isTablet = false
        var isHorizontal = false
        val linearLayout = findViewById<View>(R.id.rootView) as LinearLayout
        if (linearLayout.tag.toString().contains("tablet")) {
            isTablet = true
        }
        if (linearLayout.tag.toString().contains("horizontal")) {
            isHorizontal = true
        }
        m_GameBoards = if (isTablet) {
            val playerBoard = findViewById<View>(R.id.player_board) as GameBoard
            playerBoard.setGameSettings(m_PlaneRound, true)
            playerBoard.setPlayerBoard()
            val computerBoard = findViewById<View>(R.id.computer_board) as GameBoard
            computerBoard.setGameSettings(m_PlaneRound, true)
            computerBoard.setComputerBoard()
            GameBoardsAdapter(playerBoard, computerBoard)
        } else {
            val gameBoard = findViewById<View>(R.id.game_boards) as GameBoard
            gameBoard.setGameSettings(m_PlaneRound, false)
            GameBoardsAdapter(gameBoard)
        }

        //Board Editing Buttons
        val upButton = findViewById<View>(R.id.up_button) as Button
        val downButton = findViewById<View>(R.id.down_button) as Button
        val leftButton = findViewById<View>(R.id.left_button) as Button
        val rightButton = findViewById<View>(R.id.right_button) as Button
        val doneButton = findViewById<View>(R.id.done_button) as Button
        val rotateButton = findViewById<View>(R.id.rotate_button) as Button

        //Game Stage
        val statsTitle = findViewById<View>(R.id.stats_title_label) as ColouredSurfaceWithText
        val viewComputerBoardButton1 = findViewById<View>(R.id.view_computer_board1) as TwoLineTextButtonWithState
        val movesLabel = findViewById<View>(R.id.moves_label) as ColouredSurfaceWithText
        val movesCount = findViewById<View>(R.id.moves_count) as ColouredSurfaceWithText
        val missesLabel = findViewById<View>(R.id.misses_label) as ColouredSurfaceWithText
        val missesCount = findViewById<View>(R.id.misses_count) as ColouredSurfaceWithText
        val hitsLabel = findViewById<View>(R.id.hits_label) as ColouredSurfaceWithText
        val hitsCount = findViewById<View>(R.id.hits_count) as ColouredSurfaceWithText
        val deadsLabel = findViewById<View>(R.id.dead_label) as ColouredSurfaceWithText
        val deadCount = findViewById<View>(R.id.dead_count) as ColouredSurfaceWithText

        //Start New Game Stage
        val viewComputerBoardButton2 = findViewById<View>(R.id.view_computer_board2) as TwoLineTextButtonWithState
        val startNewGameButton = findViewById<View>(R.id.start_new_game) as TwoLineTextButton
        val computerWinsLabel = findViewById<View>(R.id.computer_wins_label) as ColouredSurfaceWithText
        val computerWinsCount = findViewById<View>(R.id.computer_wins_count) as ColouredSurfaceWithText
        val playerWinsLabel = findViewById<View>(R.id.player_wins_label) as ColouredSurfaceWithText
        val playerWinsCount = findViewById<View>(R.id.player_wins_count) as ColouredSurfaceWithText
        val winnerText = findViewById<View>(R.id.winner_textview) as ColouredSurfaceWithText
        val drawsLabel = findViewById<View>(R.id.draws_label) as ColouredSurfaceWithText
        val drawsCount = findViewById<View>(R.id.draws_count) as ColouredSurfaceWithText
        m_GameControls = GameControlsAdapter(this)
        m_GameControls.setBoardEditingControls(upButton, downButton, leftButton, rightButton, doneButton, rotateButton)
        if (!isTablet) m_GameControls.setGameControls(statsTitle, viewComputerBoardButton1, movesLabel, movesCount, missesLabel, missesCount, hitsLabel, hitsCount, deadsLabel, deadCount)
        m_GameControls.setStartNewGameControls(viewComputerBoardButton2, startNewGameButton, computerWinsLabel, computerWinsCount, playerWinsLabel, playerWinsCount, drawsLabel, drawsCount, winnerText)
        m_GameControls.setGameSettings(m_PlaneRound, isTablet)
        m_GameControls.setGameBoards(m_GameBoards)
        m_GameControls.setPlanesLayout(m_PlanesLayout)
        m_GameBoards.setGameControls(m_GameControls)
        when ((m_PlaneRound as PlanesRoundJava).getGameStage()) {
            0 -> {
                m_GameBoards.setNewRoundStage()
                m_GameControls.setNewRoundStage()
                m_PlanesLayout.setComputerBoard()
                m_PlanesLayout.setNewRoundStage()
            }
            1 -> {
                m_GameBoards.setBoardEditingStage()
                m_GameControls.setBoardEditingStage()
                m_PlanesLayout.setBoardEditingStage()
            }
            2 -> {
                m_GameBoards.setGameStage()
                m_GameControls.setGameStage()
                m_PlanesLayout.setGameStage()
            }
        }
        m_PreferencesService = PreferencesService(this)

        // recovering the instance state
        m_PreferencesService.readPreferences()
        if (!(m_PlaneRound as PlanesRoundJava).setComputerSkill(m_PreferencesService.computerSkill)) {
            m_PreferencesService.computerSkill = (m_PlaneRound as PlanesRoundJava).getComputerSkill()
        }
        if (!(m_PlaneRound as PlanesRoundJava).setShowPlaneAfterKill(m_PreferencesService.showPlaneAfterKill)) {
            m_PreferencesService.showPlaneAfterKill = (m_PlaneRound as PlanesRoundJava).getShowPlaneAfterKill()
        }
        Log.d("Planes", "onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Planes", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Planes", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Planes", "onPause")
    }

    override fun onStop() {
        m_PreferencesService.writePreferences()
        super.onStop()
        Log.d("Planes", "onStop")
    }

    public override fun onDestroy() {
        m_PreferencesService.writePreferences()
        super.onDestroy()
        Log.d("Planes", "onDestroy")
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("gamedifficulty/computerskill", m_PreferencesService.computerSkill)
        outState.putBoolean("gamedifficulty/showkilledplane", m_PreferencesService.showPlaneAfterKill)

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)
        Log.d("Planes", "onSaveInstanceState")
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        m_PreferencesService.computerSkill = savedInstanceState.getInt("gamedifficulty/computerskill")
        m_PreferencesService.showPlaneAfterKill = savedInstanceState.getBoolean("gamedifficulty/showkilledplane")
        if (!m_PlaneRound.setComputerSkill(m_PreferencesService.computerSkill)) {
            m_PreferencesService.computerSkill = m_PlaneRound.getComputerSkill()
        }
        if (!m_PlaneRound.setShowPlaneAfterKill(m_PreferencesService.showPlaneAfterKill)) {
            m_PreferencesService.showPlaneAfterKill = m_PlaneRound.getShowPlaneAfterKill()
        }
        Log.d("Planes", "onRestoreInstanceState")
    }

    fun onButtonShowHelpWindowClick() {

        // inflate the layout of the popup window
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.help_popup, null)

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(m_PlanesLayout, Gravity.CENTER, 0, 0)
        val helpTextView = popupView.findViewById<View>(R.id.popup_help_text) as TextView
        val helpTitleTextView = popupView.findViewById<View>(R.id.popup_help_title) as TextView
        val link_tutorial_game = "https://www.youtube.com/watch?v=CAxSPp2h_Vo"
        val link_tutorial_board_editing = "https://www.youtube.com/watch?v=qgL0RdwqBRY"
        val helpButton = popupView.findViewById<View>(R.id.popup_help_button) as Button
        if (helpTextView != null && helpTitleTextView != null) {
            when (m_GameBoards.gameStage) {
                GameStages.GameNotStarted -> {
                    helpTitleTextView.text = resources.getString(R.string.game_not_started_stage)
                    helpTextView.text = resources.getString(R.string.helptext_startnewgame_1)
                    helpButton.isEnabled = false
                }
                GameStages.BoardEditing -> {
                    helpTitleTextView.text = resources.getString(R.string.board_editing_stage)
                    helpTextView.text = """
                        ${resources.getString(R.string.helptext_boardediting_1)}
                        ${resources.getString(R.string.helptext_boardediting_2)}
                        """.trimIndent()
                    helpButton.setOnClickListener { view -> openLink(view.context, link_tutorial_board_editing) }
                    helpButton.isEnabled = true
                }
                GameStages.Game -> {
                    helpTitleTextView.text = resources.getString(R.string.game_stage)
                    helpTextView.text = """
                        ${resources.getString(R.string.helptext_game_1)}
                        ${resources.getString(R.string.helptext_game_2)}
                        """.trimIndent()
                    helpButton.setOnClickListener { view -> openLink(view.context, link_tutorial_game) }
                    helpButton.isEnabled = true
                }
            }
        }

        // dismiss the popup window when touched
        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }
    }

    fun onButtonShowCreditsWindowClick() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.credits_popup, null)
        val showGithubPageButton = popupView.findViewById<View>(R.id.credits_software_button) as Button
        val showAlexPageButton = popupView.findViewById<View>(R.id.credits_graphics_button) as Button
        showGithubPageButton.setOnClickListener { view -> openLink(view.context, "https://www.github.com/xxxcucus/planes") }
        showAlexPageButton.setOnClickListener { view -> openLink(view.context, "https://axa951.wixsite.com/portfolio") }

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(m_PlanesLayout, Gravity.CENTER, 0, 0)

        // dismiss the popup window when touched
        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }
    }

    fun onShowOptionsClick() {
        val intent = Intent(this, OptionsActivity::class.java)
        intent.putExtra("gamedifficulty/computerskill", m_PreferencesService.computerSkill)
        intent.putExtra("gamedifficulty/showkilledplane", m_PreferencesService.showPlaneAfterKill)
        startActivityForResult(intent, 1)
    }

    fun onShowOtherVideosClick() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.videos_popup, null)
        val link_general_tutorial = "https://youtube.com/watch?v=N2Cg8eflCxM"
        val link_multiplayer_tutorial = "https://youtube.com/watch?v=VqYK1T91-YE"
        val showGeneralVideoButton = popupView.findViewById<View>(R.id.general_video_button) as Button
        val showMultiplayerVideoButton = popupView.findViewById<View>(R.id.multiplayer_video_button) as Button
        showGeneralVideoButton.setOnClickListener { view -> openLink(view.context, link_general_tutorial) }
        showMultiplayerVideoButton.setOnClickListener { view -> openLink(view.context, link_multiplayer_tutorial) }

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(m_PlanesLayout, Gravity.CENTER, 0, 0)

        // dismiss the popup window when touched
        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, returnIntent: Intent?) {
        if (resultCode != RESULT_OK) return
        if (requestCode == 1) {
            val extras = returnIntent!!.extras
            if (extras != null) {
                val skill = extras.getInt("gamedifficulty/computerskill")
                val show = extras.getBoolean("gamedifficulty/showkilledplane")
                m_PreferencesService.computerSkill = skill
                m_PreferencesService.showPlaneAfterKill = show
            }
        }
        super.onActivityResult(requestCode, resultCode, returnIntent)
    }

}