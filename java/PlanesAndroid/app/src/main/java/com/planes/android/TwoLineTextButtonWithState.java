package com.planes.android;

import android.content.Context;
import android.util.AttributeSet;

public class TwoLineTextButtonWithState extends TwoLineTextButton implements ObjectWithStringState, ViewWithText {

    public TwoLineTextButtonWithState(Context context) {
        super(context);
        init();
    }

    public TwoLineTextButtonWithState(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TwoLineTextButtonWithState(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setState(String stateName) {
        //TODO: read texts from resources
        m_State = stateName;
        if (m_State == "player") {
            m_Text2 = "Player Board";
        }
        if (m_State == "computer") {
            m_Text2 = "Computer Board";
        }

        invalidate();
    }
    public String getCurrentStateName() {
        return m_State;
    }
    private String m_State;
}
