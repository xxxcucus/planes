package com.planes.test
import com.google.common.truth.Truth
import com.planes.single_player_engine.PlaneOrientationData
import org.junit.Test

class PlaneOrientationDataTest {
    @Test
    fun PlaneOrientationData_AreAllPointsTested() {
        val pod = PlaneOrientationData()
        Truth.assertThat(pod.areAllPointsChecked()).isTrue()
    }
}