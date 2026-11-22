package com.planes.android.singleroundtest
import com.google.common.truth.Truth
import com.planes.singleplayerengine.GameStages
import org.junit.Test

class GameStagesTest {
    @Test
    fun GameStages_Values() {
        Truth.assertThat(GameStages.entries.toTypedArray()[0] === GameStages.GameNotStarted).isTrue()
        Truth.assertThat(GameStages.entries.toTypedArray()[1] === GameStages.BoardEditing).isTrue()
        Truth.assertThat(GameStages.entries.toTypedArray()[2] === GameStages.Game).isTrue()
        Truth.assertThat(GameStages.entries.toTypedArray()[3] === GameStages.WaitForOpponentPlanesPositions).isTrue()
        Truth.assertThat(GameStages.entries.toTypedArray()[4] === GameStages.WaitForOpponentMoves).isTrue()
        Truth.assertThat(GameStages.entries.toTypedArray()[5] === GameStages.SendRemainingMoves).isTrue()
    }
}

