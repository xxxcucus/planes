package com.planes.android;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;


/**
 * Parent class for all buttons with pictograms
 */
public class ButtonWithPictogram extends AppCompatButton {

    public ButtonWithPictogram(Context context) {
        super(context);
        init();
    }

    public ButtonWithPictogram(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonWithPictogram(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        m_Paint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specSizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int specSizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        int measWidth = m_MinWidth;
        int measHeight = m_MinHeight;

        if (m_MinWidth < specSizeWidth)
            measWidth = specSizeWidth;
        if (m_MinHeight < specSizeHeight)
            measHeight = specSizeHeight;

        setMeasuredDimension(measWidth, measHeight);
    }

    final int m_MinWidth = 30;
    final int m_MinHeight = 30;

    protected Paint m_Paint;
}
