package com.planes.android.screens.multiplayergame

import com.planes.android.repository.PlanesGameRepository
import com.planes.android.screens.singleplayergame.PlaneGridViewModel
import com.planes.multiplayerengine.MultiPlayerRoundInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ComputerGridViewModelMultiPlayer @Inject constructor(planeRound: MultiPlayerRoundInterface,
                                                         private val repository: PlanesGameRepository
): PlaneGridViewModel(planeRound,true)