package com.planes.android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.planes.javafx.PlaneRoundJavaFx;

public class PlanesAndroidActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_PlaneRound = new PlaneRoundJavaFx();
        m_PlaneRound.createPlanesRound();

        m_BoardWidgets = (TopPane_Vertical)findViewById(R.id.top_pane);
        m_BoardWidgets.setGameSettings(m_PlaneRound);

        m_GameControls = (BottomPane_Vertical)findViewById(R.id.bottom_pane);
        m_GameControls.setGameSettings(m_PlaneRound);
        m_GameControls.setTopPane(m_BoardWidgets);
        m_BoardWidgets.setBottomPane(m_GameControls);
        //PlanesVerticalLayout parentLayout = new PlanesVerticalLayout(this);
        //setContentView(parentLayout);

        String title = "Planes Android";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getActionBar().setTitle(s);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        switch(m_PlaneRound.getGameStage()) {
            case 0:
                m_BoardWidgets.setNewRoundStage();
                m_GameControls.setNewRoundStage();
                break;
            case 1:
                m_BoardWidgets.setBoardEditingStage();
                m_GameControls.setBoardEditingStage();
                break;
            case 2:
                m_BoardWidgets.setGameStage();
                m_GameControls.setGameStage();
                break;
        }
    }


    private PlaneRoundJavaFx m_PlaneRound;
    TopPane_Vertical m_BoardWidgets;
    BottomPane_Vertical m_GameControls;

}
