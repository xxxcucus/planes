package com.planes_multiplayer.android

import android.content.Context
import android.widget.Button

class GameControlsAdapter(private val m_Context: Context) {
    private val m_CurStage = GameStages.BoardEditing
    private var m_PlaneRound: PlanesRoundInterface? = null
    private var m_GameBoards: GameBoardsAdapter? = null
    private var m_Tablet = false
    private var m_PlanesLayout: PlanesVerticalLayout? = null

    //Board Editing
    private var m_RotateButton: Button? = null
    private var m_LeftButton: Button? = null
    private var m_RightButton: Button? = null
    private var m_UpButton: Button? = null
    private var m_DownButton: Button? = null
    private var m_DoneButton: Button? = null

    //Game
    private var m_StatsTitle: ColouredSurfaceWithText? = null
    private var m_HitsTextView: ColouredSurfaceWithText? = null
    private var m_MissesTextView: ColouredSurfaceWithText? = null
    private var m_DeadTextView: ColouredSurfaceWithText? = null
    private var m_MovesTextView: ColouredSurfaceWithText? = null
    private var m_HitsLabel: ColouredSurfaceWithText? = null
    private var m_MissesLabel: ColouredSurfaceWithText? = null
    private var m_DeadLabel: ColouredSurfaceWithText? = null
    private var m_MovesLabel: ColouredSurfaceWithText? = null
    private var m_ViewComputerBoardButton1: TwoLineTextButtonWithState? = null

    //Start New Game
    private var m_WinnerTextView: ColouredSurfaceWithText? = null
    private var m_StartNewRound: TwoLineTextButton? = null
    private var m_ComputerWins: ColouredSurfaceWithText? = null
    private var m_PlayerWins: ColouredSurfaceWithText? = null
    private var m_Draws: ColouredSurfaceWithText? = null
    private var m_ComputerWinsLabel: ColouredSurfaceWithText? = null
    private var m_PlayerWinsLabel: ColouredSurfaceWithText? = null
    private var m_DrawsLabel: ColouredSurfaceWithText? = null
    private var m_ViewComputerBoardButton2: TwoLineTextButtonWithState? = null

    fun setBoardEditingControls(upButton: Button?, downButton: Button?, leftButton: Button?, rightButton: Button?, doneButton: Button?, rotateButton: Button?) {
        m_UpButton = upButton
        m_DownButton = downButton
        m_LeftButton = leftButton
        m_RightButton = rightButton
        m_DoneButton = doneButton
        m_RotateButton = rotateButton
        if (m_LeftButton != null) {
            m_LeftButton!!.setOnClickListener { m_GameBoards!!.movePlaneUp() }
        }
        if (m_RightButton != null) {
            m_RightButton!!.setOnClickListener { m_GameBoards!!.movePlaneDown() }
        }
        if (m_UpButton != null) {
            m_UpButton!!.setOnClickListener { m_GameBoards!!.movePlaneLeft() }
        }
        if (m_DownButton != null) {
            m_DownButton!!.setOnClickListener { m_GameBoards!!.movePlaneRight() }
        }
        if (m_RotateButton != null) {
            m_RotateButton!!.setOnClickListener { m_GameBoards!!.rotatePlane() }
        }
        if (m_DoneButton != null) {
            m_DoneButton!!.setOnClickListener {
                setGameStage()
                m_PlanesLayout!!.setGameStage()
                m_GameBoards!!.setGameStage()
                m_PlaneRound!!.doneClicked()
            }
        }
    }

