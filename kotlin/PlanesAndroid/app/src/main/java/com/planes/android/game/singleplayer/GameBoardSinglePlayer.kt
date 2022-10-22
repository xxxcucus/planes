package com.planes.android.game.singleplayer

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.GridLayout
import com.planes.android.PlanesRoundInterface
import com.planes.android.R
import com.planes.android.game.common.GameBoardInterface
import com.planes.android.game.common.GridSquare
import com.planes.single_player_engine.GameStages
import java.util.*
import kotlin.math.abs
import kotlin.math.min

class GameBoardSinglePlayer : GridLayout, GameBoardInterface {
    internal inner class PositionBoardPane(i: Int, j: Int) {
        private var x = 0
        private var y = 0

        init {
            x = i
            y = j
        }

        override fun equals(other: Any?): Boolean {
            if (other == null) return false
            if (this === other) return true
            if (this.javaClass != other.javaClass) return false
            val pos = other as PositionBoardPane
            return x == pos.x && y == pos.y
        }

        override fun hashCode(): Int {
            return 100 * x + y
        }

        fun x(): Int {
            return x
        }

        fun y(): Int {
            return y
        }
    }

    private lateinit var m_GridSquares: HashMap<PositionBoardPane, GridSquare>
    private lateinit var m_PlaneRound: PlanesRoundInterface
    private var longPressTimeInMillis: Long = 300
    private val m_Padding = 0
    private var m_IsComputer = false
    private val m_MinPlaneBodyColor = 0
    private val m_MaxPlaneBodyColor = 200
    private var m_GameStage = GameStages.BoardEditing
    private var m_GRows = 10
    private var m_GCols = 10
    private var m_PlaneNo = 3
    private var m_ColorStep = 50
    private var m_Context: Context
    private var m_Selected = 0
    private lateinit var m_GameControls: GameControlsAdapterSinglePlayer
    private var m_Tablet = false
    private lateinit var m_SiblingBoard: GameBoardSinglePlayer
    private var m_GridSquareSize = 0

