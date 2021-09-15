package com.planes_multiplayer.android

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

class RotateButton : ButtonWithPictogram {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
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
        path1.moveTo((centerX + radius).toFloat(), centerY.toFloat())
        path1.arcTo(RectF((centerX - radius).toFloat(), (centerY - radius).toFloat(), (centerX + radius).toFloat(), (centerY + radius).toFloat()), 0f, 270f)
        val radius1 = radius - radius / 3
        path1.lineTo(centerX.toFloat(), (centerY - radius / 3).toFloat())
        path1.arcTo(RectF((centerX - radius1).toFloat(), (centerY - radius1).toFloat(), (centerX + radius1).toFloat(), (centerY + radius1).toFloat()), 270f, -270f)
        path1.close()
        canvas.drawPath(path1, m_Paint)
        val path2 = Path()
        path2.moveTo((centerX + radius - radius / 6).toFloat(), centerY.toFloat())
        path2.lineTo((centerX + radius - radius / 6 - radius / 3).toFloat(), centerY.toFloat())
        path2.lineTo((centerX + radius - radius / 6).toFloat(), (centerY - radius / 3).toFloat())
        path2.lineTo((centerX + radius - radius / 6 + radius / 3).toFloat(), centerY.toFloat())
        path2.close()
        canvas.drawPath(path2, m_Paint)
    }
}