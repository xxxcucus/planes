package com.planes.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class PlanesAndroidActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        PlanesVerticalLayout parentLayout = new PlanesVerticalLayout(this);


        setContentView(parentLayout);
    }
}
