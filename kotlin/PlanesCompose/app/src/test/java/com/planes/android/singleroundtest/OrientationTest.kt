package com.planes.android.singleroundtest
import com.google.common.truth.Truth
import com.planes.singleplayerengine.Orientation
import org.junit.Test

class OrientationTest {
    @Test
    fun Orientation_Values() {
        Truth.assertThat(Orientation.entries.toTypedArray()[0] === Orientation.NorthSouth).isTrue()
        Truth.assertThat(Orientation.entries.toTypedArray()[1] === Orientation.SouthNorth).isTrue()
        Truth.assertThat(Orientation.entries.toTypedArray()[2] === Orientation.WestEast).isTrue()
        Truth.assertThat(Orientation.entries.toTypedArray()[3] === Orientation.EastWest).isTrue()
    }
}