package com.planes.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatButton;

public class RotateButton extends AppCompatButton {

    public RotateButton(Context context) {
        super(context);
        init();
    }

    public RotateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RotateButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        m_Paint = new Paint();
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
        path1.moveTo(centerX + radius, centerY);
        path1.arcTo(new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius), 0, 270);
        int radius1 = radius - radius / 3;
        path1.lineTo(centerX, centerY - radius / 3);
        path1.arcTo(new RectF(centerX - radius1, centerY - radius1, centerX + radius1, centerY + radius1), 270, -270);
        path1.close();

        canvas.drawPath(path1, m_Paint);

        Path path2 = new Path();
        path2.moveTo(centerX + radius - radius / 6, centerY);
        path2.lineTo(centerX + radius - radius / 6 - radius / 3 , centerY);
        path2.lineTo(centerX + radius - radius / 6 , centerY - radius / 3);
        path2.lineTo(centerX + radius - radius / 6 + radius / 3, centerY);
        path2.close();

        canvas.drawPath(path2, m_Paint);
    }

    private Paint m_Paint;
}
