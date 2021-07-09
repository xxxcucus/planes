package com.planes.android;

public interface ViewWithText {
    int getOptimalTextSize(int maxTextSize, int viewWidth, int viewHeight);
    void setTextSize(int textSize);
}
