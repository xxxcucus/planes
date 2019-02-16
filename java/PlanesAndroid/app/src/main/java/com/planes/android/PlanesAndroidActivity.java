package com.planes.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
        //PlanesVerticalLayout parentLayout = new PlanesVerticalLayout(this);
        //setContentView(parentLayout);
    }

    private PlaneRoundJavaFx m_PlaneRound;
    TopPane_Vertical m_BoardWidgets;
    BottomPane_Vertical m_GameControls;

}
