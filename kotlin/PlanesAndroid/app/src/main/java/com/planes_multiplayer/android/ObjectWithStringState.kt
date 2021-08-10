package com.planes_multiplayer.android

interface ObjectWithStringState {
    fun setState(stateName: String, text: String)
    val currentStateName: String?
}