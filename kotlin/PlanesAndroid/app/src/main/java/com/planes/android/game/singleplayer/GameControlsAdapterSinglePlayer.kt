package com.planes.android.game.singleplayer

import android.content.Context
import android.widget.Button
import android.widget.ProgressBar
import com.planes.android.*
import com.planes.android.customviews.ColouredSurfaceWithText
import com.planes.android.customviews.ColouredSurfaceWithTwoLineText
import com.planes.android.customviews.TwoLineTextButton
import com.planes.android.customviews.TwoLineTextButtonWithState
import com.planes.single_player_engine.RoundEndStatus

class GameControlsAdapterSinglePlayer(private val context: Context, updateOptionsMenuLambda: () -> Unit) {
    private lateinit var m_PlaneRound: PlanesRoundInterface
    private lateinit var m_GameBoards: GameBoardsAdapterSinglePlayer
    private var m_Tablet = false
    private lateinit var m_PlanesLayout: PlanesVerticalLayoutSinglePlayer

    //Board Editing
    private lateinit var m_RotateButton: Button
    private lateinit var m_CancelBoardEditingButton: Button
    private lateinit var m_DoneButton: Button
    private lateinit var m_ResetBoardButton: TwoLineTextButton

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
    private var m_CancelGameButton: Button? = null

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

    private var m_UpdateOptionsMenuLambda: () -> Unit
    private var m_Context: Context
    init {
        m_UpdateOptionsMenuLambda = updateOptionsMenuLambda
        m_Context = context
    }
    fun setBoardEditingControls(
        doneButton: Button,
        rotateButton: Button,
        cancelButton: Button,
        resetBoardButton: TwoLineTextButton) {
        m_DoneButton = doneButton
        m_RotateButton = rotateButton
        m_CancelBoardEditingButton = cancelButton
        m_ResetBoardButton = resetBoardButton

        if (this::m_RotateButton.isInitialized) {
            m_RotateButton.setOnClickListener { m_GameBoards.rotatePlane() }
        }

        if (this::m_CancelBoardEditingButton.isInitialized) {
            m_CancelBoardEditingButton.setOnClickListener {
                cancelRound()
                m_PlaneRound.cancelRound()
                setNewRoundStage()
                m_PlanesLayout.setNewRoundStage()
                m_GameBoards.setNewRoundStage()
            }
        }

        if (this::m_DoneButton.isInitialized) {
            m_DoneButton.setOnClickListener {
                setGameStage()
                m_PlanesLayout.setGameStage()
                m_GameBoards.setGameStage()
                m_PlaneRound.doneClicked()
            }
        }

        if (this::m_ResetBoardButton.isInitialized) {
            m_ResetBoardButton.setOnClickListener {
                m_PlaneRound.initRound()
                m_GameBoards.setBoardEditingStage()
            }
        }

    }

