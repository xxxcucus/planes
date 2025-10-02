package com.planes.android.singleplayergame

import com.planes.singleplayerengine.PlanesRoundInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ComputerGridViewModel @Inject constructor (planeRound : PlanesRoundInterface) : PlaneGridViewModel(planeRound,true)