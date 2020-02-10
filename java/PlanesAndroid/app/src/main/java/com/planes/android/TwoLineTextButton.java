package com.planes.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class TwoLineTextButton extends AppCompatButton {

    public TwoLineTextButton(Context context) {
        super(context);
        init();
    }

    public TwoLineTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TwoLineTextButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        m_Paint = new Paint();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        PlanesVerticalLayout.PlanesVerticalLayoutParams lp = (PlanesVerticalLayout.PlanesVerticalLayoutParams) getLayoutParams();
        m_Text1 = lp.getText1();
        m_Text2 = lp.getText2();

        int measuredHeight = measureHeight(heightMeasureSpec);
        int measuredWidth = measureWidth(widthMeasureSpec);

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int resultHeight = 10;

        m_Paint.setTextSize(20);
        Rect bounds1 = new Rect();
        m_Paint.getTextBounds(m_Text1, 0, m_Text1.length(), bounds1);
        Rect bounds2 = new Rect();
        m_Paint.getTextBounds(m_Text2, 0, m_Text2.length(), bounds2);
        if (bounds1.height() + bounds2.height() + m_LineSpacing > resultHeight)
            resultHeight = bounds1.height() + bounds2.height() + m_LineSpacing;

        if (specSize > resultHeight)
            resultHeight = specSize;

        return resultHeight;
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int resultWidth = 10;

        m_Paint.setTextSize(20);
        Rect bounds1 = new Rect();
        m_Paint.getTextBounds(m_Text1, 0, m_Text1.length(), bounds1);
        Rect bounds2 = new Rect();
        m_Paint.getTextBounds(m_Text2, 0, m_Text2.length(), bounds2);

        if (Math.max(bounds1.width(), bounds2.width()) > resultWidth)
            resultWidth = Math.max(bounds1.width(), bounds2.width());

        if (specSize > resultWidth)
            resultWidth = specSize;

        return resultWidth;
    }

    @Override
    public void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        m_Paint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, getWidth(), getHeight(), m_Paint);

        m_Paint.setColor(Color.BLUE);

        m_Paint.setTextSize(10);
        m_Paint.setTextAlign(Paint.Align.LEFT);
        int curTextSize = 0;

        int width = getWidth();
        int height = getHeight();

        int centerX = width / 2;
        int centerY = height / 2;

        int textWidth = 0;
        int textHeight = 0;

        int searchStep = 5;
        while (textWidth < width && textHeight < height) {
            curTextSize += searchStep;
            m_Paint.setTextSize(curTextSize);
            Rect bounds1 = new Rect();
            m_Paint.getTextBounds(m_Text1, 0, m_Text1.length(), bounds1);
            Rect bounds2 = new Rect();
            m_Paint.getTextBounds(m_Text2, 0, m_Text2.length(), bounds2);
            textHeight = bounds1.height() + bounds2.height() + m_LineSpacing;
            textWidth = Math.max(bounds1.width(), bounds2.width());
        }

        m_Paint.setTextSize(curTextSize - searchStep);
        Rect bounds1 = new Rect();
        m_Paint.getTextBounds(m_Text1, 0, m_Text1.length(), bounds1);
        Rect bounds2 = new Rect();
        m_Paint.getTextBounds(m_Text2, 0, m_Text2.length(), bounds2);
        textHeight = bounds1.height() + bounds2.height() + m_LineSpacing;
        textWidth = Math.max(bounds1.width(), bounds2.width());

        canvas.drawText(m_Text1, centerX - bounds1.width() / 2, centerY - (textHeight / 2) + bounds1.height(), m_Paint);
        canvas.drawText(m_Text2, centerX - bounds2.width() / 2, centerY + (textHeight / 2), m_Paint);
    }

    protected Paint m_Paint;
    protected String m_Text1;
    protected String m_Text2;
    protected int m_LineSpacing = 10;
}
