package com.planes.android.creategame

import androidx.lifecycle.ViewModel

class CreateGameViewModel(var gameName: String) : ViewModel() {

    var m_GameName: String

    init {
        m_GameName = gameName
    }
}