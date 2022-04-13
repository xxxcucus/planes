package com.planes.android.game.common

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class GridSquare : View {
    private var m_BackgroundColor = 0
    private val m_GuessColor = Color.RED
    private var m_GuessType = -1 //no guess
    private var m_RowCount = 10
    private var m_ColCount = 10
    private var m_RowNo = -1
    private var m_ColNo = -1
    private lateinit var m_Paint: Paint
    private lateinit var m_MissCircle: RectF
    private lateinit var m_HitPath: Path
    private var m_Width = 0
    private lateinit var m_Parent: GameBoard
    private var m_XTouched = -1
    private var m_YTouched = -1

    constructor(context: Context?, width: Int) : super(context) {
        m_Width = width
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun setWidth(newWidth: Int) {
        m_Width = newWidth
        allocateMemory()
        invalidate()
    }

    fun setBackgroundColor(r: Int, g: Int, b: Int) {
        m_BackgroundColor = 255 and 0xff shl 24 or (r and 0xff shl 16) or (g and 0xff shl 8) or (b and 0xff)
    }

    override fun setBackgroundColor(color: Int) {
        m_BackgroundColor = color
    }

    fun setGuess(guess_type: Int) {
        m_GuessType = guess_type
    }

    fun setRowCount(row_count: Int) {
        m_RowCount = row_count
    }

    fun setColCount(col_count: Int) {
        m_ColCount = col_count
    }

    fun getColNo(): Int {
        return m_ColNo
    }

    fun getRowNo(): Int {
        return m_RowNo
    }

    fun setRow(i: Int) {
        m_RowNo = i
    }

    fun setColumn(j: Int) {
        m_ColNo = j
    }

    override fun onDraw(canvas: Canvas) {
        m_Paint.style = Paint.Style.FILL
        m_Paint.color = m_BackgroundColor
        canvas.drawRect((width / 20).toFloat(), (height / 20).toFloat(), (width * 19 / 20).toFloat(), (height * 19 / 20).toFloat(), m_Paint)
        if (m_GuessType >= 0) {
            println("Draw " + m_GuessType + " " + m_RowNo + " " + m_ColNo)
            when (m_GuessType) {
                0 -> {
                    //draw red circle
                    m_Paint.style = Paint.Style.FILL
                    m_Paint.color = m_GuessColor
                    canvas.drawOval(m_MissCircle, m_Paint)
                }
                1 -> {
                    //draw triangle
                    m_Paint.style = Paint.Style.FILL
                    m_Paint.color = m_GuessColor
                    canvas.drawPath(m_HitPath, m_Paint)
                }
                2 -> {
                    //draw X
                    m_Paint.style = Paint.Style.STROKE
                    m_Paint.strokeWidth = 10f
                    m_Paint.color = m_GuessColor
                    canvas.drawLine(0f, 0f, width.toFloat(), height.toFloat(), m_Paint)
                    canvas.drawLine(0f, height.toFloat(), width.toFloat(), 0f, m_Paint)
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(m_Width, m_Width)
    }

    private fun init() {
        m_Paint = Paint()
        allocateMemory()
    }

    private fun allocateMemory() {
        //System.out.println("Allocate " + m_Width);
        m_MissCircle = RectF((m_Width / 4).toFloat(), (m_Width / 4).toFloat(), (m_Width * 3 / 4).toFloat(), (m_Width * 3 / 4).toFloat())
        m_HitPath = Path()
        m_HitPath.moveTo(0f, (m_Width / 2).toFloat())
        m_HitPath.lineTo((m_Width / 2).toFloat(), 0f)
        m_HitPath.lineTo(m_Width.toFloat(), (m_Width / 2).toFloat())
        m_HitPath.lineTo((m_Width / 2).toFloat(), m_Width.toFloat())
        m_HitPath.lineTo(0f, (m_Width / 2).toFloat())
        m_HitPath.close()
    }

    fun setParent(top: GameBoard) {
        m_Parent = top
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                m_XTouched = event.rawX.toInt()
                m_YTouched = event.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_BUTTON_RELEASE, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                val xtouched = event.rawX.toInt()
                val ytouched = event.rawY.toInt()
                m_Parent.touchEventUp(m_RowNo, m_ColNo, (ytouched - m_YTouched) / m_Width, (xtouched - m_XTouched) / m_Width)
            }
        }
        return true
    }
}