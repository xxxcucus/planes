package com.planes.test
import com.google.common.truth.Truth
import com.planes.single_player_engine.PlaneRound
import org.junit.Test

class PlaneRoundTest {
    @Test
    fun PlaneRound_playerGuessAlreadyMade() {
        val pr = PlaneRound(10, 10, 3)
        Truth.assertThat(pr.playerGuessAlreadyMade(0, 0)).isEqualTo(0)
    }
}