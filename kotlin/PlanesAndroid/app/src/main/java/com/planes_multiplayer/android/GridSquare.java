package com.planes_multiplayer.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static java.lang.Math.min;

public class GridSquare extends View {
    public GridSquare(Context context, int width) {
        super(context);
        m_Width = width;
        init();
    }

    public GridSquare(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GridSquare(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setWidth(int newWidth) {
        m_Width = newWidth;
        allocateMemory();
        invalidate();
    }

    public void setBackgroundColor(int r, int g, int b) {
        m_BackgroundColor = (255 & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
    }

    public void setBackgroundColor(int color) {
        m_BackgroundColor = color;
    }
    public void setGuess(int guess_type) {
        m_GuessType = guess_type;
    }
    public void setRowCount(int row_count) {
        m_RowCount = row_count;
    }
    public void setColCount(int col_count) {
        m_ColCount = col_count;
    }
    public void setRow(int i) { m_Row = i; }
    public void setColumn(int j) { m_Col = j; }
    public int getRowNo() { return m_Row; }
    public int getColNo() { return m_Col; }

    @Override
    protected void onDraw(Canvas canvas) {
        m_Paint.setStyle(Paint.Style.FILL);
        m_Paint.setColor(m_BackgroundColor);
        canvas.drawRect(getWidth() / 20, getHeight() / 20 , getWidth() * 19 / 20, getHeight() * 19 / 20, m_Paint);

        if (m_GuessType >= 0) {
            System.out.println("Draw " + m_GuessType + " " + m_Row + " " + m_Col);
            switch (m_GuessType) {
                case 0:
                    //draw red circle
                    m_Paint.setStyle(Paint.Style.FILL);
                    m_Paint.setColor(m_GuessColor);
                    canvas.drawOval(m_MissCircle, m_Paint);
                    break;
                case 1:
                    //draw triangle
                    m_Paint.setStyle(Paint.Style.FILL);
                    m_Paint.setColor(m_GuessColor);
                    canvas.drawPath(m_HitPath, m_Paint);
                    break;
                case 2:
                    //draw X
                    m_Paint.setStyle(Paint.Style.STROKE);
                    m_Paint.setStrokeWidth(10);
                    m_Paint.setColor(m_GuessColor);
                    canvas.drawLine(0, 0, getWidth(), getHeight(), m_Paint);
                    canvas.drawLine(0, getHeight(), getWidth(), 0, m_Paint);
                    break;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(m_Width, m_Width);

    }

    private void init() {
        m_Paint = new Paint();
        allocateMemory();
    }

    private void allocateMemory() {
        //System.out.println("Allocate " + m_Width);
        m_MissCircle = new RectF(m_Width / 4, m_Width / 4, m_Width * 3 / 4, m_Width * 3/ 4);
        m_HitPath = new Path();
        m_HitPath.moveTo(0, m_Width / 2);
        m_HitPath.lineTo(m_Width / 2, 0);
        m_HitPath.lineTo(m_Width, m_Width / 2);
        m_HitPath.lineTo(m_Width / 2, m_Width);
        m_HitPath.lineTo(0, m_Width / 2);
        m_HitPath.close();
    }

    public void setParent(GameBoard top) {
        m_Parent = top;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                m_XTouched = (int)event.getRawX();
                m_YTouched = (int)event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                // touch move code
                break;

            case MotionEvent.ACTION_BUTTON_RELEASE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                int xtouched = (int)event.getRawX();
                int ytouched = (int)event.getRawY();
                m_Parent.touchEventUp(m_Row, m_Col,(ytouched - m_YTouched) / m_Width, (xtouched - m_XTouched) / m_Width);
                break;
        }

        return true;
    }


    private int m_BackgroundColor;
    private int m_GuessColor = Color.RED;
    private int m_GuessType = -1; //no guess
    private int m_RowCount = 10;
    private int m_ColCount = 10;
    private int m_Row = -1;
    private int m_Col = -1;
    private Paint m_Paint;
    private RectF m_MissCircle;
    private Path m_HitPath;
    int m_Width = 0;

    private GameBoard m_Parent;
    private int m_XTouched = -1;
    private int m_YTouched = -1;
}
