package com.planes.android.screens.singleplayergame

import com.planes.singleplayerengine.SinglePlayerRoundInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ComputerGridViewModelSinglePlayer @Inject constructor (planeRound : SinglePlayerRoundInterface) : PlaneGridViewModel(planeRound,true)