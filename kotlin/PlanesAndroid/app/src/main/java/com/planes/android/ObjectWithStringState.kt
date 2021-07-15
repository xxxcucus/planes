package com.planes.android

interface ObjectWithStringState {
    fun setState(stateName: String, text: String?)
    val currentStateName: String
}