package com.planes.android.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.planes.android.customviews.CanvasPaintUtilities.Functions.drawButtonShadow
import androidx.appcompat.widget.AppCompatButton
import com.planes.android.game.common.PlanesVerticalLayoutParams

/**
 * Parent class for all buttons with pictograms
 */
open class ButtonWithPictogram : AppCompatButton {
    private val m_MinWidth = 30
    private val m_MinHeight = 30
    protected lateinit var m_Paint: Paint

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

    public override fun onDraw(canvas: Canvas) {
        val lp = layoutParams as PlanesVerticalLayoutParams
        m_Paint.color = lp.getBackgroundColor().defaultColor
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), m_Paint)
        m_Paint.color = Color.BLACK
        drawButtonShadow(canvas, m_Paint, width, height)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specSizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val specSizeHeight = MeasureSpec.getSize(heightMeasureSpec)
        var measWidth = m_MinWidth
        var measHeight = m_MinHeight
        if (m_MinWidth < specSizeWidth) measWidth = specSizeWidth
        if (m_MinHeight < specSizeHeight) measHeight = specSizeHeight
        setMeasuredDimension(measWidth, measHeight)
    }
}