package com.planes.android.screens.singleplayergame

import com.planes.singleplayerengine.PlanesRoundInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerGridViewModel @Inject constructor(planeRound: PlanesRoundInterface): PlaneGridViewModel(planeRound,false)