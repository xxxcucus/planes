package com.planes.singleround.test.com.planes.singleround.test
import com.google.common.truth.Truth
import com.planes.single_player_engine.GameStages
import org.junit.Test

class GameStagesTest {
    @Test
    fun GameStages_Values() {
        Truth.assertThat(GameStages.values()[0] === GameStages.GameNotStarted).isTrue()
        Truth.assertThat(GameStages.values()[1] === GameStages.BoardEditing).isTrue()
        Truth.assertThat(GameStages.values()[2] === GameStages.Game).isTrue()
        Truth.assertThat(GameStages.values()[3] === GameStages.WaitForOpponentPlanesPositions).isTrue()
        Truth.assertThat(GameStages.values()[4] === GameStages.WaitForOpponentMoves).isTrue()
        Truth.assertThat(GameStages.values()[5] === GameStages.SendRemainingMoves).isTrue()
    }
}

