package com.planes.android.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.planes.android.customviews.CanvasPaintUtilities.Functions.computeOptimalTextSizeOneLine
import com.planes.android.customviews.CanvasPaintUtilities.Functions.drawButtonShadow
import com.planes.android.customviews.CanvasPaintUtilities.Functions.drawTextFitToSizeOneLine
import com.planes.android.customviews.CanvasPaintUtilities.Functions.measureHeightOneLineText
import com.planes.android.customviews.CanvasPaintUtilities.Functions.measureWidthOneLineText
import com.planes.android.game.common.PlanesVerticalLayoutParams

class TextButton : AppCompatButton, ViewWithText {
    private lateinit var m_Paint: Paint
    private lateinit var m_Text: String
    private var m_TextSize = 10
    private var m_MinWidth = 0
    private var m_MinHeight = 0

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
        if (!this::m_Text.isInitialized) m_Text = lp.getText()
        val measuredHeight = measureHeightOneLineText(heightMeasureSpec, m_Paint, m_Text)
        val measuredWidth = measureWidthOneLineText(widthMeasureSpec, m_Paint, m_Text)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    public override fun onDraw(canvas: Canvas) {
        //super.onDraw(canvas);
        m_Paint.color = Color.GRAY
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), m_Paint)
        m_Paint.color = Color.BLUE
        drawTextFitToSizeOneLine(m_Text, m_TextSize, canvas, m_Paint, width, height)
        m_Paint.color = Color.BLACK
        drawButtonShadow(canvas, m_Paint, width, height)
    }

    override fun getOptimalTextSize(maxTextSize: Int, viewWidth: Int, viewHeight: Int): Int {
        return computeOptimalTextSizeOneLine(m_Text, m_Paint, viewWidth, viewHeight, maxTextSize)
    }

    override fun setTextSize(textSize: Int) {
        m_TextSize = textSize
        invalidate()
    }


}