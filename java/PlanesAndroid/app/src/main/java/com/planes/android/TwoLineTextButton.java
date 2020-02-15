package com.planes.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class TwoLineTextButton extends AppCompatButton implements ViewWithText {

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

        int measuredHeight = CanvasPaintUtilities.measureHeightTwoLinesText(heightMeasureSpec, m_Paint, m_Text1, m_Text2, m_LineSpacing);
        int measuredWidth = CanvasPaintUtilities.measureWidthTwoLinesText(widthMeasureSpec, m_Paint, m_Text1, m_Text2);

        setMeasuredDimension(measuredWidth, measuredHeight);
    }


    @Override
    public void onDraw(Canvas canvas) {
        m_Paint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, getWidth(), getHeight(), m_Paint);

        m_Paint.setColor(Color.BLUE);
        CanvasPaintUtilities.drawTextFitToSizeTwoLines(m_Text1, m_Text2, m_TextSize, canvas, m_Paint, getWidth(), getHeight(), m_LineSpacing);
    }

    public int getOptimalTextSize(int maxTextSize, int viewWidth, int viewHeight) {
        return CanvasPaintUtilities.computeOptimalTextSizeTwoLines(m_Text1, m_Text2, m_Paint, viewWidth, viewHeight, maxTextSize, m_LineSpacing);
    }

    public void setTextSize(int textSize) {
        m_TextSize = textSize;
        invalidate();

    }

    protected Paint m_Paint;
    protected String m_Text1;
    protected String m_Text2;
    protected int m_TextSize = 10;
    protected int m_LineSpacing = 10;
}
