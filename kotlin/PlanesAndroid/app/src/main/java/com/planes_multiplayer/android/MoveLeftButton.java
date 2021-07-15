package com.planes_multiplayer.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;

public class MoveLeftButton extends ButtonWithPictogram {

    public MoveLeftButton(Context context) {
        super(context);
        init();
    }

    public MoveLeftButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoveLeftButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        m_Paint.setStyle(Paint.Style.FILL);
        m_Paint.setColor(Color.BLUE);

        int width = getWidth();
        int height = getHeight();

        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(width / 3, height / 3);

        Path path1 = new Path();
        CanvasPaintUtilities.Functions.createFillRectPath(path1, centerX - radius / 2 + radius / 3, centerY - radius / 5, radius, radius * 2 / 5);
        canvas.drawPath(path1, m_Paint);

        Path path2 = new Path();
        path2.moveTo(centerX - radius / 2 + radius / 3, centerY);
        path2.lineTo(centerX - radius / 2 + radius / 3, centerY + radius / 3);
        path2.lineTo(centerX - radius / 2 - radius / 3, centerY);
        path2.lineTo(centerX - radius / 2 + radius / 3, centerY - radius / 3);
        path2.close();
        canvas.drawPath(path2, m_Paint);
    }

}
