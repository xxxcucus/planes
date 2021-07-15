package com.planes_multiplayer.android

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.view.View

public class CanvasPaintUtilities {

    //TODO: Do I need JvmStatic here ?
    companion object Functions {
        fun createFillRectPath(path: Path, left: Int, top: Int, width: Int, height: Int) {
            path.moveTo(left.toFloat(), top.toFloat())
            path.lineTo((left + width).toFloat(), top.toFloat())
            path.lineTo((left + width).toFloat(), (top + height).toFloat())
            path.lineTo(left.toFloat(), (top + height).toFloat())
            path.close()
        }


        fun drawTextFitToSizeOneLine(text: String, textSize: Int, canvas: Canvas, paint: Paint, width: Int, height: Int) {
            paint.textAlign = Paint.Align.LEFT
            paint.textSize = textSize.toFloat()
            val centerX = width / 2
            val centerY = height / 2
            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)
            val textHeight = bounds.height()
            val textWidth = bounds.width()
            canvas.drawText(text, (centerX - textWidth / 2).toFloat(), (centerY + textHeight / 2).toFloat(), paint)
        }


        fun computeOptimalTextSizeOneLine(text: String, paint: Paint, width: Int, height: Int, maxSize: Int): Int {
            var curTextSize = 0
            var textWidth = 0
            var textHeight = 0
            val searchStep = 1
            while (textWidth < 9 * width / 10 && textHeight < height && curTextSize <= maxSize) {
                curTextSize += searchStep
                paint.textSize = curTextSize.toFloat()
                val bounds = Rect()
                paint.getTextBounds(text, 0, text.length, bounds)
                textHeight = bounds.height()
                textWidth = bounds.width()
            }
            return curTextSize - searchStep
        }


        fun drawTextFitToSizeTwoLines(text1: String, text2: String, textSize: Int, canvas: Canvas, paint: Paint, width: Int, height: Int, lineSpacing: Int) {
            paint.textAlign = Paint.Align.LEFT
            paint.textSize = textSize.toFloat()
            val centerX = width / 2
            val centerY = height / 2
            val bounds1 = Rect()
            paint.getTextBounds(text1, 0, text1.length, bounds1)
            val bounds2 = Rect()
            paint.getTextBounds(text2, 0, text2.length, bounds2)
            val textHeight = bounds1.height() + bounds2.height() + lineSpacing
            val textWidth = Math.max(bounds1.width(), bounds2.width())
            canvas.drawText(text1, (centerX - bounds1.width() / 2).toFloat(), (centerY - textHeight / 2 + bounds1.height()).toFloat(), paint)
            canvas.drawText(text2, (centerX - bounds2.width() / 2).toFloat(), (centerY + textHeight / 2).toFloat(), paint)
        }


        fun computeOptimalTextSizeTwoLines(text1: String, text2: String, paint: Paint, width: Int, height: Int, maxSize: Int, lineSpacing: Int): Int {
            var curTextSize = 0
            var textWidth1 = 0
            var textHeight1 = 0
            var textWidth2 = 0
            var textHeight2 = 0
            val searchStep = 1
            while (Math.max(textWidth1, textWidth2) < 9 * width / 10 && textHeight1 + textHeight2 + lineSpacing < height && curTextSize <= maxSize) {
                curTextSize += searchStep
                paint.textSize = curTextSize.toFloat()
                val bounds1 = Rect()
                paint.getTextBounds(text1, 0, text1.length, bounds1)
                val bounds2 = Rect()
                paint.getTextBounds(text2, 0, text2.length, bounds2)
                textHeight1 = bounds1.height()
                textWidth1 = bounds1.width()
                textWidth2 = bounds2.width()
                textHeight2 = bounds2.height()
            }
            return curTextSize - searchStep
        }


        fun measureHeightOneLineText(measureSpec: Int, paint: Paint, text: String): Int {
            val specMode = View.MeasureSpec.getMode(measureSpec)
            val specSize = View.MeasureSpec.getSize(measureSpec)
            var resultHeight = 10
            paint.textSize = 20f
            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)
            if (bounds.height() > resultHeight) resultHeight = bounds.height()
            if (specSize > resultHeight) resultHeight = specSize
            return resultHeight
        }


        fun measureWidthOneLineText(measureSpec: Int, paint: Paint, text: String): Int {
            val specMode = View.MeasureSpec.getMode(measureSpec)
            val specSize = View.MeasureSpec.getSize(measureSpec)
            var resultWidth = 10
            paint.textSize = 20f
            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)
            if (bounds.width() > resultWidth) resultWidth = bounds.width()
            if (specSize > resultWidth) resultWidth = specSize
            return resultWidth
        }


        fun measureHeightTwoLinesText(measureSpec: Int, paint: Paint, text1: String, text2: String, lineSpacing: Int): Int {
            val specMode = View.MeasureSpec.getMode(measureSpec)
            val specSize = View.MeasureSpec.getSize(measureSpec)
            var resultHeight = 10
            paint.textSize = 20f
            val bounds1 = Rect()
            paint.getTextBounds(text1, 0, text1.length, bounds1)
            val bounds2 = Rect()
            paint.getTextBounds(text2, 0, text2.length, bounds2)
            if (bounds1.height() + bounds2.height() + lineSpacing > resultHeight) resultHeight = bounds1.height() + bounds2.height() + lineSpacing
            if (specSize > resultHeight) resultHeight = specSize
            return resultHeight
        }


        fun measureWidthTwoLinesText(measureSpec: Int, paint: Paint, text1: String, text2: String): Int {
            val specMode = View.MeasureSpec.getMode(measureSpec)
            val specSize = View.MeasureSpec.getSize(measureSpec)
            var resultWidth = 10
            paint.textSize = 20f
            val bounds1 = Rect()
            paint.getTextBounds(text1, 0, text1.length, bounds1)
            val bounds2 = Rect()
            paint.getTextBounds(text2, 0, text2.length, bounds2)
            if (Math.max(bounds1.width(), bounds2.width()) > resultWidth) resultWidth = Math.max(bounds1.width(), bounds2.width())
            if (specSize > resultWidth) resultWidth = specSize
            return resultWidth
        }


        fun drawButtonShadow(canvas: Canvas, paint: Paint?, width: Int, height: Int) {
            canvas.drawLine(0f, (height - 1).toFloat(), width.toFloat(), (height - 1).toFloat(), paint!!)
            canvas.drawLine((width - 1).toFloat(), 0f, (width - 1).toFloat(), height.toFloat(), paint)
        }
    }
}