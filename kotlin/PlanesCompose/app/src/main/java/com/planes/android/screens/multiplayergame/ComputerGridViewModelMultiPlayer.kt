package com.planes.android.screens.multiplayergame

import com.planes.android.repository.PlanesGameRepository
import com.planes.android.screens.singleplayergame.PlaneGridViewModel
import com.planes.singleplayerengine.SinglePlayerRoundInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ComputerGridViewModelMultiPlayer @Inject constructor(planeRound: SinglePlayerRoundInterface,
                                                         private val repository: PlanesGameRepository
): PlaneGridViewModel(planeRound,true)