package com.planes.android.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.planes.android.game.common.PlanesVerticalLayoutParams


class ColouredSurfaceWithTwoLineText : View, ViewWithText {
    private lateinit var m_Paint: Paint
    private lateinit var m_Text1: String
    private lateinit var m_Text2: String
    private var m_TextSize = 10
    private var m_BackgroundColor = 0
    private var m_ForegroundColor = 0
    private var m_LineSpacing = 10

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

    fun setText(text1: String, text2: String) {
        m_Text1 = text1
        m_Text2 = text2
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val lp = layoutParams as PlanesVerticalLayoutParams
        if (!this::m_Text1.isInitialized) m_Text1 = lp.getText1()
        if (!this::m_Text2.isInitialized) m_Text2 = lp.getText2()
        m_BackgroundColor = lp.getBackgroundColor().defaultColor
        m_ForegroundColor = lp.getForegroundColor().defaultColor
        val measuredHeight = CanvasPaintUtilities.measureHeightTwoLinesText(
            heightMeasureSpec, m_Paint, m_Text1, m_Text2, m_LineSpacing)
        val measuredWidth = CanvasPaintUtilities.measureWidthTwoLinesText(
            widthMeasureSpec, m_Paint, m_Text1, m_Text2)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    public override fun onDraw(canvas: Canvas) {
        //TODO: draw the surface in the colour specified
        m_Paint.color = m_BackgroundColor
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), m_Paint)
        m_Paint.color = m_ForegroundColor
        CanvasPaintUtilities.drawTextFitToSizeTwoLines(
            m_Text1, m_Text2, m_TextSize, canvas, m_Paint, width, height, m_LineSpacing)
    }

    override fun getOptimalTextSize(maxTextSize: Int, viewWidth: Int, viewHeight: Int): Int {
        return CanvasPaintUtilities.computeOptimalTextSizeTwoLines(
            m_Text1, m_Text2, m_Paint, viewWidth, viewHeight, maxTextSize, m_LineSpacing)
    }

    override fun setTextSize(textSize: Int) {
        m_TextSize = textSize
        invalidate()
    }
}