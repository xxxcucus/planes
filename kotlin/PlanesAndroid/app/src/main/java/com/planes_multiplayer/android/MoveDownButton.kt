package com.planes_multiplayer.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import com.planes_multiplayer.android.CanvasPaintUtilities.Functions.createFillRectPath

class MoveDownButton : ButtonWithPictogram {
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
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
        createFillRectPath(path1, centerX - radius / 5, centerY - radius / 2 - radius / 3, radius * 2 / 5, radius)
        canvas.drawPath(path1, m_Paint)
        val path2 = Path()
        path2.moveTo(centerX.toFloat(), (centerY + radius / 2 - radius / 3).toFloat())
        path2.lineTo((centerX - radius / 3).toFloat(), (centerY + radius / 2 - radius / 3).toFloat())
        path2.lineTo(centerX.toFloat(), (centerY + radius / 2 + radius / 3).toFloat())
        path2.lineTo((centerX + radius / 3).toFloat(), (centerY + radius / 2 - radius / 3).toFloat())
        path2.close()
        canvas.drawPath(path2, m_Paint)
    }
}