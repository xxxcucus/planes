package com.planes.android.game.singleplayer

import android.content.Context
import android.widget.Button
import com.planes.android.*
import com.planes.android.customviews.ColouredSurfaceWithText
import com.planes.android.customviews.ColouredSurfaceWithTwoLineText
import com.planes.android.customviews.TwoLineTextButton
import com.planes.android.customviews.TwoLineTextButtonWithState
import com.planes.single_player_engine.GameStages

class GameControlsAdapterSinglePlayer(private val m_Context: Context) {
    private val m_CurStage = GameStages.BoardEditing
    private lateinit var m_PlaneRound: PlanesRoundInterface
    private lateinit var m_GameBoards: GameBoardsAdapterSinglePlayer
    private var m_Tablet = false
    private lateinit var m_PlanesLayout: PlanesVerticalLayoutSinglePlayer

    //Board Editing
    private lateinit var m_RotateButton: Button
    private lateinit var m_LeftButton: Button
    private lateinit var m_RightButton: Button
    private lateinit var m_UpButton: Button
    private lateinit var m_DownButton: Button
    private lateinit var m_DoneButton: Button

    //Game
    private lateinit var m_StatsTitle: ColouredSurfaceWithTwoLineText
    private lateinit var m_HitsTextView: ColouredSurfaceWithText
    private lateinit var m_MissesTextView: ColouredSurfaceWithText
    private lateinit var m_DeadTextView: ColouredSurfaceWithText
    private lateinit var m_MovesTextView: ColouredSurfaceWithText
    private lateinit var m_HitsLabel: ColouredSurfaceWithText
    private lateinit var m_MissesLabel: ColouredSurfaceWithText
    private lateinit var m_DeadLabel: ColouredSurfaceWithText
    private lateinit var m_MovesLabel: ColouredSurfaceWithText
    private lateinit var m_ViewComputerBoardButton1: TwoLineTextButtonWithState

    //Start New Game
    private lateinit var m_WinnerTextView: ColouredSurfaceWithText
    private lateinit var m_StartNewRound: TwoLineTextButton
    private lateinit var m_ComputerWins: ColouredSurfaceWithText
    private lateinit var m_PlayerWins: ColouredSurfaceWithText
    private lateinit var m_Draws: ColouredSurfaceWithText
    private lateinit var m_ComputerWinsLabel: ColouredSurfaceWithText
    private lateinit var m_PlayerWinsLabel: ColouredSurfaceWithText
    private lateinit var m_DrawsLabel: ColouredSurfaceWithText
    private lateinit var m_ViewComputerBoardButton2: TwoLineTextButtonWithState

    fun setBoardEditingControls(upButton: Button, downButton: Button, leftButton: Button, rightButton: Button, doneButton: Button, rotateButton: Button) {
        m_UpButton = upButton
        m_DownButton = downButton
        m_LeftButton = leftButton
        m_RightButton = rightButton
        m_DoneButton = doneButton
        m_RotateButton = rotateButton
        if (this::m_LeftButton.isInitialized) {
            m_LeftButton.setOnClickListener { m_GameBoards.movePlaneUp() }
        }
        if (this::m_RightButton.isInitialized) {
            m_RightButton.setOnClickListener { m_GameBoards.movePlaneDown() }
        }
        if (this::m_UpButton.isInitialized) {
            m_UpButton.setOnClickListener { m_GameBoards.movePlaneLeft() }
        }
        if (this::m_DownButton.isInitialized) {
            m_DownButton.setOnClickListener { m_GameBoards.movePlaneRight() }
        }
        if (this::m_RotateButton.isInitialized) {
            m_RotateButton.setOnClickListener { m_GameBoards.rotatePlane() }
        }
        if (this::m_DoneButton.isInitialized) {
            m_DoneButton.setOnClickListener {
                setGameStage()
                m_PlanesLayout.setGameStage()
                m_GameBoards.setGameStage()
                m_PlaneRound.doneClicked()
            }
        }
    }

