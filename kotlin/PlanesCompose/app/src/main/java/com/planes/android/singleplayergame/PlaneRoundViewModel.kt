package com.planes.android.singleplayergame

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.planes.singleplayerengine.GameStages

//TODO: HiltViewModel
class PlaneRoundViewModel() : ViewModel() {

    private var m_GameStage = mutableStateOf(GameStages.BoardEditing)

    fun getGameStage(): GameStages {
        return m_GameStage.value
    }
}