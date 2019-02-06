package com.planes.android;

import android.app.Activity;
import android.os.Bundle;

import com.planes.javafx.PlaneRoundAndroid;

public class PlanesAndroidActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //PlanesVerticalLayout parentLayout = new PlanesVerticalLayout(this);
        //setContentView(parentLayout);
    }

    private PlaneRoundAndroid m_PlaneRound;
}
