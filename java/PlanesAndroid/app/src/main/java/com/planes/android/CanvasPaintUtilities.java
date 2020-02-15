package com.planes.android;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.View;

public class CanvasPaintUtilities {

    public static void createFillRectPath(Path path, int left, int top, int width, int height) {
        path.moveTo(left, top);
        path.lineTo(left + width, top);
        path.lineTo(left + width, top + height);
        path.lineTo(left, top + height);
        path.close();
    }

    public static void drawTextFitToSizeOneLine(String text, int textSize, Canvas canvas, Paint paint, int width, int height) {
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(textSize);
        int centerX = width / 2;
        int centerY = height / 2;

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int textHeight = bounds.height();
        int textWidth = bounds.width();

        canvas.drawText(text, centerX - textWidth / 2, centerY + textHeight / 2, paint);
    }

    public static int computeOptimalTextSizeOneLine(String text, Paint paint, int width, int height, int maxSize) {
        int curTextSize = 0;

        int textWidth = 0;
        int textHeight = 0;

        int searchStep = 1;
        while (textWidth < width && textHeight < height && curTextSize < maxSize) {
            curTextSize += searchStep;
            paint.setTextSize(curTextSize);
            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);
            textHeight = bounds.height();
            textWidth = bounds.width();
        }

        return curTextSize - searchStep;
    }

    public static void drawTextFitToSizeTwoLines(String text1, String text2, int textSize, Canvas canvas, Paint paint, int width, int height, int lineSpacing) {
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(textSize);
        int centerX = width / 2;
        int centerY = height / 2;

        Rect bounds1 = new Rect();
        paint.getTextBounds(text1, 0, text1.length(), bounds1);

        Rect bounds2 = new Rect();
        paint.getTextBounds(text2, 0, text2.length(), bounds2);

        int textHeight = bounds1.height() + bounds2.height() + lineSpacing;
        int textWidth = Math.max(bounds1.width(), bounds2.width());

        canvas.drawText(text1, centerX - bounds1.width() / 2, centerY - (textHeight / 2) + bounds1.height(), paint);
        canvas.drawText(text2, centerX - bounds2.width() / 2, centerY + (textHeight / 2), paint);
    }

    public static int computeOptimalTextSizeTwoLines(String text1, String text2, Paint paint, int width, int height, int maxSize, int lineSpacing) {
        int curTextSize = 0;

        int textWidth1 = 0;
        int textHeight1 = 0;
        int textWidth2 = 0;
        int textHeight2 = 0;

        int searchStep = 1;
        while (Math.max(textWidth1, textWidth2) < width && (textHeight1 + textHeight2 + lineSpacing < height) && curTextSize < maxSize) {
            curTextSize += searchStep;
            paint.setTextSize(curTextSize);
            Rect bounds1 = new Rect();
            paint.getTextBounds(text1, 0, text1.length(), bounds1);
            Rect bounds2 = new Rect();
            paint.getTextBounds(text2, 0, text2.length(), bounds2);

            textHeight1 = bounds1.height();
            textWidth1 = bounds1.width();
            textWidth2 = bounds2.width();
            textHeight2 = bounds2.height();
        }

        return curTextSize - searchStep;
    }

    public static int measureHeightOneLineText(int measureSpec, Paint paint, String text) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        int resultHeight = 10;

        paint.setTextSize(20);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        if (bounds.height() > resultHeight)
            resultHeight = bounds.height();

        if (specSize > resultHeight)
            resultHeight = specSize;

        return resultHeight;
    }

    public static int measureWidthOneLineText(int measureSpec, Paint paint, String text) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        int resultWidth = 10;

        paint.setTextSize(20);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        if (bounds.width() > resultWidth)
            resultWidth = bounds.width();

        if (specSize > resultWidth)
            resultWidth = specSize;

        return resultWidth;
    }

    public static int measureHeightTwoLinesText(int measureSpec, Paint paint, String text1, String text2, int lineSpacing) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        int resultHeight = 10;

        paint.setTextSize(20);
        Rect bounds1 = new Rect();
        paint.getTextBounds(text1, 0, text1.length(), bounds1);
        Rect bounds2 = new Rect();
        paint.getTextBounds(text2, 0, text2.length(), bounds2);

        if (bounds1.height() + bounds2.height() + lineSpacing > resultHeight)
            resultHeight = bounds1.height() + bounds2.height() + lineSpacing;

        if (specSize > resultHeight)
            resultHeight = specSize;

        return resultHeight;
    }

    public static int measureWidthTwoLinesText(int measureSpec, Paint paint, String text1, String text2) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        int resultWidth = 10;

        paint.setTextSize(20);
        Rect bounds1 = new Rect();
        paint.getTextBounds(text1, 0, text1.length(), bounds1);
        Rect bounds2 = new Rect();
        paint.getTextBounds(text2, 0, text2.length(), bounds2);

        if (Math.max(bounds1.width(), bounds2.width()) > resultWidth)
            resultWidth = Math.max(bounds1.width(), bounds2.width());

        if (specSize > resultWidth)
            resultWidth = specSize;

        return resultWidth;
    }


}
