package com.planes.android;

import android.content.Context;
import android.widget.LinearLayout;

//layout in portrait orientation
public class PlanesVerticalLayout extends LinearLayout {

    public PlanesVerticalLayout(Context context) {
        super(context);

        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
    }
}