    fun setGameControls(statsTitle: ColouredSurfaceWithText?, viewComputerBoardButton1: TwoLineTextButtonWithState?, movesLabel: ColouredSurfaceWithText?, movesCount: ColouredSurfaceWithText?, missesLabel: ColouredSurfaceWithText?, missesCount: ColouredSurfaceWithText?,
                        hitsLabel: ColouredSurfaceWithText?, hitsCount: ColouredSurfaceWithText?, deadsLabel: ColouredSurfaceWithText?, deadCount: ColouredSurfaceWithText?) {
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
        m_ViewComputerBoardButton1!!.setState("player", m_Context.resources.getString(R.string.view_player_board2))
        if (m_ViewComputerBoardButton1 != null) {
            m_ViewComputerBoardButton1!!.setOnClickListener {
                if (m_ViewComputerBoardButton1!!.currentStateName === "computer") {
                    m_GameBoards!!.setComputerBoard()
                    m_ViewComputerBoardButton1!!.setState("player", m_Context.resources.getString(R.string.view_player_board2))
                    val misses = m_PlaneRound!!.playerGuess_StatNoComputerMisses()
                    val hits = m_PlaneRound!!.playerGuess_StatNoComputerHits()
                    val dead = m_PlaneRound!!.playerGuess_StatNoComputerDead()
                    val moves = m_PlaneRound!!.playerGuess_StatNoComputerMoves()
                    m_MissesTextView!!.setText(Integer.toString(misses))
                    m_HitsTextView!!.setText(Integer.toString(hits))
                    m_DeadTextView!!.setText(Integer.toString(dead))
                    m_MovesTextView!!.setText(Integer.toString(moves))
                    m_StatsTitle!!.setText(m_Context.resources.getString(R.string.computer_stats))
                    m_PlanesLayout!!.setComputerBoard()
                } else if (m_ViewComputerBoardButton1!!.currentStateName === "player") {
                    m_GameBoards!!.setPlayerBoard()
                    m_ViewComputerBoardButton1!!.setState("computer", m_Context.resources.getString(R.string.view_computer_board2))
                    val misses = m_PlaneRound!!.playerGuess_StatNoPlayerMisses()
                    val hits = m_PlaneRound!!.playerGuess_StatNoPlayerHits()
                    val dead = m_PlaneRound!!.playerGuess_StatNoPlayerDead()
                    val moves = m_PlaneRound!!.playerGuess_StatNoPlayerMoves()
                    m_MissesTextView!!.setText(Integer.toString(misses))
                    m_HitsTextView!!.setText(Integer.toString(hits))
                    m_DeadTextView!!.setText(Integer.toString(dead))
                    m_MovesTextView!!.setText(Integer.toString(moves))
                    m_StatsTitle!!.setText(m_Context.resources.getString(R.string.player_stats))
                    m_PlanesLayout!!.setPlayerBoard()
                }
            }
        }
    }

    fun setStartNewGameControls(viewComputerBoardButton2: TwoLineTextButtonWithState?, startNewGameButton: TwoLineTextButton?,
                                computerWinsLabel: ColouredSurfaceWithText?, computerWinsCount: ColouredSurfaceWithText?,
                                playerWinsLabel: ColouredSurfaceWithText?, playerWinsCount: ColouredSurfaceWithText?,
                                drawsLabel: ColouredSurfaceWithText?, drawsCount: ColouredSurfaceWithText?, winnerText: ColouredSurfaceWithText?) {
        m_ViewComputerBoardButton2 = viewComputerBoardButton2
        m_StartNewRound = startNewGameButton
        m_ComputerWinsLabel = computerWinsLabel
        m_ComputerWins = computerWinsCount
        m_PlayerWinsLabel = playerWinsLabel
        m_PlayerWins = playerWinsCount
        m_WinnerTextView = winnerText
        m_Draws = drawsCount
        m_DrawsLabel = drawsLabel
        if (m_StartNewRound != null) {
            m_StartNewRound!!.setOnClickListener {
                m_PlaneRound!!.initRound()
                m_PlanesLayout!!.setBoardEditingStage()
                m_GameBoards!!.setBoardEditingStage()
                setBoardEditingStage()
            }
        }
        m_ViewComputerBoardButton2!!.setState("player", m_Context.resources.getString(R.string.view_player_board2))
        if (m_ViewComputerBoardButton2 != null) {
            m_ViewComputerBoardButton2!!.setOnClickListener {
                if (m_ViewComputerBoardButton2!!.currentStateName === "computer") {
                    m_GameBoards!!.setComputerBoard()
                    m_PlanesLayout!!.setComputerBoard()
                    m_ViewComputerBoardButton2!!.setState("player", m_Context.resources.getString(R.string.view_player_board2))
                } else if (m_ViewComputerBoardButton2!!.currentStateName === "player") {
                    m_GameBoards!!.setPlayerBoard()
                    m_PlanesLayout!!.setPlayerBoard()
                    m_ViewComputerBoardButton2!!.setState("computer", m_Context.resources.getString(R.string.view_computer_board2))
                }
            }
        }
    }

