package com.planes_multiplayer.android

interface ViewWithText {
    fun getOptimalTextSize(maxTextSize: Int, viewWidth: Int, viewHeight: Int): Int
    fun setTextSize(textSize: Int)
}