    fun setGameControls(statsTitle: ColouredSurfaceWithTwoLineText, viewComputerBoardButton1: TwoLineTextButtonWithState, movesLabel: ColouredSurfaceWithText, movesCount: ColouredSurfaceWithText, missesLabel: ColouredSurfaceWithText, missesCount: ColouredSurfaceWithText,
                        hitsLabel: ColouredSurfaceWithText, hitsCount: ColouredSurfaceWithText, deadsLabel: ColouredSurfaceWithText, deadCount: ColouredSurfaceWithText
    ) {
        m_StatsTitle = statsTitle
        m_ViewComputerBoardButton1 = viewComputerBoardButton1
        m_MovesLabel = movesLabel
        m_MovesTextView = movesCount
        m_MissesLabel = missesLabel
        m_MissesTextView = missesCount
        m_HitsLabel = hitsLabel
        m_HitsTextView = hitsCount
        m_DeadLabel = deadsLabel
        m_DeadTextView = deadCount
        m_ViewComputerBoardButton1.setState("player", m_Context.resources.getString(R.string.view_player_board2))
        if (this::m_ViewComputerBoardButton1.isInitialized) {
            m_ViewComputerBoardButton1.setOnClickListener {
                if (m_ViewComputerBoardButton1.currentStateName === "computer") {
                    m_GameBoards.setComputerBoard()
                    m_ViewComputerBoardButton1.setState("player", m_Context.resources.getString(R.string.view_player_board2))
                    val misses = m_PlaneRound.playerGuess_StatNoComputerMisses()
                    val hits = m_PlaneRound.playerGuess_StatNoComputerHits()
                    val dead = m_PlaneRound.playerGuess_StatNoComputerDead()
                    val moves = m_PlaneRound.playerGuess_StatNoComputerMoves()
                    m_MissesTextView.setText(Integer.toString(misses))
                    m_HitsTextView.setText(Integer.toString(hits))
                    m_DeadTextView.setText(Integer.toString(dead))
                    m_MovesTextView.setText(Integer.toString(moves))
                    m_StatsTitle.setText(m_Context.resources.getString(R.string.computer_stats1), m_Context.resources.getString(
                        R.string.computer_stats2
                    ))
                    m_PlanesLayout.setComputerBoard()
                } else if (m_ViewComputerBoardButton1.currentStateName === "player") {
                    m_GameBoards.setPlayerBoard()
                    m_ViewComputerBoardButton1.setState("computer", m_Context.resources.getString(R.string.view_computer_board2))
                    val misses = m_PlaneRound.playerGuess_StatNoPlayerMisses()
                    val hits = m_PlaneRound.playerGuess_StatNoPlayerHits()
                    val dead = m_PlaneRound.playerGuess_StatNoPlayerDead()
                    val moves = m_PlaneRound.playerGuess_StatNoPlayerMoves()
                    m_MissesTextView.setText(Integer.toString(misses))
                    m_HitsTextView.setText(Integer.toString(hits))
                    m_DeadTextView.setText(Integer.toString(dead))
                    m_MovesTextView.setText(Integer.toString(moves))
                    m_StatsTitle.setText(m_Context.resources.getString(R.string.player_stats1), m_Context.resources.getString(
                        R.string.player_stats2
                    ))
                    m_PlanesLayout.setPlayerBoard()
                }
            }
        }
    }

    fun setStartNewGameControls(viewComputerBoardButton2: TwoLineTextButtonWithState, startNewGameButton: TwoLineTextButton,
                                computerWinsLabel: ColouredSurfaceWithText, computerWinsCount: ColouredSurfaceWithText,
                                playerWinsLabel: ColouredSurfaceWithText, playerWinsCount: ColouredSurfaceWithText,
                                drawsLabel: ColouredSurfaceWithText, drawsCount: ColouredSurfaceWithText, winnerText: ColouredSurfaceWithText
    ) {
        m_ViewComputerBoardButton2 = viewComputerBoardButton2
        m_StartNewRound = startNewGameButton
        m_ComputerWinsLabel = computerWinsLabel
        m_ComputerWins = computerWinsCount
        m_PlayerWinsLabel = playerWinsLabel
        m_PlayerWins = playerWinsCount
        m_WinnerTextView = winnerText
        m_Draws = drawsCount
        m_DrawsLabel = drawsLabel
        if (this::m_StartNewRound.isInitialized != null) {
            m_StartNewRound.setOnClickListener {
                m_PlaneRound.initRound()
                m_PlanesLayout.setBoardEditingStage()
                m_GameBoards.setBoardEditingStage()
                setBoardEditingStage()
            }
        }
        m_ViewComputerBoardButton2.setState("player", m_Context.resources.getString(R.string.view_player_board2))
        if (this::m_ViewComputerBoardButton2.isInitialized) {
            m_ViewComputerBoardButton2.setOnClickListener {
                if (m_ViewComputerBoardButton2.currentStateName === "computer") {
                    m_GameBoards.setComputerBoard()
                    m_PlanesLayout.setComputerBoard()
                    m_ViewComputerBoardButton2.setState("player", m_Context.resources.getString(R.string.view_player_board2))
                } else if (m_ViewComputerBoardButton2.currentStateName === "player") {
                    m_GameBoards.setPlayerBoard()
                    m_PlanesLayout.setPlayerBoard()
                    m_ViewComputerBoardButton2.setState("computer", m_Context.resources.getString(R.string.view_computer_board2))
                }
            }
        }
    }

