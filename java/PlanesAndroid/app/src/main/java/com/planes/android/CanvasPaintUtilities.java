package com.planes.android;


import android.graphics.Path;

public class CanvasPaintUtilities {

    public static void createFillRectPath(Path path, int left, int top, int width, int height) {
        path.moveTo(left, top);
        path.lineTo(left + width, top);
        path.lineTo(left + width, top + height);
        path.lineTo(left, top + height);
        path.close();
    }
}
