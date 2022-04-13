package com.planes.android.game.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import com.planes.android.customviews.ButtonWithPictogram
import com.planes.android.customviews.CanvasPaintUtilities.Functions.createFillRectPath

class MoveLeftButton : ButtonWithPictogram {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        m_Paint.style = Paint.Style.FILL
        m_Paint.color = Color.BLUE
        val width = width
        val height = height
        val centerX = width / 2
        val centerY = height / 2
        val radius = Math.min(width / 3, height / 3)
        val path1 = Path()
        createFillRectPath(path1, centerX - radius / 2 + radius / 3, centerY - radius / 5, radius, radius * 2 / 5)
        canvas.drawPath(path1, m_Paint)
        val path2 = Path()
        path2.moveTo((centerX - radius / 2 + radius / 3).toFloat(), centerY.toFloat())
        path2.lineTo((centerX - radius / 2 + radius / 3).toFloat(), (centerY + radius / 3).toFloat())
        path2.lineTo((centerX - radius / 2 - radius / 3).toFloat(), centerY.toFloat())
        path2.lineTo((centerX - radius / 2 + radius / 3).toFloat(), (centerY - radius / 3).toFloat())
        path2.close()
        canvas.drawPath(path2, m_Paint)
    }
}