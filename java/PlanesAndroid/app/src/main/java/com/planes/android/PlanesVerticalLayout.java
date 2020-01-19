package com.planes.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


//vertical layout works with 2 GameBoard objects and 2 GameControl objects
//the game control object should change depending on the game stage
//the size of the game controls depends on the size of the visible game board
//dimension of the screen and toolbars should be saved inside the layout
public class PlanesVerticalLayout extends ViewGroup {

    class PlanesVerticalLayoutParams extends LayoutParams {

        PlanesVerticalLayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PlanesVerticalLayout);
            m_Row = a.getInt(R.styleable.PlanesVerticalLayout_gc_row, 0);
            m_Col = a.getInt(R.styleable.PlanesVerticalLayout_gc_col, 0);
            m_GameStage = a.getInt(R.styleable.PlanesVerticalLayout_gc_game_stage, 0);
            a.recycle();
        }

        PlanesVerticalLayoutParams(int width, int height) {
            super(width, height);
        }

        PlanesVerticalLayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        private int m_Row = 0;
        private int m_Col = 0;
        private int m_RowSpan = 0;
        private int m_ColSpan = 0;
        private int m_GameStage = -1;
        private boolean m_Player = false;
    }

    public PlanesVerticalLayout(Context context) {
        super(context);
        m_Context = context;
    }

    public PlanesVerticalLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_Context = context;
    }

    public PlanesVerticalLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m_Context = context;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        m_GameBoards = new ArrayList<GameBoard>();
        m_GameControls = new HashMap<Integer, ArrayList<View>>();
        m_GameControlsMaxRow = new HashMap<Integer, Integer>();
        m_GameControlsMaxCol = new HashMap<Integer, Integer>();

        //TODO: to move in a init function
        //TODO: to read only the children corresponding to the current stage
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            if (child instanceof GameBoard) {
                m_GameBoards.add((GameBoard)child);
            } else {
                PlanesVerticalLayoutParams lp = (PlanesVerticalLayoutParams) child.getLayoutParams();

                if (!(lp.m_Row == 0 || lp.m_Col == 0 || lp.m_GameStage == -1)) {
                    if (!m_GameControls.containsKey(lp.m_GameStage)) {
                        m_GameControls.put(lp.m_GameStage, new ArrayList<View>());
                        m_GameControlsMaxRow.put(lp.m_GameStage, 0);
                        m_GameControlsMaxCol.put(lp.m_GameStage, 0);
                    }
                    ArrayList<View> viewsForGameStage = m_GameControls.get(lp.m_GameStage);
                    if (lp.m_GameStage != m_GameStage.getValue()) {
                        child.setVisibility(GONE);
                    } else {
                        child.setVisibility(VISIBLE);
                    }
                    viewsForGameStage.add(child);
                    int maxRow = m_GameControlsMaxRow.get(lp.m_GameStage);
                    if (lp.m_Row > maxRow)
                        m_GameControlsMaxRow.put(lp.m_GameStage, lp.m_Row);
                    int maxCol = m_GameControlsMaxCol.get(lp.m_GameStage);
                    if (lp.m_Col > maxCol)
                        m_GameControlsMaxCol.put(lp.m_GameStage, lp.m_Col);
                }
            }
        }

        if (m_GameBoards.size() == 0 || m_GameBoards.size() > 2) {
            return;
        }

        if (m_GameBoards.size() == 2) {
            m_Tablet = true;
        }

        if (right - left < bottom - top) { //vertical layout
            m_Vertical = true;
        }

        int layoutWidth = right -left;
        int layoutHeight = bottom - top;

        if (m_GameStage == GameStages.BoardEditing) {
            if (m_Tablet) {
                if (m_Vertical) {
                    setPlayerBoardPosition(0, 0, layoutWidth,layoutHeight / 2, true);
                    setGameControlsPositions(0, layoutHeight / 2, layoutWidth, layoutHeight);
                } else {
                    setPlayerBoardPosition(0, 0,layoutWidth / 2,  layoutHeight, true);
                    setGameControlsPositions(layoutWidth / 2, 0, layoutWidth, layoutHeight);
                }
            } else {
                if (m_Vertical) {
                    setFirstBoardPosition(0, 0, layoutWidth, layoutWidth);
                    setGameControlsPositions(0, layoutWidth, layoutWidth, layoutHeight);
                } else {
                    setFirstBoardPosition(0, 0, layoutHeight, layoutHeight);
                    setGameControlsPositions(layoutHeight, 0, layoutWidth, layoutHeight);
                }
            }
        }

        if (m_GameStage == GameStages.Game) {
            if (m_Tablet) {
                if (m_Vertical) {
                    setPlayerBoardPosition(0, 0, layoutWidth,layoutHeight / 2, false);
                    setComputerBoardPosition(0, layoutHeight / 2, layoutWidth, layoutHeight, false);
                } else {
                    setPlayerBoardPosition(0, 0,layoutWidth / 2,  layoutHeight, false);
                    setComputerBoardPosition(layoutWidth / 2, 0, layoutWidth, layoutHeight, false);
                }
            } else {
                if (m_Vertical) {
                    setFirstBoardPosition(0, 0, layoutWidth, layoutWidth);
                    setGameControlsPositions(0, layoutWidth, layoutWidth, layoutHeight);
                } else {
                    setFirstBoardPosition(0, 0, layoutHeight, layoutHeight);
                    setGameControlsPositions(layoutHeight, 0, layoutWidth, layoutHeight);
                }
            }
        }

        if (m_GameStage == GameStages.GameNotStarted) {
            if (m_Tablet) {
                if (m_Vertical) {
                    if (m_ShowPlayerBoard) {
                        setPlayerBoardPosition(0, 0, layoutWidth,layoutHeight / 2, true);
                        setGameControlsPositions(0, layoutHeight / 2, layoutWidth, layoutHeight);
                    } else {
                        setGameControlsPositions(0, 0, layoutWidth,layoutHeight / 2);
                        setComputerBoardPosition(0, layoutHeight / 2, layoutWidth, layoutHeight, true);
                    }
                } else {
                    if (m_ShowPlayerBoard) {
                        setGameControlsPositions(0, 0,layoutWidth / 2,  layoutHeight);
                        setPlayerBoardPosition(layoutWidth / 2, 0, layoutWidth, layoutHeight, true);
                    } else {
                        setComputerBoardPosition(0, 0,layoutWidth / 2,  layoutHeight, true);
                        setGameControlsPositions(layoutWidth / 2, 0, layoutWidth, layoutHeight);
                    }
                }
            } else {
                if (m_Vertical) {
                    setFirstBoardPosition(0, 0, layoutWidth, layoutWidth);
                    setGameControlsPositions(0, layoutWidth, layoutWidth, layoutHeight);
                } else {
                    setFirstBoardPosition(0, 0, layoutHeight, layoutHeight);
                    setGameControlsPositions(layoutHeight, 0, layoutWidth, layoutHeight);
                }
            }
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new PlanesVerticalLayoutParams(getContext(), attrs);
    }
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }
    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }


    private void setPlayerBoardPosition(int left, int top, int right, int bottom, boolean hideOthers) {
        for (GameBoard board : m_GameBoards) {
            if (board.isPlayer()) {
                board.layout(left, top, right, bottom);
                board.setVisibility(VISIBLE);
            } else {
                if (hideOthers)
                    board.setVisibility(GONE);
                else
                    board.setVisibility(VISIBLE);
            }
        }
    }

    private void setComputerBoardPosition(int left, int top, int right, int bottom, boolean hideOthers) {
        for (GameBoard board : m_GameBoards) {
            if (!board.isPlayer()) {
                board.layout(left, top, right, bottom);
                board.setVisibility(VISIBLE);
            } else {
                if (hideOthers)
                    board.setVisibility(GONE);
                else
                    board.setVisibility(VISIBLE);
            }
        }
    }

    private void setFirstBoardPosition(int left, int top, int right, int bottom) {
        m_GameBoards.get(0).layout(left, top,  right, bottom);
    }

    private void setGameControlsPositions(int left, int top, int right, int bottom) {
        int maxRow = m_GameControlsMaxRow.get(m_GameStage.getValue());
        int maxCol = m_GameControlsMaxCol.get(m_GameStage.getValue());

        int stepX = (right - left) / (maxCol + 2);
        int stepY = (bottom - top) / (maxRow + 2);

        for (View view : m_GameControls.get(m_GameStage.getValue())) {
            PlanesVerticalLayoutParams lp = (PlanesVerticalLayoutParams) view.getLayoutParams();

            //TODO: create own buttons and textviews
            /*int measWidth = view.getMeasuredWidth();
            int measHeight = view.getMeasuredHeight();

            int actualWidth = Math.max(stepX, measWidth);
            int actualHeight = Math.max(stepY, measHeight);

            int viewCenterX = left + lp.m_Col * stepX + stepX / 2;
            int viewCenterY = top + lp.m_Row * stepY + stepY / 2;

            view.layout(viewCenterX - actualWidth / 2, viewCenterY - actualHeight / 2, viewCenterX + actualWidth / 2, viewCenterY + actualWidth / 2);*/

            view.layout(left + lp.m_Col * stepX, top + lp.m_Row * stepY, left + (lp.m_Col + 1) * stepX, top + (lp.m_Row + 1) * stepY);
        }
    }

    //TODO: is the use of invalidate appropriate?
    public void setNewRoundStage() {
        m_GameStage = GameStages.GameNotStarted;
        invalidate();
    }

    public void setGameStage() {
        m_GameStage = GameStages.Game;
        invalidate();
        requestLayout();
    }

    public void setBoardEditingStage() {
        m_GameStage = GameStages.BoardEditing;
        invalidate();
        requestLayout();
    }

    public void setPlayerBoard() {
        m_ShowPlayerBoard = true;
        invalidate();
        requestLayout();
    }

    public void setComputerBoard() {
        m_ShowPlayerBoard = false;
        invalidate();
        requestLayout();
    }


    private ArrayList<GameBoard> m_GameBoards;
    private HashMap<Integer, ArrayList<View>> m_GameControls;
    private HashMap<Integer, Integer> m_GameControlsMaxRow;
    private HashMap<Integer, Integer> m_GameControlsMaxCol;

    private Context m_Context;
    private GameStages m_GameStage = GameStages.BoardEditing;
    private boolean m_CorrectChildren;

    private boolean m_ShowPlayerBoard = false;  //in start new game stage which board to show
    private boolean m_Tablet = false;
    private boolean m_Vertical = false;
}
