package com.planes.android.singleroundtest
import com.google.common.truth.Truth
import com.planes.singleplayerengine.PlaneRound
import org.junit.Test

class PlaneRoundTest {
    @Test
    fun PlaneRound_playerGuessAlreadyMade() {
        val pr = PlaneRound(10, 10, 3)
        Truth.assertThat(pr.playerGuessAlreadyMade(0, 0)).isFalse()
    }
}