package com.planes.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class TextButton extends AppCompatButton {

    public TextButton(Context context) {
        super(context);
        init();
    }

    public TextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        m_Paint = new Paint();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        PlanesVerticalLayout.PlanesVerticalLayoutParams lp = (PlanesVerticalLayout.PlanesVerticalLayoutParams) getLayoutParams();
        m_Text = lp.getText();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        m_Paint.setColor(Color.BLUE);
        m_Paint.setTextSize(10);
        m_Paint.setTextAlign(Paint.Align.LEFT);
        int curTextSize = 10;

        int width = getWidth();
        int height = getHeight();

        int centerX = width / 2;
        int centerY = height / 2;

        int textWidth = 10;
        int textHeight = 10;

        while (textWidth < width / 2 && textHeight < height / 2) {
            m_Paint.setTextSize(curTextSize + 10);
            Rect bounds = new Rect();
            m_Paint.getTextBounds(m_Text, 0, m_Text.length(), bounds);
            textHeight = bounds.height();
            textWidth = bounds.width();
            curTextSize += 10;
        }

        m_Paint.setTextSize(curTextSize);

        canvas.drawText(m_Text, centerX - textWidth / 2, centerY + textHeight / 2, m_Paint);
    }


    private Paint m_Paint;
    private String m_Text;

}
