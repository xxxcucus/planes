package com.planes.android

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.planes.single_player_engine.PlanesRoundJava


/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.*/

class GameFragment : Fragment() {

    private lateinit var m_PlaneRound: PlanesRoundInterface
    private lateinit var m_GameBoards: GameBoardsAdapter
    private lateinit var m_GameControls: GameControlsAdapter
    private lateinit var m_PlanesLayout: PlanesVerticalLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        m_PlaneRound = PlanesRoundJava()
        (m_PlaneRound as PlanesRoundJava).createPlanesRound()

        m_GameControls = GameControlsAdapter(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_game, container, false)

        m_PlanesLayout = rootView.findViewById<View>(R.id.planes_layout) as PlanesVerticalLayout

        var isTablet = false
        var isHorizontal = false
        val linearLayout = rootView.findViewById<View>(R.id.rootView) as LinearLayout
        if (linearLayout.tag.toString().contains("tablet")) {
            isTablet = true
        }
        if (linearLayout.tag.toString().contains("horizontal")) {
            isHorizontal = true
        }
        m_GameBoards = if (isTablet) {
            val playerBoard = rootView.findViewById<View>(R.id.player_board) as GameBoard
            playerBoard.setGameSettings(m_PlaneRound, true)
            playerBoard.setPlayerBoard()
            val computerBoard = rootView.findViewById<View>(R.id.computer_board) as GameBoard
            computerBoard.setGameSettings(m_PlaneRound, true)
            computerBoard.setComputerBoard()
            GameBoardsAdapter(playerBoard, computerBoard)
        } else {
            val gameBoard = rootView.findViewById<View>(R.id.game_boards) as GameBoard
            gameBoard.setGameSettings(m_PlaneRound, false)
            GameBoardsAdapter(gameBoard)
        }

        //Board Editing Buttons
        val upButton = rootView.findViewById<View>(R.id.up_button) as Button
        val downButton = rootView.findViewById<View>(R.id.down_button) as Button
        val leftButton = rootView.findViewById<View>(R.id.left_button) as Button
        val rightButton = rootView.findViewById<View>(R.id.right_button) as Button
        val doneButton = rootView.findViewById<View>(R.id.done_button) as Button
        val rotateButton = rootView.findViewById<View>(R.id.rotate_button) as Button

        //Game Stage
        val statsTitle = rootView.findViewById<View>(R.id.stats_title_label) as ColouredSurfaceWithTwoLineText?
        val viewComputerBoardButton1 = rootView.findViewById<View>(R.id.view_computer_board1) as TwoLineTextButtonWithState?
        val movesLabel = rootView.findViewById<View>(R.id.moves_label) as ColouredSurfaceWithText?
        val movesCount = rootView.findViewById<View>(R.id.moves_count) as ColouredSurfaceWithText?
        val missesLabel = rootView.findViewById<View>(R.id.misses_label) as ColouredSurfaceWithText?
        val missesCount = rootView.findViewById<View>(R.id.misses_count) as ColouredSurfaceWithText?
        val hitsLabel = rootView.findViewById<View>(R.id.hits_label) as ColouredSurfaceWithText?
        val hitsCount = rootView.findViewById<View>(R.id.hits_count) as ColouredSurfaceWithText?
        val deadsLabel = rootView.findViewById<View>(R.id.dead_label) as ColouredSurfaceWithText?
        val deadCount = rootView.findViewById<View>(R.id.dead_count) as ColouredSurfaceWithText?

        //Start New Game Stage
        val viewComputerBoardButton2 = rootView.findViewById<View>(R.id.view_computer_board2) as TwoLineTextButtonWithState
        val startNewGameButton = rootView.findViewById<View>(R.id.start_new_game) as TwoLineTextButton
        val computerWinsLabel = rootView.findViewById<View>(R.id.computer_wins_label) as ColouredSurfaceWithText
        val computerWinsCount = rootView.findViewById<View>(R.id.computer_wins_count) as ColouredSurfaceWithText
        val playerWinsLabel = rootView.findViewById<View>(R.id.player_wins_label) as ColouredSurfaceWithText
        val playerWinsCount = rootView.findViewById<View>(R.id.player_wins_count) as ColouredSurfaceWithText
        val winnerText = rootView.findViewById<View>(R.id.winner_textview) as ColouredSurfaceWithText
        val drawsLabel = rootView.findViewById<View>(R.id.draws_label) as ColouredSurfaceWithText
        val drawsCount = rootView.findViewById<View>(R.id.draws_count) as ColouredSurfaceWithText

        m_GameControls.setBoardEditingControls(upButton, downButton, leftButton, rightButton, doneButton, rotateButton)
        if (!isTablet) m_GameControls.setGameControls(statsTitle!!, viewComputerBoardButton1!!, movesLabel!!, movesCount!!, missesLabel!!, missesCount!!, hitsLabel!!, hitsCount!!, deadsLabel!!, deadCount!!)
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

        (activity as MainActivity).setActionBarTitle(getString(R.string.game))
        (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Game)
        return rootView
    }
}