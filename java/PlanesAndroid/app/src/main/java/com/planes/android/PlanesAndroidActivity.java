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

    private PlaneRoundJavaFx m_PlaneRound;
    TopPane_Vertical m_BoardWidgets;
    BottomPane_Vertical m_GameControls;

}