    fun setNewRoundStage() {
        val computer_wins = m_PlaneRound!!.playerGuess_StatNoComputerWins()
        val player_wins = m_PlaneRound!!.playerGuess_StatNoPlayerWins()
        val draws = m_PlaneRound!!.playerGuess_StatNoDraws()
        m_PlayerWins!!.setText(Integer.toString(player_wins))
        m_ComputerWins!!.setText(Integer.toString(computer_wins))
        m_Draws!!.setText(Integer.toString(draws))
        m_ViewComputerBoardButton2!!.setState("player", m_Context.resources.getString(R.string.view_player_board2))
        m_PlanesLayout!!.setComputerBoard()
    }

    fun setGameStage() {
        if (!m_Tablet) {
            m_GameBoards!!.setComputerBoard()
            m_ViewComputerBoardButton1!!.setState("player", m_Context.resources.getString(R.string.view_player_board2))
            m_StatsTitle!!.setText(m_Context.resources.getString(R.string.computer_stats))
            updateStats(true)
        } else {
        }
    }

    fun setBoardEditingStage() {}
    fun updateStats(isComputer: Boolean) {

        //on the computer board show the computer stats and
        //such that the player knows how far the computer is
        if (isComputer) {
            val misses = m_PlaneRound!!.playerGuess_StatNoComputerMisses()
            val hits = m_PlaneRound!!.playerGuess_StatNoComputerHits()
            val dead = m_PlaneRound!!.playerGuess_StatNoComputerDead()
            val moves = m_PlaneRound!!.playerGuess_StatNoComputerMoves()
            m_MissesTextView!!.setText(Integer.toString(misses))
            m_HitsTextView!!.setText(Integer.toString(hits))
            m_DeadTextView!!.setText(Integer.toString(dead))
            m_MovesTextView!!.setText(Integer.toString(moves))
        } else {
            val misses = m_PlaneRound!!.playerGuess_StatNoPlayerMisses()
            val hits = m_PlaneRound!!.playerGuess_StatNoPlayerHits()
            val dead = m_PlaneRound!!.playerGuess_StatNoPlayerDead()
            val moves = m_PlaneRound!!.playerGuess_StatNoPlayerMoves()
            m_MissesTextView!!.setText(Integer.toString(misses))
            m_HitsTextView!!.setText(Integer.toString(hits))
            m_DeadTextView!!.setText(Integer.toString(dead))
            m_MovesTextView!!.setText(Integer.toString(moves))
        }
    }

    fun roundEnds(isComputerWinner: Boolean, isDraw: Boolean) {
        m_PlanesLayout!!.setComputerBoard()
        m_PlanesLayout!!.setNewRoundStage()
        if (isComputerWinner) m_WinnerTextView!!.setText(m_Context.resources.getText(R.string.computer_winner).toString()) else m_WinnerTextView!!.setText(m_Context.resources.getText(R.string.player_winner).toString())
        if (isDraw) m_WinnerTextView!!.setText(m_Context.resources.getText(R.string.draw_result).toString())
        val computer_wins = m_PlaneRound!!.playerGuess_StatNoComputerWins()
        val player_wins = m_PlaneRound!!.playerGuess_StatNoPlayerWins()
        val draws = m_PlaneRound!!.playerGuess_StatNoDraws()
        m_PlayerWins!!.setText(Integer.toString(player_wins))
        m_ComputerWins!!.setText(Integer.toString(computer_wins))
        m_Draws!!.setText(Integer.toString(draws))
        m_ViewComputerBoardButton2!!.setState("player", m_Context.resources.getText(R.string.view_player_board2).toString())
    }

    fun setDoneEnabled(enabled: Boolean) {
        m_DoneButton!!.isEnabled = enabled
    }

    fun setGameSettings(planeRound: PlanesRoundInterface?, isTablet: Boolean) {
        m_PlaneRound = planeRound
        m_Tablet = isTablet
    }

    fun setGameBoards(gameBoards: GameBoardsAdapter?) {
        m_GameBoards = gameBoards
    }

    fun setPlanesLayout(planesLayout: PlanesVerticalLayout?) {
        m_PlanesLayout = planesLayout
    }
}