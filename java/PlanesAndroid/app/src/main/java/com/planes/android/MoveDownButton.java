package com.planes.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class MoveDownButton extends AppCompatButton {

    public MoveDownButton(Context context) {
        super(context);
        init();
    }

    public MoveDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoveDownButton(Context context, AttributeSet attrs, int defStyle) {
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
        CanvasPaintUtilities.createFillRectPath(path1, centerX - radius / 5, centerY - radius / 2 - radius / 3, radius * 2 / 5, radius);
        canvas.drawPath(path1, m_Paint);

        Path path2 = new Path();

        path2.moveTo(centerX, centerY + radius / 2 - radius / 3);
        path2.lineTo(centerX - radius / 3, centerY + radius / 2 - radius / 3);
        path2.lineTo(centerX, centerY + radius / 2 + radius / 3);
        path2.lineTo(centerX + radius / 3, centerY + radius / 2 - radius / 3);
        path2.close();
        canvas.drawPath(path2, m_Paint);
    }


    private Paint m_Paint;
}
