package com.planes.android.screens.norobot

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class PhotoModel(id: Int, selected: Boolean) {

    var m_ImageId: Int
    var m_Selected: Boolean

    init {
        m_ImageId = id
        m_Selected = selected
    }
}