package com.planes.android.game.singleplayer

import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.planes.android.*
import com.planes.android.customviews.ColouredSurfaceWithText
import com.planes.android.customviews.ColouredSurfaceWithTwoLineText
import com.planes.android.customviews.TwoLineTextButton
import com.planes.android.customviews.TwoLineTextButtonWithState
import com.planes.single_player_engine.PlanesRoundJava



class GameFragmentSinglePlayer : Fragment() {

    lateinit var m_PlaneRound: PlanesRoundInterface
    private lateinit var m_GameBoards: GameBoardsAdapterSinglePlayer
    lateinit var m_GameControls: GameControlsAdapterSinglePlayer
    private lateinit var m_PlanesLayout: PlanesVerticalLayoutSinglePlayer

    override fun onAttach(context: Context) {
        super.onAttach(context)

        m_PlaneRound = PlanesRoundJava()
        (m_PlaneRound as PlanesRoundJava).createPlanesRound()

        m_GameControls = GameControlsAdapterSinglePlayer(context, ::updateOptionsMenu)
        //TODO: give lambda to update options menu function as parameter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_game_singleplayer, container, false)

        m_PlanesLayout = rootView.findViewById<View>(R.id.planes_layout) as PlanesVerticalLayoutSinglePlayer

        var isTablet = false
        val linearLayout = rootView.findViewById<View>(R.id.rootView) as LinearLayout
        if (linearLayout.tag.toString().contains("tablet")) {
            isTablet = true
        }
        m_GameBoards = if (isTablet) {
            val playerBoard = rootView.findViewById<View>(R.id.player_board) as GameBoardSinglePlayer
            playerBoard.setGameSettings(m_PlaneRound, true)
            playerBoard.setPlayerBoard()
            val computerBoard = rootView.findViewById<View>(R.id.computer_board) as GameBoardSinglePlayer
            computerBoard.setGameSettings(m_PlaneRound, true)
            computerBoard.setComputerBoard()
            GameBoardsAdapterSinglePlayer(playerBoard, computerBoard)
        } else {
            val gameBoard = rootView.findViewById<View>(R.id.game_boards) as GameBoardSinglePlayer
            gameBoard.setGameSettings(m_PlaneRound, false)
            GameBoardsAdapterSinglePlayer(gameBoard)
        }

        //Board Editing Buttons
        val doneButton = rootView.findViewById<View>(R.id.done_button) as Button
        val rotateButton = rootView.findViewById<View>(R.id.rotate_button) as Button
        val cancelBoardEditingButton = rootView.findViewById<View>(R.id.cancel_boardediting) as Button
        val resetBoardButton = rootView.findViewById<View>(R.id.reset_board) as TwoLineTextButton

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
        val cancelGameButton = rootView.findViewById<View>(R.id.cancel_game) as Button?

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

        m_GameControls.setBoardEditingControls(doneButton, rotateButton, cancelBoardEditingButton, resetBoardButton)
        if (!isTablet) m_GameControls.setGameControls(statsTitle!!, viewComputerBoardButton1!!, movesLabel!!, movesCount!!,
            missesLabel!!, missesCount!!, hitsLabel!!, hitsCount!!, deadsLabel!!, deadCount!!, cancelGameButton)
        m_GameControls.setStartNewGameControls(viewComputerBoardButton2, startNewGameButton, computerWinsLabel, computerWinsCount, playerWinsLabel, playerWinsCount, drawsLabel, drawsCount, winnerText)
        m_GameControls.setGameSettings(m_PlaneRound, isTablet)
        m_GameControls.setGameBoards(m_GameBoards)
        m_GameControls.setPlanesLayout(m_PlanesLayout)
        m_GameBoards.setGameControls(m_GameControls)

        reinitializeFromState()

        if (activity is MainActivity) {
            (activity as MainActivity).setActionBarTitle(getString(R.string.game))
            (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Game)
        }
        return rootView
    }

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        if (activity is MainActivity)
            return super.onGetLayoutInflater(savedInstanceState)

        val inflater = super.onGetLayoutInflater(savedInstanceState)
        val contextThemeWrapper: Context = ContextThemeWrapper(requireContext(), R.style.MyAppTheme)
        return inflater.cloneInContext(contextThemeWrapper)
    }

    fun reinitializeFromState() {
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

        if (activity is MainActivity)
            (activity as MainActivity).updateOptionsMenu()
    }

    fun updateOptionsMenu() {
        if (activity is MainActivity)
            (activity as MainActivity).updateOptionsMenu()
    }
    fun cancelRound() {
        m_GameControls.cancelRound()
    }
}