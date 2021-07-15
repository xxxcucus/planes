package com.planes_multiplayer.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;

public class TextButton extends AppCompatButton implements ViewWithText {

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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        PlanesVerticalLayout.PlanesVerticalLayoutParams lp = (PlanesVerticalLayout.PlanesVerticalLayoutParams) getLayoutParams();
        if (m_Text == null)
            m_Text = lp.getText();

        int measuredHeight = CanvasPaintUtilities.measureHeightOneLineText(heightMeasureSpec, m_Paint, m_Text);
        int measuredWidth = CanvasPaintUtilities.measureWidthOneLineText(widthMeasureSpec, m_Paint, m_Text);

        setMeasuredDimension(measuredWidth, measuredHeight);
    }


    @Override
    public void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        m_Paint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, getWidth(), getHeight(), m_Paint);

        m_Paint.setColor(Color.BLUE);
        CanvasPaintUtilities.drawTextFitToSizeOneLine(m_Text, m_TextSize, canvas, m_Paint, getWidth(), getHeight());

        m_Paint.setColor(Color.BLACK);
        CanvasPaintUtilities.drawButtonShadow(canvas, m_Paint, getWidth(), getHeight());
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
    private int m_MinWidth = 0;
    private int m_MinHeight = 0;
}
