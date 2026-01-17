package com.planes.multiplayerengine

import com.planes.singleplayerengine.PlaneRound
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class MultiplayerRound @AssistedInject constructor(
    @Assisted("rowNo") rowNo: Int,
    @Assisted("colNo") colNo: Int,
    @Assisted("planeNo") planeNo: Int): PlaneRound(rowNo, colNo, planeNo), MultiPlayerRoundInterface {

    @AssistedFactory
    interface Factory {
        fun createPlaneRound(
            @Assisted("rowNo") rowNo: Int,
            @Assisted("colNo") colNo: Int,
            @Assisted("planeNo") planeNo: Int): MultiplayerRound
    }
}