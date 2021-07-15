package com.planes_multiplayer.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class ColouredSurfaceWithText extends View implements ViewWithText {

    public ColouredSurfaceWithText(Context context) {
        super(context);
        init();
    }

    public ColouredSurfaceWithText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColouredSurfaceWithText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        m_Paint = new Paint();
    }

    public void setText(String text) {
        m_Text = text;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        PlanesVerticalLayout.PlanesVerticalLayoutParams lp = (PlanesVerticalLayout.PlanesVerticalLayoutParams) getLayoutParams();
        if (m_Text == null)
            m_Text = lp.getText();
        m_BackgroundColor = lp.getColor();

        int measuredHeight = CanvasPaintUtilities.measureHeightOneLineText(heightMeasureSpec, m_Paint, m_Text);
        int measuredWidth = CanvasPaintUtilities.measureWidthOneLineText(widthMeasureSpec, m_Paint, m_Text);

        setMeasuredDimension(measuredWidth, measuredHeight);
    }


    public void onDraw(Canvas canvas) {
        //TODO: draw the surface in the colour specified
        m_Paint.setColor(m_BackgroundColor);
        canvas.drawRect(0, 0, getWidth(), getHeight(), m_Paint);

        m_Paint.setColor(Color.BLUE);
        CanvasPaintUtilities.drawTextFitToSizeOneLine(m_Text, m_TextSize, canvas, m_Paint, getWidth(), getHeight());
    }

    public int getOptimalTextSize(int maxTextSize, int viewWidth, int viewHeight) {
        return CanvasPaintUtilities.computeOptimalTextSizeOneLine(m_Text, m_Paint, viewWidth, viewHeight, maxTextSize);
    }

    public void setTextSize(int textSize) {
        m_TextSize = textSize;
        invalidate();
    }


    private Paint m_Paint;
    private String m_Text;
    private int m_TextSize = 10;
    private int m_BackgroundColor;
}
