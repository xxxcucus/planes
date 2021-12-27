package com.planes.android.customviews

interface ObjectWithStringState {
    fun setState(stateName: String, text: String)
    val currentStateName: String?
}