    constructor(context: Context) : super(context) {
        m_Context = context
        init(m_Context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        m_Context = context
        init(m_Context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        m_Context = context
        init(m_Context)
    }

    fun setGameSettings(planeRound: PlanesRoundInterface, isTablet: Boolean) {
        m_PlaneRound = planeRound
        m_GRows = m_PlaneRound.getRowNo()
        m_GCols = m_PlaneRound.getColNo()
        m_PlaneNo = m_PlaneRound.getPlaneNo()
        m_ColorStep = (m_MaxPlaneBodyColor - m_MinPlaneBodyColor) / m_PlaneNo
        m_Tablet = isTablet
        // init(m_Context);
        updateBoards()
    }

    /**
     * Repositioning of grid squares inside the game board when the layout changes
     * (e.g. when going from one game stage to the other)
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        // These are the far left and right edges in which we are performing layout.
        val leftPos = paddingLeft
        val rightPos = right - left - paddingRight

        // These are the top and bottom edges in which we are performing layout.
        val topPos = paddingTop
        val bottomPos = bottom - top - paddingBottom
        val spacing = 0
        val newWidth = (min(bottomPos - topPos, rightPos - leftPos) - spacing) / (m_GRows + 2 * m_Padding)
        m_GridSquareSize = newWidth
        var verticalOffset = 0
        var horizontalOffset = 0
        if (bottomPos - topPos > rightPos - leftPos) {
            verticalOffset = (bottomPos - topPos - rightPos + leftPos) / 2
        } else {
            horizontalOffset = (rightPos - leftPos - bottomPos + topPos) / 2
        }
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i) as GridSquare
            child.width = newWidth
            // Log.d("Planes", "Set width " + i);
            val childLeft = leftPos + horizontalOffset + spacing / 2 + child.getColNo() * newWidth
            val childTop = topPos + verticalOffset + spacing / 2 + child.getRowNo() * newWidth
            val childRight = leftPos + horizontalOffset + spacing / 2 + child.getColNo() * newWidth + newWidth
            val childBottom = topPos + verticalOffset + spacing / 2 + child.getRowNo() * newWidth + newWidth
            child.layout(childLeft, childTop, childRight, childBottom)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // TODO: is this correct ?
        if (m_GridSquareSize == 0) super.onMeasure(widthMeasureSpec, heightMeasureSpec) else setMeasuredDimension((m_GRows + 2 * m_Padding) * m_GridSquareSize, (m_GRows + 2 * m_Padding) * m_GridSquareSize)
    }

    private fun init(context: Context) {
        val gridSize = 1
        rowCount = m_GRows + 2 * m_Padding
        columnCount = m_GCols + 2 * m_Padding
        m_GridSquares = HashMap()
        for (i in 0 until m_GRows + 2 * m_Padding) {
            for (j in 0 until m_GCols + 2 * m_Padding) {
                val gs = GridSquare(context, gridSize)
                if (i < m_Padding || i >= m_GRows + m_Padding || j < m_Padding || j >= m_GCols + m_Padding) gs.setBackgroundColor(Color.YELLOW) else gs.setBackgroundColor(
                    resources.getColor(
                        R.color.aqua
                    )
                )
                gs.setGuess(-1)
                gs.setRowCount(m_GRows + 2 * m_Padding)
                gs.setColCount(m_GCols + 2 * m_Padding)
                gs.setRow(i)
                gs.setColumn(j)
                gs.setParent(this)
                val params = LayoutParams(spec(j, 1), spec(i, 1))
                addView(gs, params)
                val position = PositionBoardPane(i, j)
                m_GridSquares[position] = gs
            }
        }
    }

    private fun updateBoards() {
        // draw the squares background
        for (i in 0 until m_GRows + 2 * m_Padding) {
            for (j in 0 until m_GCols + 2 * m_Padding) {
                val c = m_GridSquares[PositionBoardPane(i, j)]
                c!!.setGuess(-1)
                c.setBackgroundColor(computeSquareBackgroundColor(i, j))
                c.invalidate()
            }
        } // display background of square; double for loop

        val count = if (m_IsComputer) m_PlaneRound.getPlayerGuessesNo() else m_PlaneRound.getComputerGuessesNo()
        println("$count guesses")
        for (i in 0 until count) {
            var row: Int
            var col: Int
            var type: Int
            if (m_IsComputer) {
                row = m_PlaneRound.getPlayerGuessRow(i)
                col = m_PlaneRound.getPlayerGuessCol(i)
                type = m_PlaneRound.getPlayerGuessType(i)
            } else {
                row = m_PlaneRound.getComputerGuessRow(i)
                col = m_PlaneRound.getComputerGuessCol(i)
                type = m_PlaneRound.getComputerGuessType(i)
            }
            val c = m_GridSquares[PositionBoardPane(row + m_Padding, col + m_Padding)]
            // System.out.println("Guess type " + type);
            c!!.setGuess(type)
            c.invalidate()
        }
    }

    private fun computeSquareBackgroundColor(i: Int, j: Int): Int {
        var squareColor = if (i < m_Padding || i >= m_GRows + m_Padding || j < m_Padding || j >= m_GCols + m_Padding) {
            Color.YELLOW
        } else {
            resources.getColor(R.color.aqua)
        }
        if (!m_IsComputer || m_IsComputer && m_GameStage === GameStages.GameNotStarted) {
            // if (true) {
            val type = m_PlaneRound.getPlaneSquareType(i - m_Padding, j - m_Padding, if (m_IsComputer) 1 else 0)
            when (type) {
                -1 -> squareColor = Color.RED
                -2 -> squareColor = Color.GREEN
                0 -> {
                }
                else -> squareColor = if (type - 1 == m_Selected && !m_IsComputer && m_GameStage === GameStages.BoardEditing) {
                    Color.BLUE
                } else {
                    val grayCol = m_MinPlaneBodyColor + type * m_ColorStep
                    Color.rgb(grayCol, grayCol, grayCol)
                }
            }
        }
        return squareColor
    }

    override fun touchEventUp(row: Int, col: Int, row_diff: Int, col_diff: Int, touchedTime: Long) {
        if (row_diff == 0 && col_diff == 0) {
            if (!m_IsComputer && m_GameStage === GameStages.BoardEditing && touchedTime > longPressTimeInMillis) {
                rotatePlane()
            } else {
                touchInASingleSquare(row, col)
            }
        } else {
            touchInMoreSquares(row, col, row + row_diff, col + col_diff)
        }
    }

    // simple touch
    private fun touchInASingleSquare(row: Int, col: Int) {
        if (!m_IsComputer && m_GameStage === GameStages.BoardEditing) {
            val type = m_PlaneRound.getPlaneSquareType(row - m_Padding, col - m_Padding, if (m_IsComputer) 1 else 0)
            if (type > 0) m_Selected = type - 1
            updateBoards()
        }
        if (m_IsComputer && m_GameStage === GameStages.Game) {
            if (m_IsComputer) {
                println("Player guess")
                if (m_PlaneRound.playerGuessAlreadyMade(col - m_Padding, row - m_Padding) != 0) {
                    println("Player guess already made")
                    return
                }
                m_PlaneRound.playerGuess(col - m_Padding, row - m_Padding)

                // check if the round ended
                if (m_PlaneRound.playerGuess_RoundEnds()) {
                    if (m_Tablet) {
                        m_SiblingBoard.setNewRoundStage(false)
                        setNewRoundStage(false)
                    } else {
                        setNewRoundStage(true)
                    }
                    m_PlaneRound.roundEnds()
                    m_GameControls.roundEnds(!m_PlaneRound.playerGuess_IsPlayerWinner(), m_PlaneRound.playerGuess_IsDraw())
                } else {
                    if (!m_Tablet) m_GameControls.updateStats(m_IsComputer)
                }
                updateBoards()
                if (this::m_SiblingBoard.isInitialized) m_SiblingBoard.updateBoards()
            }
        }
    }

    // drag
    private fun touchInMoreSquares(row_first: Int, col_first: Int, row_last: Int, col_last: Int) {
        // System.out.println("Drag from " + Integer.toString(row_first) + ", " + Integer.toString(col_first) + " to " + Integer.toString(row_last) + " , " + Integer.toString(col_last));
        if (!m_IsComputer && m_GameStage === GameStages.BoardEditing) {
            if (abs(row_last - row_first) > abs(col_last - col_first)) {
                // vertical movement
                if (row_last > row_first) movePlaneRight() else movePlaneLeft()
            } else {
                // horizontal movement
                if (col_last > col_first) movePlaneDown() else movePlaneUp()
            }
        }
    }

    fun movePlaneLeft() {
        val valid = m_PlaneRound.movePlaneLeft(m_Selected) == 1
        updateBoards()
        m_GameControls.setDoneEnabled(valid)
    }

    fun movePlaneRight() {
        val valid = m_PlaneRound.movePlaneRight(m_Selected) == 1
        updateBoards()
        m_GameControls.setDoneEnabled(valid)
    }

    fun movePlaneUp() {
        val valid = m_PlaneRound.movePlaneUpwards(m_Selected) == 1
        updateBoards()
        m_GameControls.setDoneEnabled(valid)
    }

    fun movePlaneDown() {
        val valid = m_PlaneRound.movePlaneDownwards(m_Selected) == 1
        updateBoards()
        m_GameControls.setDoneEnabled(valid)
    }

    fun rotatePlane() {
        val valid = m_PlaneRound.rotatePlane(m_Selected) == 1
        updateBoards()
        m_GameControls.setDoneEnabled(valid)
    }

    fun setPlayerBoard() {
        m_IsComputer = false
        updateBoards()
    }

    fun setComputerBoard() {
        m_IsComputer = true
        updateBoards()
    }

    /**
     * Sets the game stage. If the boolean setRole is true, automatically transform to computer board.
     * setRole is true for phone devices.
     * @param setRole
     */
    fun setGameStage(setRole: Boolean) {
        m_GameStage = GameStages.Game
        if (setRole) m_IsComputer = true
        updateBoards()
    }

    fun getGameStage(): GameStages {
        return m_GameStage
    }

    /**
     * Sets the board editing stage. If the boolean setRole is true, automatically transform to computer board.
     * setRole is true for phone devices.
     * @param setRole
     */
    fun setBoardEditingStage(setRole: Boolean) {
        m_GameStage = GameStages.BoardEditing
        if (setRole) m_IsComputer = false
        updateBoards()
    }

    /**
     * Sets the new round stage. If the boolean setRole is true, automatically transform to computer board.
     * setRole is true for phone devices.
     * @param setRole
     */
    fun setNewRoundStage(setRole: Boolean) {
        m_GameStage = GameStages.GameNotStarted
        if (setRole) m_IsComputer = true
        updateBoards()
    }

    fun setGameControls(gameControls: GameControlsAdapterSinglePlayer) {
        m_GameControls = gameControls
    }

    fun setSiblingBoard(siblingBoard: GameBoardSinglePlayer) {
        m_SiblingBoard = siblingBoard
    }

    fun isPlayer(): Boolean {
        return !m_IsComputer
    }
}
