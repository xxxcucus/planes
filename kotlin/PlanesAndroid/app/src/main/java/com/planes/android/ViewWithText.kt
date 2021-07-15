package com.planes.android

interface ViewWithText {
    fun getOptimalTextSize(maxTextSize: Int, viewWidth: Int, viewHeight: Int): Int
    fun setTextSize(textSize: Int)
}