    fun setGameControls(statsTitle: ColouredSurfaceWithTwoLineText, viewComputerBoardButton1: TwoLineTextButtonWithState,
                        movesLabel: ColouredSurfaceWithText, movesCount: ColouredSurfaceWithText, missesLabel: ColouredSurfaceWithText,
                        missesCount: ColouredSurfaceWithText,
                        hitsLabel: ColouredSurfaceWithText, hitsCount: ColouredSurfaceWithText, deadsLabel: ColouredSurfaceWithText,
                        deadCount: ColouredSurfaceWithText, cancelButton: Button?
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
        m_CancelGameButton = cancelButton

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
                    m_MissesTextView.setText(misses.toString())
                    m_HitsTextView.setText(hits.toString())
                    m_DeadTextView.setText(dead.toString())
                    m_MovesTextView.setText(moves.toString())
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
                    m_MissesTextView.setText(misses.toString())
                    m_HitsTextView.setText(hits.toString())
                    m_DeadTextView.setText(dead.toString())
                    m_MovesTextView.setText(moves.toString())
                    m_StatsTitle.setText(m_Context.resources.getString(R.string.player_stats1), m_Context.resources.getString(
                        R.string.player_stats2
                    ))
                    m_PlanesLayout.setPlayerBoard()
                }
            }
        }

        if (m_CancelGameButton != null) {
            m_CancelGameButton!!.setOnClickListener {
                cancelRound()
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
        m_StartNewRound.setOnClickListener {
            m_PlaneRound.initRound()
            m_PlanesLayout.setBoardEditingStage()
            m_GameBoards.setBoardEditingStage()
            setBoardEditingStage()
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
        m_PlayerWins.setText(player_wins.toString())
        m_ComputerWins.setText(computer_wins.toString())
        m_Draws.setText(draws.toString())

        var winnerText  =
            when(m_PlaneRound.getRoundEndStatus()) {
                RoundEndStatus.Cancelled.value -> m_Context.resources.getString(R.string.round_cancelled)
                RoundEndStatus.PlayerWins.value -> m_Context.resources.getString(R.string.player_winner)
                RoundEndStatus.ComputerWins.value -> m_Context.resources.getString(R.string.computer_winner)
                else -> {
                    m_Context.resources.getString(R.string.draw_result)
                }
            }

        m_WinnerTextView.setText(winnerText)

        m_ViewComputerBoardButton2.setState("player", m_Context.resources.getString(R.string.view_player_board2))
        m_PlanesLayout.setComputerBoard()
        m_UpdateOptionsMenuLambda()
    }

    fun setGameStage() {
        if (!m_Tablet) {
            m_GameBoards.setComputerBoard()
            m_ViewComputerBoardButton1.setState("player", m_Context.resources.getString(R.string.view_player_board2))
            m_StatsTitle.setText(m_Context.resources.getString(R.string.computer_stats1), m_Context.resources.getString(
                R.string.computer_stats2
            ))
            updateStats(true)
        }
        m_UpdateOptionsMenuLambda()
    }

    fun setBoardEditingStage() {
        m_UpdateOptionsMenuLambda()
    }
    fun updateStats(isComputer: Boolean) {

        //on the computer board show the computer stats and
        //such that the player knows how far the computer is
        if (isComputer) {
            val misses = m_PlaneRound.playerGuess_StatNoComputerMisses()
            val hits = m_PlaneRound.playerGuess_StatNoComputerHits()
            val dead = m_PlaneRound.playerGuess_StatNoComputerDead()
            val moves = m_PlaneRound.playerGuess_StatNoComputerMoves()
            m_MissesTextView.setText(misses.toString())
            m_HitsTextView.setText(hits.toString())
            m_DeadTextView.setText(dead.toString())
            m_MovesTextView.setText(moves.toString())
        } else {
            val misses = m_PlaneRound.playerGuess_StatNoPlayerMisses()
            val hits = m_PlaneRound.playerGuess_StatNoPlayerHits()
            val dead = m_PlaneRound.playerGuess_StatNoPlayerDead()
            val moves = m_PlaneRound.playerGuess_StatNoPlayerMoves()
            m_MissesTextView.setText(misses.toString())
            m_HitsTextView.setText(hits.toString())
            m_DeadTextView.setText(dead.toString())
            m_MovesTextView.setText(moves.toString())
        }
    }

    fun cancelRound() {
        m_WinnerTextView.setText(m_Context.resources.getString(R.string.round_cancelled).toString())
        m_PlaneRound.cancelRound()
        setNewRoundStage()
        m_PlanesLayout.setNewRoundStage()
        m_GameBoards.setNewRoundStage()
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
        m_PlayerWins.setText(player_wins.toString())
        m_ComputerWins.setText(computer_wins.toString())
        m_Draws.setText(draws.toString())
        m_ViewComputerBoardButton2.setState("player", m_Context.resources.getText(R.string.view_player_board2).toString())
        m_UpdateOptionsMenuLambda()
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