package com.planes.android;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

public class CanvasPaintUtilities {

    public static void createFillRectPath(Path path, int left, int top, int width, int height) {
        path.moveTo(left, top);
        path.lineTo(left + width, top);
        path.lineTo(left + width, top + height);
        path.lineTo(left, top + height);
        path.close();
    }

    public static void drawTextFitToSize(String text, Canvas canvas, Paint paint, int width, int height) {
        paint.setTextAlign(Paint.Align.LEFT);
        int curTextSize = 0;

        int centerX = width / 2;
        int centerY = height / 2;

        int textWidth = 0;
        int textHeight = 0;

        int searchStep = 5;
        while (textWidth < width && textHeight < height) {
            curTextSize += searchStep;
            paint.setTextSize(curTextSize);
            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);
            textHeight = bounds.height();
            textWidth = bounds.width();
        }

        paint.setTextSize(curTextSize - searchStep);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        textHeight = bounds.height();
        textWidth = bounds.width();

        canvas.drawText(text, centerX - textWidth / 2, centerY + textHeight / 2, paint);
    }
}