    fun setNewRoundStage() {
        val computer_wins = m_PlaneRound.playerGuess_StatNoComputerWins()
        val player_wins = m_PlaneRound.playerGuess_StatNoPlayerWins()
        val draws = m_PlaneRound.playerGuess_StatNoDraws()
        m_PlayerWins.setText(Integer.toString(player_wins))
        m_ComputerWins.setText(Integer.toString(computer_wins))
        m_Draws.setText(Integer.toString(draws))
        m_ViewComputerBoardButton2.setState("player", m_Context.resources.getString(R.string.view_player_board2))
        m_PlanesLayout.setComputerBoard()
    }

    fun setGameStage() {
        if (!m_Tablet) {
            m_GameBoards.setComputerBoard()
            m_ViewComputerBoardButton1.setState("player", m_Context.resources.getString(R.string.view_player_board2))
            m_StatsTitle.setText(m_Context.resources.getString(R.string.computer_stats1), m_Context.resources.getString(
                R.string.computer_stats2
            ))
            updateStats(true)
        } else {
        }
    }

    fun setBoardEditingStage() {}
    fun updateStats(isComputer: Boolean) {

        //on the computer board show the computer stats and
        //such that the player knows how far the computer is
        if (isComputer) {
            val misses = m_PlaneRound.playerGuess_StatNoComputerMisses()
            val hits = m_PlaneRound.playerGuess_StatNoComputerHits()
            val dead = m_PlaneRound.playerGuess_StatNoComputerDead()
            val moves = m_PlaneRound.playerGuess_StatNoComputerMoves()
            m_MissesTextView.setText(Integer.toString(misses))
            m_HitsTextView.setText(Integer.toString(hits))
            m_DeadTextView.setText(Integer.toString(dead))
            m_MovesTextView.setText(Integer.toString(moves))
        } else {
            val misses = m_PlaneRound.playerGuess_StatNoPlayerMisses()
            val hits = m_PlaneRound.playerGuess_StatNoPlayerHits()
            val dead = m_PlaneRound.playerGuess_StatNoPlayerDead()
            val moves = m_PlaneRound.playerGuess_StatNoPlayerMoves()
            m_MissesTextView.setText(Integer.toString(misses))
            m_HitsTextView.setText(Integer.toString(hits))
            m_DeadTextView.setText(Integer.toString(dead))
            m_MovesTextView.setText(Integer.toString(moves))
        }
    }

    fun roundEnds(isComputerWinner: Boolean, isDraw: Boolean) {
        m_PlanesLayout.setComputerBoard()
        m_PlanesLayout.setNewRoundStage()
        if (isComputerWinner) m_WinnerTextView.setText(m_Context.resources.getText(R.string.computer_winner).toString()) else m_WinnerTextView.setText(m_Context.resources.getText(
            R.string.player_winner
        ).toString())
        if (isDraw) m_WinnerTextView.setText(m_Context.resources.getText(R.string.draw_result).toString())
        val computer_wins = m_PlaneRound.playerGuess_StatNoComputerWins()
        val player_wins = m_PlaneRound.playerGuess_StatNoPlayerWins()
        val draws = m_PlaneRound.playerGuess_StatNoDraws()
        m_PlayerWins.setText(Integer.toString(player_wins))
        m_ComputerWins.setText(Integer.toString(computer_wins))
        m_Draws.setText(Integer.toString(draws))
        m_ViewComputerBoardButton2.setState("player", m_Context.resources.getText(R.string.view_player_board2).toString())
    }

    fun setDoneEnabled(enabled: Boolean) {
        m_DoneButton.isEnabled = enabled
    }

    fun setGameSettings(planeRound: PlanesRoundInterface, isTablet: Boolean) {
        m_PlaneRound = planeRound
        m_Tablet = isTablet
    }

    fun setGameBoards(gameBoards: GameBoardsAdapterSinglePlayer) {
        m_GameBoards = gameBoards
    }

    fun setPlanesLayout(planesLayout: PlanesVerticalLayoutSinglePlayer) {
        m_PlanesLayout = planesLayout
    }
}