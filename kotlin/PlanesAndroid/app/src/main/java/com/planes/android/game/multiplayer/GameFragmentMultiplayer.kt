package com.planes.android.game.multiplayer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.planes.android.*
import com.planes.android.customviews.*
import com.planes.android.game.singleplayer.*
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.single_player_engine.PlanesRoundJava

class GameFragmentMultiplayer : Fragment() {

    private lateinit var m_PlaneRound: MultiplayerRoundInterface
    private lateinit var m_GameBoards: GameBoardsAdapterMultiplayer
    private lateinit var m_GameControls: GameControlsAdapterMultiplayer
    private lateinit var m_PlanesLayout: PlanesVerticalLayoutSinglePlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        m_PlaneRound = MultiplayerRoundJava()
        (m_PlaneRound as MultiplayerRoundJava).createPlanesRound()

        m_GameControls = GameControlsAdapterMultiplayer(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_game_multiplayer, container, false)

        m_PlanesLayout = rootView.findViewById<View>(R.id.planes_layout) as PlanesVerticalLayoutSinglePlayer

        var isTablet = false
        var isHorizontal = false
        val linearLayout = rootView.findViewById<View>(R.id.rootView) as LinearLayout
        if (linearLayout.tag.toString().contains("tablet")) {
            isTablet = true
        }
        if (linearLayout.tag.toString().contains("horizontal")) {
            isHorizontal = true
        }
        m_GameBoards = if (showTwoBoards(isTablet)) {
            val playerBoard = rootView.findViewById<View>(R.id.player_board) as GameBoardMultiplayer
            playerBoard.setGameSettings(m_PlaneRound, true)
            playerBoard.setPlayerBoard()
            val computerBoard = rootView.findViewById<View>(R.id.computer_board) as GameBoardMultiplayer
            computerBoard.setGameSettings(m_PlaneRound, true)
            computerBoard.setComputerBoard()
            GameBoardsAdapterMultiplayer(playerBoard, computerBoard)
        } else {
            val gameBoard = rootView.findViewById<View>(R.id.game_boards) as GameBoardMultiplayer
            gameBoard.setGameSettings(m_PlaneRound, false)
            GameBoardsAdapterMultiplayer(gameBoard)
        }

        //Board Editing Buttons
        val doneButton = rootView.findViewById<View>(R.id.done_button) as Button
        val rotateButton = rootView.findViewById<View>(R.id.rotate_button) as Button
        val cancelBoardEditingButton = rootView.findViewById<View>(R.id.cancel_boardediting) as Button
        val progressBarBoardEditing = rootView.findViewById<View>(R.id.ProgressBarBoardEditing) as ProgressBar

        //Game Stage
        val statsTitle = rootView.findViewById<View>(R.id.stats_title_label) as TwoLineTextButton
        val viewOpponentBoardButton1 = rootView.findViewById<View>(R.id.view_opponent_board1) as TwoLineTextButtonWithState
        val cancelGameButton = rootView.findViewById<View>(R.id.cancel_game) as TextButton
        val progressBarGameButton = rootView.findViewById<View>(R.id.ProgressBarGame) as ProgressBar

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

        m_GameControls.setBoardEditingControls(doneButton, rotateButton, cancelBoardEditingButton, progressBarBoardEditing)
        if (!showTwoBoards(isTablet)) m_GameControls.setGameControls(statsTitle, viewOpponentBoardButton1, cancelBoardEditingButton, progressBarGameButton)
        m_GameControls.setStartNewGameControls(viewComputerBoardButton2, startNewGameButton, computerWinsLabel, computerWinsCount, playerWinsLabel, playerWinsCount, drawsLabel, drawsCount, winnerText)
        m_GameControls.setGameSettings(m_PlaneRound, isTablet)
        m_GameControls.setGameBoards(m_GameBoards)
        m_GameControls.setPlanesLayout(m_PlanesLayout)
        m_GameBoards.setGameControls(m_GameControls)
        when ((m_PlaneRound as MultiplayerRoundJava).getGameStage()) {
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

    fun showTwoBoards(isTablet: Boolean): Boolean {
        return false
    }
}