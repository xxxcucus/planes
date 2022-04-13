package com.planes.android.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.planes.android.customviews.CanvasPaintUtilities.Functions.computeOptimalTextSizeTwoLines
import com.planes.android.customviews.CanvasPaintUtilities.Functions.drawButtonShadow
import com.planes.android.customviews.CanvasPaintUtilities.Functions.drawTextFitToSizeTwoLines
import com.planes.android.customviews.CanvasPaintUtilities.Functions.measureHeightTwoLinesText
import com.planes.android.customviews.CanvasPaintUtilities.Functions.measureWidthTwoLinesText
import com.planes.android.game.singleplayer.PlanesVerticalLayoutSinglePlayer.PlanesVerticalLayoutParams

open class TwoLineTextButton : AppCompatButton, ViewWithText {

    protected lateinit var m_Paint: Paint
    protected lateinit var m_Text1: String
    protected lateinit var m_Text2: String
    protected var m_TextSize = 10
    protected var m_LineSpacing = 10

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    fun init() {
        m_Paint = Paint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val lp = layoutParams as PlanesVerticalLayoutParams
        if (!this::m_Text1.isInitialized) m_Text1 = lp.getText1()
        if (!this::m_Text2.isInitialized) m_Text2 = lp.getText2()
        val measuredHeight = measureHeightTwoLinesText(heightMeasureSpec, m_Paint, m_Text1, m_Text2, m_LineSpacing)
        val measuredWidth = measureWidthTwoLinesText(widthMeasureSpec, m_Paint, m_Text1, m_Text2)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    public override fun onDraw(canvas: Canvas) {
        m_Paint.color = Color.GRAY
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), m_Paint)
        m_Paint.color = Color.BLUE
        drawTextFitToSizeTwoLines(m_Text1, m_Text2, m_TextSize, canvas, m_Paint, width, height, m_LineSpacing)
        m_Paint.color = Color.BLACK
        drawButtonShadow(canvas, m_Paint, width, height)
    }

    override fun getOptimalTextSize(maxTextSize: Int, viewWidth: Int, viewHeight: Int): Int {
        return computeOptimalTextSizeTwoLines(m_Text1, m_Text2, m_Paint, viewWidth, viewHeight, maxTextSize, m_LineSpacing)
    }

    override fun setTextSize(textSize: Int) {
        m_TextSize = textSize
        invalidate()
    }
}