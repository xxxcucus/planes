package com.planes.singleround.test.com.planes.singleround.test
import com.planes.single_player_engine.GuessPoint
import com.planes.single_player_engine.HeadData
import com.planes.single_player_engine.PlaneOrientationData
import com.planes.single_player_engine.Type
import org.junit.Test
import com.google.common.truth.Truth

class HeadDataTest {
    @Test
    fun HeadData_Init() {
        val hd = HeadData(10, 10, 5, 5)
        hd.update(GuessPoint(5, 5, Type.Dead))
    }

    @Test
    fun HeadData_UpdateCorrectOrientSet() {
        var hd = HeadData(10, 10, 5, 5)
        hd.m_correctOrient = 0
        Truth.assertThat(hd.update(GuessPoint(0, 0))).isTrue()
    }

    @Test
    fun HeadData_UpdateOneOrientationCompletAndNotDiscarded() {
        var hd = HeadData(10, 10, 5, 5)
        var pod = PlaneOrientationData()  //already correct orientation found
        pod.m_discarded = false
        hd.m_options[0] = pod
        Truth.assertThat(hd.update(GuessPoint(0, 0))).isTrue()
        Truth.assertThat(0 == hd.m_correctOrient).isTrue()
    }

    @Test
    fun HeadData_UpdateThreeOrientationsDiscardedAndOneNotDiscarded() {
        var hd = HeadData(10, 10, 5, 5)
        hd.m_options[0].m_discarded = true
        hd.m_options[1].m_discarded = true
        hd.m_options[2].m_discarded = true
        hd.m_options[3].m_discarded = false
        Truth.assertThat(hd.update(GuessPoint(0, 0))).isTrue()
        Truth.assertThat(3 == hd.m_correctOrient).isTrue()
    }

    @Test
    fun HeadData_UpdateFourOrientationsDiscarded() {
        var hd = HeadData(10, 10, 5, 5)
        hd.m_options[0].m_discarded = true
        hd.m_options[1].m_discarded = true
        hd.m_options[2].m_discarded = true
        hd.m_options[3].m_discarded = true
        Truth.assertThat(hd.update(GuessPoint(0, 0))).isFalse()
    }

    @Test
    fun HeadData_UpdateTwoOrientationsDiscardedAndTwoNotDiscarded() {
        var hd = HeadData(10, 10, 5, 5)
        hd.m_options[0].m_discarded = true
        hd.m_options[1].m_discarded = true
        hd.m_options[2].m_discarded = false
        hd.m_options[3].m_discarded = false
        Truth.assertThat(!hd.update(GuessPoint(0, 0))).isTrue()
    }
}