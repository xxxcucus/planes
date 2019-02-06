package com.planes.android;

import android.app.Activity;
import android.os.Bundle;

import com.planes.javafx.PlaneRoundJavaFx;

public class PlanesAndroidActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_PlaneRound = new PlaneRoundJavaFx();
        m_PlaneRound.createPlanesRound();

        //PlanesVerticalLayout parentLayout = new PlanesVerticalLayout(this);
        //setContentView(parentLayout);
    }

    private PlaneRoundJavaFx m_PlaneRound;
}
