package com.planes_multiplayer.android

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import java.util.*

//vertical layout works with 2 GameBoard objects and 2 GameControl objects
//the game control object should change depending on the game stage
//the size of the game controls depends on the size of the visible game board
//dimension of the screen and toolbars should be saved inside the layout
class PlanesVerticalLayout : ViewGroup {
    internal inner class PlanesVerticalLayoutParams : LayoutParams {
        constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.PlanesVerticalLayout)
            m_Row = a.getInt(R.styleable.PlanesVerticalLayout_gc_row, 0)
            m_Col = a.getInt(R.styleable.PlanesVerticalLayout_gc_col, 0)
            m_RowSpan = a.getInt(R.styleable.PlanesVerticalLayout_gc_rowspan, 0)
            m_ColSpan = a.getInt(R.styleable.PlanesVerticalLayout_gc_colspan, 0)
            m_GameStage = a.getInt(R.styleable.PlanesVerticalLayout_gc_game_stage, 0)
            m_Text = a.getString(R.styleable.PlanesVerticalLayout_gc_text).toString()
            m_Text1 = a.getString(R.styleable.PlanesVerticalLayout_gc_text1).toString()
            m_Text2 = a.getString(R.styleable.PlanesVerticalLayout_gc_text2).toString()
            m_BackgroundColor = a.getColor(R.styleable.PlanesVerticalLayout_gc_background_color, resources.getColor(R.color.grey))
            a.recycle()
        }

        //TODO: for these 2 constructors there is no initialization of member variables !!!
        constructor(width: Int, height: Int) : super(width, height) {}
        constructor(source: LayoutParams?) : super(source) {}

        fun getText(): String {
            return m_Text
        }

        fun getText1(): String {
            return m_Text1
        }

        fun getText2(): String {
            return m_Text2
        }

        fun getColor(): Int {
            return m_BackgroundColor
        }

        //TODO: to add text formatting options
        //word wrap, no word wrap
        var m_Row = 0
        var m_Col = 0
        var m_RowSpan = 0
        var m_ColSpan = 0
        var m_GameStage = -1
        private val m_Player = false
        lateinit var m_Text: String
        lateinit var m_Text1: String
        lateinit var m_Text2: String
        var m_BackgroundColor = 0
    }

    constructor(context: Context) : super(context) {
        m_Context = context
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        m_Context = context
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        m_Context = context
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val count = childCount
        m_GameBoards = ArrayList()
        m_GameControls = HashMap()
        m_GameControlsMaxRow = HashMap()
        m_GameControlsMaxCol = HashMap()

        //TODO: to move in a init function
        //TODO: to read only the children corresponding to the current stage
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child is GameBoard) {
                m_GameBoards.add(child)
            } else {
                val lp = child.layoutParams as PlanesVerticalLayoutParams
                if (!(lp.m_Row == 0 || lp.m_Col == 0 || lp.m_GameStage == -1)) {
                    if (!m_GameControls.containsKey(lp.m_GameStage)) {
                        m_GameControls[lp.m_GameStage] = ArrayList()
                        m_GameControlsMaxRow[lp.m_GameStage] = 0
                        m_GameControlsMaxCol[lp.m_GameStage] = 0
                    }
                    val viewsForGameStage = m_GameControls[lp.m_GameStage]!!
                    if (lp.m_GameStage != m_GameStage.value) {
                        child.visibility = GONE
                    } else {
                        child.visibility = VISIBLE
                    }
                    viewsForGameStage.add(child)
                    val maxRow = m_GameControlsMaxRow[lp.m_GameStage]!!
                    val rowspan = if (lp.m_RowSpan != 0) lp.m_RowSpan - 1 else 0
                    val colspan = if (lp.m_ColSpan != 0) lp.m_ColSpan - 1 else 0
                    if (lp.m_Row + rowspan > maxRow) m_GameControlsMaxRow[lp.m_GameStage] = lp.m_Row + rowspan
                    val maxCol = m_GameControlsMaxCol[lp.m_GameStage]!!
                    if (lp.m_Col + colspan > maxCol) m_GameControlsMaxCol[lp.m_GameStage] = lp.m_Col + colspan
                }
            }
        }
        if (m_GameBoards.size == 0 || m_GameBoards.size > 2) {
            return
        }
        if (m_GameBoards.size == 2) {
            m_Tablet = true
        }
        if (right - left < bottom - top) { //vertical layout
            m_Vertical = true
        }
        val layoutWidth = right - left
        val layoutHeight = bottom - top
        if (m_GameStage === GameStages.BoardEditing) {
            if (m_Tablet) {
                if (m_Vertical) {
                    setPlayerBoardPosition(0, 0, layoutWidth, layoutHeight / 2, true)
                    setGameControlsPositions(0, layoutHeight / 2, layoutWidth, layoutHeight)
                } else {
                    setPlayerBoardPosition(0, 0, layoutWidth / 2, layoutHeight, true)
                    setGameControlsPositions(layoutWidth / 2, 0, layoutWidth, layoutHeight)
                }
            } else {
                if (m_Vertical) {
                    setFirstBoardPosition(0, 0, layoutWidth, layoutWidth)
                    setGameControlsPositions(0, layoutWidth, layoutWidth, layoutHeight)
                } else {
                    setFirstBoardPosition(0, 0, layoutHeight, layoutHeight)
                    setGameControlsPositions(layoutHeight, 0, layoutWidth, layoutHeight)
                }
            }
        }
        val boardSpacing = 20
        if (m_GameStage === GameStages.Game) {
            if (m_Tablet) {
                if (m_Vertical) {
                    setPlayerBoardPosition(0, 0, layoutWidth, layoutHeight / 2 - boardSpacing, false)
                    setComputerBoardPosition(0, layoutHeight / 2 + boardSpacing, layoutWidth, layoutHeight, false)
                } else {
                    setPlayerBoardPosition(0, 0, layoutWidth / 2 - boardSpacing, layoutHeight, false)
                    setComputerBoardPosition(layoutWidth / 2 + boardSpacing, 0, layoutWidth, layoutHeight, false)
                }
            } else {
                if (m_Vertical) {
                    setFirstBoardPosition(0, 0, layoutWidth, layoutWidth)
                    setGameControlsPositions(0, layoutWidth, layoutWidth, layoutHeight)
                } else {
                    setFirstBoardPosition(0, 0, layoutHeight, layoutHeight)
                    setGameControlsPositions(layoutHeight, 0, layoutWidth, layoutHeight)
                }
            }
        }
        if (m_GameStage === GameStages.GameNotStarted) {
            if (m_Tablet) {
                if (m_Vertical) {
                    if (!m_ShowPlayerBoard) {
                        setGameControlsPositions(0, 0, layoutWidth, layoutHeight / 2)
                        setComputerBoardPosition(0, layoutHeight / 2, layoutWidth, layoutHeight, true)
                    } else {
                        setPlayerBoardPosition(0, 0, layoutWidth, layoutHeight / 2, true)
                        setGameControlsPositions(0, layoutHeight / 2, layoutWidth, layoutHeight)
                    }
                } else {
                    if (m_ShowPlayerBoard) {
                        setPlayerBoardPosition(0, 0, layoutWidth / 2, layoutHeight, true)
                        setGameControlsPositions(layoutWidth / 2, 0, layoutWidth, layoutHeight)
                    } else {
                        setGameControlsPositions(0, 0, layoutWidth / 2, layoutHeight)
                        setComputerBoardPosition(layoutWidth / 2, 0, layoutWidth, layoutHeight, true)
                    }
                }
            } else {
                if (m_Vertical) {
                    setFirstBoardPosition(0, 0, layoutWidth, layoutWidth)
                    setGameControlsPositions(0, layoutWidth, layoutWidth, layoutHeight)
                } else {
                    setFirstBoardPosition(0, 0, layoutHeight, layoutHeight)
                    setGameControlsPositions(layoutHeight, 0, layoutWidth, layoutHeight)
                }
            }
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return PlanesVerticalLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return LayoutParams(p)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is LayoutParams
    }

    private fun setPlayerBoardPosition(left: Int, top: Int, right: Int, bottom: Int, hideOthers: Boolean) {
        for (board in m_GameBoards) {
            if (board.isPlayer()) {
                board.layout(left, top, right, bottom)
                board.visibility = VISIBLE
            } else {
                if (hideOthers) board.visibility = GONE else board.visibility = VISIBLE
            }
        }
    }

    private fun setComputerBoardPosition(left: Int, top: Int, right: Int, bottom: Int, hideOthers: Boolean) {
        for (board in m_GameBoards) {
            if (!board.isPlayer()) {
                board.layout(left, top, right, bottom)
                board.visibility = VISIBLE
            } else {
                if (hideOthers) board.visibility = GONE else board.visibility = VISIBLE
            }
        }
    }

    private fun setFirstBoardPosition(left: Int, top: Int, right: Int, bottom: Int) {
        m_GameBoards[0]!!.layout(left, top, right, bottom)
    }

    private fun setGameControlsPositions(left: Int, top: Int, right: Int, bottom: Int) {
        val maxRow = m_GameControlsMaxRow[m_GameStage.value]!!
        val maxCol = m_GameControlsMaxCol[m_GameStage.value]!!
        val stepX = (right - left) / (maxCol + 2)
        val stepY = (bottom - top) / (maxRow + 2)
        var currentOptimalTextSize = 100

        //compute text size
        for (view in m_GameControls[m_GameStage.value]!!) {
            if (view !is ViewWithText) continue
            val lp = view.layoutParams as PlanesVerticalLayoutParams
            val rowspan = if (lp.m_RowSpan != 0) lp.m_RowSpan else 1
            val colspan = if (lp.m_ColSpan != 0) lp.m_ColSpan else 1
            val heightMeasureSpec = MeasureSpec.makeMeasureSpec(rowspan * stepY, MeasureSpec.UNSPECIFIED)
            val widthMeasureSpec = MeasureSpec.makeMeasureSpec(colspan * stepX, MeasureSpec.UNSPECIFIED)
            view.measure(widthMeasureSpec, heightMeasureSpec)
            val actualWidth = view.measuredWidth
            val actualHeight = view.measuredHeight
            currentOptimalTextSize = (view as ViewWithText).getOptimalTextSize(currentOptimalTextSize, actualWidth - m_GridSpacing, actualHeight - m_GridSpacing)
        }

        //layout
        for (view in m_GameControls[m_GameStage.value]!!) {
            val lp = view.layoutParams as PlanesVerticalLayoutParams
            val rowspan = if (lp.m_RowSpan != 0) lp.m_RowSpan else 1
            val colspan = if (lp.m_ColSpan != 0) lp.m_ColSpan else 1
            val heightMeasureSpec = MeasureSpec.makeMeasureSpec(rowspan * stepY, MeasureSpec.UNSPECIFIED)
            val widthMeasureSpec = MeasureSpec.makeMeasureSpec(colspan * stepX, MeasureSpec.UNSPECIFIED)
            view.measure(widthMeasureSpec, heightMeasureSpec)
            val actualWidth = view.measuredWidth - m_GridSpacing
            val actualHeight = view.measuredHeight - m_GridSpacing
            val viewCenterX = left + lp.m_Col * stepX + colspan * stepX / 2
            val viewCenterY = top + lp.m_Row * stepY + rowspan * stepY / 2
            if (view is ViewWithText) (view as ViewWithText).setTextSize(currentOptimalTextSize)
            view.layout(viewCenterX - actualWidth / 2, viewCenterY - actualHeight / 2, viewCenterX + actualWidth / 2, viewCenterY + actualHeight / 2)

            //view.layout(left + lp.m_Col * stepX, top + lp.m_Row * stepY, left + (lp.m_Col + 1) * stepX, top + (lp.m_Row + 1) * stepY);
        }
    }

    fun setNewRoundStage() {
        m_GameStage = GameStages.GameNotStarted
        invalidate()
        requestLayout()
    }

    fun setGameStage() {
        m_GameStage = GameStages.Game
        invalidate()
        requestLayout()
    }

    fun setBoardEditingStage() {
        m_GameStage = GameStages.BoardEditing
        invalidate()
        requestLayout()
    }

    fun setPlayerBoard() {
        m_ShowPlayerBoard = true
        invalidate()
        requestLayout()
    }

    fun setComputerBoard() {
        m_ShowPlayerBoard = false
        invalidate()
        requestLayout()
    }

    fun setToolbarHeight(toolbarHeight: Int) {
        m_ToolbarHeight = toolbarHeight
    }

    private lateinit var m_GameBoards: ArrayList<GameBoard>
    private lateinit var m_GameControls: HashMap<Int, ArrayList<View>>
    private lateinit var m_GameControlsMaxRow: HashMap<Int, Int>
    private lateinit var m_GameControlsMaxCol: HashMap<Int, Int>
    private var m_ToolbarHeight: Int = 0
    private var m_Context: Context
    private var m_GameStage = GameStages.BoardEditing
    private val m_CorrectChildren = false
    private var m_ShowPlayerBoard = false //in start new game stage which board to show
    private var m_Tablet = false
    private var m_Vertical = false
    private val m_GridSpacing = 5
}